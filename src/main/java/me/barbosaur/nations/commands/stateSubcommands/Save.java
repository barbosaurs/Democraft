package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.SaveLoad;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Save implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!sender.hasPermission("state.save")){
            sender.sendMessage(Lang.getLang("not_enough_perms"));
            return;
        }

        SaveLoad.saveStates();
        sender.sendMessage(Lang.getLang("saved"));
    }

}
