package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import me.barbosaur.nations.libs.Notifications;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Invite implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(args.length != 2){
            sender.sendMessage(Lang.getLang("incorrect_args"));
            return;
        }

        State state = State.leaderOfWhatUncornfirmed(p);
        State state1 = state;
        if(state == null){
            sender.sendMessage(Lang.getLang("not_leader_of_unc_state"));
            return;
        }

        if(State.IsCitizen(args[1]) || State.IsCitizenUnconfirmed(args[1])) {
            sender.sendMessage(Lang.getLang("player_already_citizen"));
            return;
        }

        if (!Discord.isVerified(args[1])) {
            sender.sendMessage(Lang.getLang("player_not_verified"));
        }

        if (state.invitations.contains(args[1])) {
            sender.sendMessage(Lang.getLang("already_invited"));
            return;
        }

        state1.invitations.add(args[1]);
        State.replaceStateInListUnconfirmed(state, state1);
        sender.sendMessage(Lang.getLang("invitation_sent", args[1]));
        Notifications.notify(args[1], Lang.getLang("invitation_received", state.name, state.name));
    }

}
