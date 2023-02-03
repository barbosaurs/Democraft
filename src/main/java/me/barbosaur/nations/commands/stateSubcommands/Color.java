package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.DynMap;
import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Color implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!State.IsCitizen(p)) {
            sender.sendMessage(Lang.getLang("not_citizen"));
            return;
        }

        if(args.length != 2) {
            sender.sendMessage(Lang.getLang("incorrect_args"));
            return;
        }

        if(!Nations.colors.containsKey(args[1])) {
            String list = "";
            for(int i = 0; i<Nations.colors.keySet().size(); i++){
                if((i+1)==Nations.colors.keySet().size()){
                    list = list + " " + Nations.colors.keySet().toArray()[i];
                }else{
                    list = list + " " + Nations.colors.keySet().toArray()[i] + ",";
                }
            }

            sender.sendMessage(Lang.getLang("incorrect_color", list));
            return;
        }

        for (State state : Nations.states) {
            if (!state.leaders.contains(p)) {
                continue;
            }

            state.color = Nations.colors.get(args[1]);
            sender.sendMessage(Lang.getLang("color_set", args[1]));
            DynMap.updateMap();
        }
    }

}
