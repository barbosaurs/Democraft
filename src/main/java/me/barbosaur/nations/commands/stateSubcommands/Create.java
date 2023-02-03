package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Discord;
import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateCommand;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Create implements StateSubcommand {

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

        if (State.IsOccupied(chunk)) {
            sender.sendMessage(Lang.getLang("chunk_occupied"));
            return;
        }

        String name = args[1];
        if (State.CountryExists(name)) {
            sender.sendMessage(Lang.getLang("state_exists"));
            return;
        }

        if(Nations.stateCooldown.get(p) > 0) {
            sender.sendMessage(Lang.getLang("wait", Nations.stateCooldown.get(p)));
            return;
        }

        State newState = new State(name, p);
        newState.territory.add(chunk);
        newState.deletionCooldown = State.deletionCooldownGive;
        sender.sendMessage(Lang.getLang("state_registered", name, State.minimumPlayersForState - 1));

        if(newState.players.size() >= State.minimumPlayersForState){
            newState.deletionCooldown = 0;
            newState.chunkTakeCooldown = 10;
            Nations.states.add(newState);
            newState.notifyAll(Lang.getLang("state_confirmed"));
        }else{
            Nations.unconfirmedStates.add(newState);
        }

        for (State state : Nations.unconfirmedStates) {
            if(state.name.equals(newState.name)) {
               continue;
            }

            if (!State.ContainsChunk(state.territory, chunk)) {
                continue;
            }

            Nations.stateCooldown.put(state.leader, State.stateCooldownGive);
            Nations.unconfirmedStates.remove(state);
            state.notifyAll(Lang.getLang("unc_state_occupied"));
            break;
        }
    }

}
