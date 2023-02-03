package me.barbosaur.nations.commands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.libs.RomanNumbers;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class DemocraftCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        TextComponent discordClick = new TextComponent(Lang.getLang("clickable_element"));

        discordClick.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Nations.config.getString("discord_link")));

        TextComponent mapClick = new TextComponent(Lang.getLang("clickable_element"));

        mapClick.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, Nations.config.getString("map_link")));

        TextComponent democraftTitle = new TextComponent(Lang.getLang("democraft", RomanNumbers.intToRoman(Nations.config.getInt("season"))) + "\n");

        sender.spigot().sendMessage(democraftTitle, new TextComponent(Lang.getLang("democraft_our_discord")), discordClick,
                new TextComponent("\n"), new TextComponent(Lang.getLang("democraft_map")), mapClick);

        return true;
    }

}
