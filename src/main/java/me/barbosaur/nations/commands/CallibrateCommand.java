package me.barbosaur.nations.commands;

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
        if(TntLaunchers.validLauncher(block)){
            if(TntLaunchers.launchersAngle.containsKey(block) && TntLaunchers.launcherTNTcount.containsKey(block)){
                if(args.length == 3){
                    Double arg1 = Double.valueOf(args[0]);
                    Double arg2 = Double.valueOf(args[1]);
                    Double arg3 = Double.valueOf(args[2]);
                    if((arg1 <= 5) && (arg2 <= 5) && (arg3 <= 5) && (arg1 >= -5) && (arg2 >= -5) && (arg3 >= -5)){
                        TntLaunchers.launchersAngle.put(block, new Vector(arg1, arg2, arg3));
                        sender.sendMessage("Установлен курс. X: " + arg1 + ", Y: " + arg2 + ", Z: " + arg3);
                    }else{
                        sender.sendMessage("Слишком много мощи");
                    }
                }
            }
        }else{
            sender.sendMessage("Это не установка!");
        }
        return true;
    }

}
