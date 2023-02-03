package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reload implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!sender.hasPermission("nations.reload")){
            sender.sendMessage(Lang.getLang("not_enough_perms"));
            return;
        }

        Configs.reload();
        sender.sendMessage(Lang.getLang("reload"));
    }

}
