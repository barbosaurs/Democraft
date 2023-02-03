package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import me.barbosaur.nations.libs.Notifications;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Join implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!Discord.isVerified(p)) {
            sender.sendMessage(Lang.getLang("not_verified"));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(Lang.getLang("incorrect_args"));
            return;
        }

        if (State.IsCitizen(p) || State.IsCitizenUnconfirmed(p)) {
            sender.sendMessage(Lang.getLang("already_citizen"));
            return;
        }

        if (!State.CountryExists(args[1])) {
            sender.sendMessage(Lang.getLang("state_doesnt_exist"));
            return;
        }

        if(State.GetByName(args[1]).requests.contains(p)) {
            sender.sendMessage(Lang.getLang("request_already_sent"));
            return;
        }

        if(Nations.stateCooldown.get(p) > 0) {
            sender.sendMessage(Lang.getLang("wait", Nations.stateCooldown.get(p)));
            return;
        }

        for (State state : Nations.states) {
            if (state.name.equalsIgnoreCase(args[1])) {
                state.requests.add(p);
                for (String s : state.leaders) {
                    Notifications.notify(s, Lang.getLang("request_from", p));
                }
                sender.sendMessage(Lang.getLang("request_sent"));
                break;
            }
        }
    }

}
