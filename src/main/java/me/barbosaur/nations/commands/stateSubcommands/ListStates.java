package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class ListStates implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){

        int stateAmount = 5;
        int page = 1;
        if(args.length >= 2){
            int pageArg = Integer.valueOf(args[1]);
            if(pageArg > 0){
                page = pageArg;
            }
        }

        int pageCount = Nations.states.size()/stateAmount;
        if(Nations.states.size()%stateAmount>0){
            pageCount++;
        }

        String string = Lang.getLang("state_list") + ":\n";

        for(int i = 0; i<stateAmount; i++){
            if(((page - 1) * stateAmount + i+1)<=Nations.states.size()) {
                string += (Nations.states.get((page - 1) * stateAmount + i).name + "\n");
            }else{
                break;
            }
        }

        string += (Lang.getLang("page") + " " + page + "/" + pageCount);

        sender.sendMessage(string);
    }

}
