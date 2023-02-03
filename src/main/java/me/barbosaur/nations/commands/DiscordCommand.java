package me.barbosaur.nations.commands;

import me.barbosaur.nations.Discord;
import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class DiscordCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String subcmd = args[0];

        if(subcmd.equalsIgnoreCase("link")) {
            if (Nations.discords.containsKey(sender.getName())) {
                sender.sendMessage(Lang.getLang("already_verified"));
                return true;
            }

            if (args.length < 2) {
                sender.sendMessage(Lang.getLang("incorrect_args_discord_link"));
                return true;
            }

            String discordTag = args[1];
            if(args.length > 2){
                for(int i = 2; i < args.length; i++){
                    discordTag += (" " + args[i]);
                }
            }

            if(Nations.discords.containsValue(discordTag)) {
                sender.sendMessage(Lang.getLang("this_discord_already_exists"));
                return true;
            }

            Guild guild = Discord.jda.getGuildById("776137007876407297");
            if(guild == null){
                sender.sendMessage(Lang.getLang("serverside_error"));
                return true;
            }
            Member member = guild.getMemberByTag(discordTag);
            if (member == null) {
                TextComponent component = new TextComponent(Lang.getLang("clickable_element"));
                component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/D7eB3GjytS"));
                sender.spigot().sendMessage(new TextComponent(Lang.getLang("incorrect_tag")), component);
                return true;
            }

            Nations.getPlugin().getLogger().info("Found user");
            User user = member.getUser();
            int code = ThreadLocalRandom.current().nextInt(1000, 9999);

            while (Discord.codes.containsValue(code)) {
                code = ThreadLocalRandom.current().nextInt(1000, 9999);
            }

            Discord.codes.put(sender.getName(), code);
            Discord.tempDiscords.put(sender.getName(), discordTag);

            int finalCode = code;
            user.openPrivateChannel().queue((channel) ->
            {
                channel.sendMessage(Lang.getLang("your_code") + ": " + finalCode + ". /discord code <код>").queue();
            });
        }else if(subcmd.equalsIgnoreCase("code")){

            if (!Nations.discords.containsKey(sender.getName())) {
                if (args.length == 2) {

                    if(Discord.codes.get(sender.getName()).equals(Integer.valueOf(args[1]))){
                        Nations.discords.put(sender.getName(), Discord.tempDiscords.get(sender.getName()));
                        Discord.codes.remove(sender.getName());
                        Discord.tempDiscords.remove(sender.getName());
                        sender.sendMessage(Lang.getLang("account_verified"));
                        Nations.inactiveTime.put(sender.getName(), 0);
                    }else{
                        sender.sendMessage(Lang.getLang("wrong_code"));
                    }

                } else {
                    sender.sendMessage(Lang.getLang("incorrect_args_discord_code"));
                }
            } else {
                sender.sendMessage(Lang.getLang("already_verified"));
            }
        }else{
            sender.sendMessage(Lang.getLang("discord_help"));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(command.getLabel().equalsIgnoreCase("discord")){
            if(args.length == 1){
                List<String> strings = new ArrayList<>();
                strings.add("link");
                strings.add("code");
                return strings;
            }else if(args[0].equalsIgnoreCase("link") && (args.length == 2)){
                List<String> strings = new ArrayList<>();
                Guild guild = Discord.jda.getGuildById("776137007876407297");
                if(guild == null){
                    return null;
                }
                for(Member m : guild.getMembers()){
                    strings.add(m.getUser().getAsTag());
                }
                return strings;
            }
        }
        return null;
    }

}
