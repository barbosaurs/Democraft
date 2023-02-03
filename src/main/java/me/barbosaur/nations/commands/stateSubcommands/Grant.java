package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.libs.Notifications;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Grant implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!State.IsCitizen(p)) {
            sender.sendMessage(Lang.getLang("not_citizen"));
            return;
        }

        if (args.length != 2) {
            sender.sendMessage(Lang.getLang("incorrect_args"));
            return;
        }

        if (!State.getPlayerCountry(p).leader.equals(p)) {
            sender.sendMessage(Lang.getLang("not_enough_perms"));
            return;
        }

        if (!State.getPlayerCountry(p).players.contains(args[1])) {
            sender.sendMessage(Lang.getLang("no_such_player"));
            return;
        }

        if (State.getPlayerCountry(p).leaders.contains(args[1])) {
            sender.sendMessage(Lang.getLang("already_granted"));
        }

        for(State state : Nations.states){
            if(!state.leader.equals(p)){
                continue;
            }
            state.leaders.add(args[1]);
            sender.sendMessage(Lang.getLang("granted", args[1]));
            Notifications.notify(args[1], Lang.getLang("you_were_granted"));
        }
    }

}
