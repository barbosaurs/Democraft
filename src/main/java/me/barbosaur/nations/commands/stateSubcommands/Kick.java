package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import me.barbosaur.nations.libs.Notifications;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Kick implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!State.IsCitizen(p)) {
            sender.sendMessage(Lang.getLang("not_citizen"));
            return;
        }
        
        if(!State.getPlayerCountry(p).leaders.contains(p)) {
            sender.sendMessage(Lang.getLang("not_enough_perms"));
            return;
        }

        if(args.length != 2){
            sender.sendMessage(Lang.getLang("incorrect_args"));
            return;
        }

        if (!State.getPlayerCountry(p).players.contains(args[1])) {
            sender.sendMessage(Lang.getLang("no_such_player"));
            return;
        }

        if(State.getPlayerCountry(p).leaders.contains(args[1])){
            sender.sendMessage(Lang.getLang("you_cant_kick"));
            return;
        }

        if(State.getPlayerCountry(p).players.size() < State.minimumPlayersForState) {
            sender.sendMessage(Lang.getLang("will_be_not_enough"));
            return;
        }

        for (State state : Nations.states) {
            if (!state.players.contains(p)) {
                continue;
            }

            state.players.remove(args[1]);
            Notifications.notify(args[1], Lang.getLang("kicked", state.name));
            sender.sendMessage(Lang.getLang("kicked_player", args[1]));
            Nations.stateCooldown.put(args[1], State.stateCooldownIfLeftGive);
            DynMap.updateMap();
            break;
        }
    }

}
