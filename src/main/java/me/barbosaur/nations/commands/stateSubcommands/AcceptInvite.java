package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import java.awt.Color;
import java.util.ArrayList;

public class AcceptInvite implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(Nations.stateCooldown.get(p) > 0) {
            sender.sendMessage(Lang.getLang("wait", Nations.stateCooldown.get(p)));
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

        if (!Discord.isVerified(p)) {
            sender.sendMessage(Lang.getLang("not_verified"));
            return;
        }

        State uncState = null;
        for(State state : Nations.unconfirmedStates){
            if(state.name.equals(args[1])){
                uncState = state;
                break;
            }
        }

        if((uncState == null) || !uncState.invitations.contains(p)){
            sender.sendMessage(Lang.getLang("no_such_invitation"));
            return;
        }

        Nations.unconfirmedStates.remove(uncState);
        uncState.invitations.remove(p);
        uncState.players.add(p);
        sender.sendMessage(Lang.getLang("you_entered_unc_state"));

        if(uncState.players.size() < State.minimumPlayersForState){
            Nations.unconfirmedStates.add(uncState);
            return;
        }

        if(State.CountryExists(uncState.name)) {
            sender.sendMessage(Lang.getLang("error_confirmed", uncState.name));
            return;
        }

        uncState.invitations = new ArrayList<>();
        uncState.notifyAll(Lang.getLang("state_confirmed"));
        uncState.chunkTakeCooldown = 60;
        Role role = Discord.guild.createRole().setName(uncState.name).setColor(uncState.color).setHoisted(true).complete();
        Discord.guild.modifyRolePositions().selectPosition(role).moveTo(5);
        uncState.roleId = role.getIdLong();
        Nations.states.add(uncState);
        for(String s : uncState.players){
            Member member = Discord.guild.getMemberByTag(Nations.discords.get(s));
            assert member != null;
            member.getRoles().add(role);
        }
        DynMap.updateMap();

    }

}
