package me.barbosaur.nations.commands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.TntLaunchers;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class CallibrateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Block block = ((Player)sender).getTargetBlockExact(5);

        if(block == null){
            return true;
        }

        if(!TntLaunchers.validLauncher(block)){
            sender.sendMessage(Lang.getLang("face_to_launcher"));
            return true;
        }

        if(!TntLaunchers.launchersAngle.containsKey(block) || !TntLaunchers.launcherTNTcount.containsKey(block)){
            sender.sendMessage(Lang.getLang("create_launcher"));
            return true;
        }

        if(args.length != 3){
            sender.sendMessage(Lang.getLang("incorrect_args_callibrate"));
            return true;
        }

        double arg1 = Double.parseDouble(args[0]);
        double arg2 = Double.parseDouble(args[1]);
        double arg3 = Double.parseDouble(args[2]);
        if(!((arg1 <= 5) && (arg2 <= 5) && (arg3 <= 5) && (arg1 >= -5) && (arg2 >= -5) && (arg3 >= -5))){
            sender.sendMessage(Lang.getLang("calibrate_values"));
            return true;
        }

        TntLaunchers.launchersAngle.put(block, new Vector(arg1, arg2, arg3));
        sender.sendMessage(Lang.getLang("angle_set", arg1, arg2, arg3));
        return true;
    }

}
