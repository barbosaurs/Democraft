package me.barbosaur.nations;

import me.barbosaur.nations.libs.Notifications;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class InactiveTime implements Listener {

    public static void onStart(){
        new BukkitRunnable() {
            @Override
            public void run() {
                List<String> players = new ArrayList<>();
                for(Player player : Bukkit.getOnlinePlayers()){
                    players.add(player.getDisplayName());
                }

                for(String player : Nations.inactiveTime.keySet()){
                    if(players.contains(player)) {
                        continue;
                    }

                    if(Nations.inactiveTime.get(player) == 259200){
                        Notifications.notify(player,
                                "Вы давно не заходили на сервер. Зайдите, чтобы не потерять свой прогресс в государстве");
                        if(State.getPlayerCountry(player) != null) {
                            Notifications.notify(State.getPlayerCountry(player).leader, "Игрок " + player
                                    + " в вашем государстве долго не заходил на сервер");
                        }
                    }

                    Nations.inactiveTime.put(player, Nations.inactiveTime.get(player) + 1);
                    if(Nations.inactiveTime.get(player) > 518400){
                        for(State state : Nations.states){
                            if(state.players.contains(player)) {
                                state.players.remove(player);
                                if (state.players.size() < State.minimumPlayersForState) {
                                    state.notifyAll("Ваше государство распалось");
                                    for(String s : state.players) {
                                        Discord.guild.getMemberByTag(Nations.discords.get(s)).getRoles().remove(Discord.guild.getRoleById(state.roleId));
                                    }
                                    Nations.states.remove(state);
                                    DynMap.updateMap();
                                }
                            }
                        }
                    }
                }

            }
        }.runTaskTimer(Nations.getPlugin(), 20, 20);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(!Discord.isVerified(e.getPlayer().getDisplayName())){
            return;
        }

        Nations.inactiveTime.put(e.getPlayer().getDisplayName(), 0);
    }

}
