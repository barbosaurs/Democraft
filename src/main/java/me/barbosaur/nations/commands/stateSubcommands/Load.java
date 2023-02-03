package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.SaveLoad;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Load implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!sender.hasPermission("state.load")){
            sender.sendMessage(Lang.getLang("not_enough_perms"));
            return;
        }

        SaveLoad.loadStates();
        sender.sendMessage(Lang.getLang("loaded"));
    }

}
