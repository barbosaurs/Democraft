package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.libs.Notifications;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Deny implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(args.length != 2){
            sender.sendMessage(Lang.getLang("incorrect_args"));
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
            }
            sender.sendMessage(Lang.getLang("player_denied", args[1]));
            Notifications.notify(args[1], Lang.getLang("you_were_denied", state.name));
            state.requests.remove(args[1]);
            break;
        }
    }

}
