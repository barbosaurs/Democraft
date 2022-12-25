package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.Notifications;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateCommand;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Claim implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(State.IsCitizen(p)) {
            if (State.IsOccupied(chunk)) {
                if(!Nations.claimingTimer.containsKey(chunk)) {
                    if (!State.getPlayerCountry(p).name.equals(State.getByChunk(chunk).name)) {
                        if (((Player) sender).getLocation().getY() >= 50) {
                            if (State.getByChunk(chunk).isOnline(60)) {
                                if(State.getPlayerCountry(p).invasionCooldown <= 0) {
                                    sender.sendMessage("Завоевание чанка " + State.getByChunk(chunk).name + " началось");
                                    State.getByChunk(chunk).notifyAll("Вашу территорию захватывает государство " + State.getPlayerCountry(p).name
                                            + " (" + (chunk.getX()*16) + ", " + (chunk.getZ()*16) + ")");
                                    State.getPlayerCountry(p).notifyAll("Ваше государство захватывает территорию государства " + State.getPlayerCountry(p).name
                                            + " (" + (chunk.getX()*16) + ", " + (chunk.getZ()*16) + ")");
                                    Nations.claimingBy.put(chunk, State.getPlayerCountry(p));
                                    Nations.bossbars.put(chunk, Bukkit.createBossBar("Завоевание чанка государством " + State.getPlayerCountry(p).name, BarColor.RED, BarStyle.SOLID));
                                    Nations.bossbars.get(chunk).setProgress(0.0);
                                    Nations.bossbars.get(chunk).setVisible(true);
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            java.util.List<Entity> entities = Arrays.asList(chunk.getEntities());
                                            java.util.List<Player> players = new ArrayList<>();
                                            for (Entity e : entities) {
                                                if (e instanceof Player) {
                                                    players.add((Player) e);
                                                }
                                            }
                                            List<Player> inChunk = new ArrayList<>();
                                            for (Player player : players) {
                                                if(State.IsCitizen(player.getDisplayName())) {
                                                    if (State.ChunkEqual(player.getLocation().getChunk(), chunk) && (player.getLocation().getY() >= 50)
                                                            && (State.getPlayerCountry(player.getDisplayName()).name.equals(State.getPlayerCountry(p).name))) {
                                                        inChunk.add(player);
                                                    }
                                                }
                                            }

                                            if (!inChunk.isEmpty()) {
                                                if (!Nations.claimingTimer.containsKey(chunk)) {
                                                    Nations.claimingTimer.put(chunk, 0);

                                                } else {
                                                    if (Nations.claimingTimer.get(chunk) < 600) {
                                                        Nations.claimingTimer.put(chunk, Nations.claimingTimer.get(chunk) + 1);
                                                        Nations.bossbars.get(chunk).setProgress(Nations.claimingTimer.get(chunk) / 600.0);
                                                    } else {
                                                        for (int i = 0; i < Nations.states.size(); i++) {
                                                            if (State.ContainsChunk(Nations.states.get(i).territory, chunk)) {
                                                                State.RemoveChunkFromList(Nations.states.get(i).territory, chunk);
                                                                if (Nations.states.get(i).territory.isEmpty()) {
                                                                    Nations.states.get(i).notifyAll("Ваша страна была уничтожена");
                                                                    Nations.states.remove(Nations.states.get(i));
                                                                }else{
                                                                    Nations.states.get(i).notifyAll("Чанк вашего государства был захвачен государством "
                                                                            + State.getPlayerCountry(p).name + " (" + (chunk.getX()*16) + ", " + (chunk.getZ()*16) + ")");
                                                                }
                                                            } else if (Nations.states.get(i).players.contains(p) && (!State.ContainsChunk(Nations.states.get(i).territory, chunk))) {
                                                                Nations.states.get(i).territory.add(chunk);
                                                                sender.sendMessage("Чанк завоеван");
                                                            }
                                                        }
                                                        Nations.claimingTimer.remove(chunk);
                                                        Nations.claimingBy.remove(chunk);
                                                        Nations.bossbars.get(chunk).removeAll();
                                                        for(Player player : Nations.bossbars.get(chunk).getPlayers()){
                                                            Nations.bossbars.get(chunk).removePlayer(player);
                                                        }
                                                        Nations.bossbars.remove(chunk);
                                                        State.updateMap();
                                                        cancel();
                                                    }

                                                }
                                            } else {
                                                Nations.claimingTimer.remove(chunk);
                                                Nations.claimingBy.remove(chunk);
                                                Nations.bossbars.get(chunk).removeAll();
                                                for(Player player : Nations.bossbars.get(chunk).getPlayers()){
                                                    Nations.bossbars.get(chunk).removePlayer(player);
                                                }
                                                Nations.bossbars.remove(chunk);
                                                sender.sendMessage("Попытка завоевания сорвалась!");
                                                for (int i = 0; i < Nations.states.size(); i++) {
                                                    Nations.states.get(i).invasionCooldown = State.invasionCooldownGive;
                                                }
                                                State.updateMap();
                                                cancel();
                                            }

                                        }
                                    }.runTaskTimer(Nations.getPlugin(), 20, 20);
                                }else{
                                    sender.sendMessage("Подождите еще " + State.getPlayerCountry(p).invasionCooldown + " секунд");
                                }
                            } else {
                                sender.sendMessage("В атакуемом государстве в сети недостаточно игроков");
                            }
                        } else {
                            sender.sendMessage("Поднимитесь повыше (y50)");
                        }
                    } else {
                        sender.sendMessage("Это ваше государство");
                    }
                }else{
                    if(Nations.claimingBy.get(chunk).players.contains(p)) {
                        sender.sendMessage("Захват чанка " + Nations.claimingTimer.get(chunk) + "/60");
                    }else{
                        sender.sendMessage("Чанк государства " + State.getByChunk(chunk).name + " захватывается государством " + Nations.claimingBy.get(chunk).name);
                    }
                }
            } else {
                if(State.getPlayerCountry(p).chunkTakeCooldown <= 0) {
                    if(State.getPlayerCountry(p).canTakeChunks > 0) {
                        for (int i = 0; i < Nations.states.size(); i++) {
                            if (Nations.states.get(i).players.contains(p)) {
                                sender.sendMessage("Вы заняли чанк");
                                Nations.states.get(i).territory.add(chunk);
                                Nations.states.get(i).canTakeChunks -= 1;
                                if(State.getPlayerCountry(p).canTakeChunks == 0){
                                    Nations.states.get(i).chunkTakeCooldown = State.chunkTakeCooldownGive;
                                }
                                for (State state : Nations.unconfirmedStates) {
                                    if (State.ContainsChunk(state.territory, chunk)) {
                                        Nations.stateCooldown.put(state.leader, State.stateCooldownGive);
                                        Nations.unconfirmedStates.remove(state);
                                        for (String s : state.players) {
                                            Notifications.notify(s, "Территория вашего неподтвержденного государства была занята");
                                        }
                                        break;
                                    }
                                }
                                State.updateMap();
                                break;
                            }
                        }
                    }else{
                        for (int i = 0; i < Nations.states.size(); i++) {
                            if (Nations.states.get(i).players.contains(p)) {
                                Nations.states.get(i).chunkTakeCooldown = State.chunkTakeCooldownGive;
                                sender.sendMessage("Подождите еще " + Nations.states.get(i).chunkTakeCooldown + " секунд");
                                break;
                            }
                        }
                    }
                }else{
                    sender.sendMessage("Подождите еще " + State.getPlayerCountry(p).chunkTakeCooldown + " секунд");
                }

            }
        }else{
            sender.sendMessage("Вы не являетесь гражданином государства");
        }
    }

}
