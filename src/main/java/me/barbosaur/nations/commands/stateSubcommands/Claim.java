package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.DynMap;
import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Bukkit;
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
import java.util.Objects;

public class Claim implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!State.IsCitizen(p)) {
            sender.sendMessage(Lang.getLang("not_citizen"));
            return;
        }

        if (!State.IsOccupied(chunk)) {
            claimUnoccupied(sender, p, chunk);
            return;
        }

        claimOccupied(sender, p, chunk);

    }

    private static void claimUnoccupied(CommandSender sender, String p, Chunk chunk){
        if(State.getPlayerCountry(p).chunkTakeCooldown > 0) {
            sender.sendMessage(Lang.getLang("wait", State.getPlayerCountry(p).chunkTakeCooldown));
            return;
        }

        if(State.getPlayerCountry(p).canTakeChunks <= 0) {
            for (State state : Nations.states) {
                if (state.players.contains(p)) {
                    continue;
                }
                state.chunkTakeCooldown = State.chunkTakeCooldownGive;
                sender.sendMessage(Lang.getLang("wait", state.chunkTakeCooldown));
                return;
            }
            return;
        }

        for (State state : Nations.states) {
            if (!state.players.contains(p)) {
                continue;
            }

            sender.sendMessage(Lang.getLang("took_chunk"));
            state.territory.add(chunk);
            state.canTakeChunks -= 1;
            if(State.getPlayerCountry(p).canTakeChunks == 0){
                state.chunkTakeCooldown = State.chunkTakeCooldownGive;
            }
            for (State state1 : Nations.unconfirmedStates) {
                if (!State.ContainsChunk(state1.territory, chunk)
                || Objects.equals(State.getPlayerCountry(p), State.getByChunk(chunk))) {
                    continue;
                }
                Nations.stateCooldown.put(state1.leader, State.stateCooldownGive);
                state1.notifyAll(Lang.getLang("unc_state_occupied"));
                Nations.unconfirmedStates.remove(state1);
                break;
            }
            DynMap.updateMap();
            break;
        }
    }

    private static void claimOccupied(CommandSender sender, String p, Chunk chunk){
        if(Nations.claimingTimer.containsKey(chunk)) {
            if(Nations.claimingBy.get(chunk).players.contains(p)) {
                sender.sendMessage(Lang.getLang("chunk_capture_progress",
                        Nations.claimingTimer.get(chunk), State.claimTime));
            }else{
                sender.sendMessage(Lang.getLang("chunk_being_captured",
                        State.getByChunk(chunk).name), Nations.claimingBy.get(chunk).name);
            }
            return;
        }

        if (State.getPlayerCountry(p).name.equals(State.getByChunk(chunk).name)) {
            sender.sendMessage(Lang.getLang("its_your_state"));
            return;
        }

        if (((Player) sender).getLocation().getY() < State.minCaptureHeight) {
            sender.sendMessage(Lang.getLang("get_higher", State.minCaptureHeight));
            return;
        }

        if (!State.getByChunk(chunk).isOnline(State.minCapturePercentage)) {
            sender.sendMessage(Lang.getLang("not_enough_players_for_invasion"));
            return;
        }

        if(State.getPlayerCountry(p).invasionCooldown > 0) {
            sender.sendMessage(Lang.getLang("wait", State.getPlayerCountry(p).invasionCooldown));
            return;
        }

        sender.sendMessage(Lang.getLang("capture_started", State.getByChunk(chunk).name));
        State.getByChunk(chunk).notifyAll(Lang.getLang("invasion_in_your_country",
                State.getPlayerCountry(p).name, chunk.getX()*16, chunk.getZ()*16));
        State.getPlayerCountry(p).notifyAll(Lang.getLang("your_country_invaded",
                State.getByChunk(chunk).name, chunk.getX()*16, chunk.getZ()*16));
        Nations.claimingBy.put(chunk, State.getPlayerCountry(p));
        Nations.bossbars.put(chunk, Bukkit.createBossBar(Lang.getLang("chunk_being_captured_short", State.getPlayerCountry(p).name), BarColor.RED, BarStyle.SOLID));
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
                                        Nations.states.get(i).notifyAll(Lang.getLang("your_country_was_destroyed"));
                                        Nations.states.remove(Nations.states.get(i));
                                    }else{
                                        Nations.states.get(i).notifyAll(Lang.getLang("your_chunk_captured",
                                                State.getPlayerCountry(p).name, chunk.getX()*16, chunk.getZ()*16));
                                    }
                                } else if (Nations.states.get(i).players.contains(p) && (!State.ContainsChunk(Nations.states.get(i).territory, chunk))) {
                                    Nations.states.get(i).territory.add(chunk);
                                    sender.sendMessage(Lang.getLang("chunk_captured"));
                                }
                            }
                            Nations.claimingTimer.remove(chunk);
                            Nations.claimingBy.remove(chunk);
                            Nations.bossbars.get(chunk).removeAll();
                            for(Player player : Nations.bossbars.get(chunk).getPlayers()){
                                Nations.bossbars.get(chunk).removePlayer(player);
                            }
                            Nations.bossbars.remove(chunk);
                            DynMap.updateMap();
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
                    sender.sendMessage(Lang.getLang("capture_failure"));
                    for (int i = 0; i < Nations.states.size(); i++) {
                        Nations.states.get(i).invasionCooldown = State.invasionCooldownGive;
                    }
                    DynMap.updateMap();
                    cancel();
                }

            }
        }.runTaskTimer(Nations.getPlugin(), 20, 20);

    }

}
