package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import me.barbosaur.nations.libs.Notifications;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Accept implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(args.length != 2){
            return;
        }

        if(!State.IsCitizen(p)) {
            sender.sendMessage(Lang.getLang("not_citizen"));
            return;
        }

        for (State state : Nations.states) {
            if (!state.leaders.contains(p)) {
                continue;
            }
            if (!state.requests.contains(args[1])) {
                sender.sendMessage(Lang.getLang("no_such_request"));
                return;
            }

            sender.sendMessage(Lang.getLang("request_accepted", args[1]));
            Notifications.notify(args[1], Lang.getLang("you_were_accepted", state.name));
            state.requests.remove(args[1]);
            state.players.add(args[1]);
            DynMap.updateMap();
        }
    }

}
