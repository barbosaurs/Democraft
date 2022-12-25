package me.barbosaur.nations;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

public class Notifications{

    public static HashMap<String, String> notifications = new HashMap<>();

    public static void notify(String player, String message){
        for(Player player1 : Bukkit.getOnlinePlayers()){
            if(player1.getDisplayName().equals(player)){
                player1.sendMessage(message);
                return;
            }
        }
        if(Nations.discords.containsKey(player)) {
            Guild guild = Discord.jda.getGuildById("776137007876407297");
            Member member = guild.getMemberByTag(Nations.discords.get(player));
            if (member != null) {
                System.out.println("nonull");
                User user = member.getUser();
                user.openPrivateChannel().queue((channel) ->
                {
                    channel.sendMessage(message).queue();
                });
            }
        }else{
            notifications.put(player, message);
        }
    }

}
