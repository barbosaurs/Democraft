package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Leave implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(State.IsCitizen(p)) {
            for (int i = 0; i < Nations.states.size(); i++) {
                if (Nations.states.get(i).leader.equalsIgnoreCase(p)) {
                    Nations.stateCooldown.put(p, State.stateCooldownIfLeftGive);
                    for(String s : Nations.states.get(i).players) {
                        Discord.guild.getMemberByTag(Nations.discords.get(s)).getRoles().remove(Discord.guild.getRoleById(Nations.states.get(i).roleId));
                    }
                    Nations.states.remove(Nations.states.get(i));
                    sender.sendMessage(Lang.getLang("state_deleted"));
                } else if (Nations.states.get(i).players.contains(p)) {
                    Nations.states.get(i).players.remove(p);
                    sender.sendMessage(Lang.getLang("left_state"));
                    Nations.stateCooldown.put(p, State.stateCooldownIfLeftGive);
                    if(Nations.states.get(i).players.size() < State.minimumPlayersForState){
                        for(String s : Nations.states.get(i).players) {
                            Discord.guild.getMemberByTag(Nations.discords.get(s)).getRoles().remove(Discord.guild.getRoleById(Nations.states.get(i).roleId));
                        }
                        Nations.states.remove(i);
                        Nations.states.get(i).notifyAll(Lang.getLang("state_decayed"));
                    }
                }
                DynMap.updateMap();
            }
        }else if(State.IsCitizenUnconfirmed(p)){
            for (int i = 0; i < Nations.unconfirmedStates.size(); i++) {
                if (Nations.unconfirmedStates.get(i).leader.equalsIgnoreCase(p)) {
                    Nations.unconfirmedStates.remove(Nations.unconfirmedStates.get(i));
                    sender.sendMessage(Lang.getLang("unc_state_deleted"));
                    Nations.stateCooldown.put(p, State.stateCooldownGive);
                } else if (Nations.unconfirmedStates.get(i).players.contains(p)) {
                    Nations.unconfirmedStates.get(i).players.remove(p);
                    sender.sendMessage("left_unc_state");
                    Nations.stateCooldown.put(p, State.stateCooldownGive);
                }
            }
        }else{
            sender.sendMessage(Lang.getLang("not_citizen"));
        }
    }

}
