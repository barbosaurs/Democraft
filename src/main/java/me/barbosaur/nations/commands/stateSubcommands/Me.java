package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class Me implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){

        if(State.IsCitizen(p)){

            String string = "";

            State state = State.getPlayerCountry(p);

            string += Lang.getLang("your_state") + ": " + state.name + "\n";
            string += Lang.getLang("player_amount") + ": " + state.players.size() + "\n";
            string += Lang.getLang("players") + ": ";
            for(int i = 0; i < state.players.size(); i++){
                if((i+1)>=state.players.size()){
                    string += state.players.get(i) + "\n";
                }else{
                    string += state.players.get(i) + ", ";
                }
            }
            string += Lang.getLang("chunk_amount") + ": " + state.territory.size() + "\n";
            if(state.canTakeChunks > 0){
                string += Lang.getLang("you_can_take_chunks", state.canTakeChunks) + "\n";
            }else{
                string += Lang.getLang("able_to_take_chunks", state.chunkTakeCooldown) + "\n";
            }
            if(state.invasionCooldown > 0){
                string += Lang.getLang("able_to_capture", state.invasionCooldown) + "\n";
            }
            string += Lang.getLang("level") + ": " + state.level + "\n";
            if(Upgrade.getLevel(state.level+1) != null) {
                string += Lang.getLang("for_upgrade_needed") + ": ";
                for (Material mat : Upgrade.getLevel(state.level + 1).toUpgradeTo.keySet()) {
                    string += "\n" + mat.name().toLowerCase(Locale.ROOT) + ": " + Upgrade.getLevel(state.level + 1).toUpgradeTo.get(mat);
                }
            }else{
                string += Lang.getLang("max_level");
            }

            sender.sendMessage(string);

        }else if(State.IsCitizenUnconfirmed(p)){

            String string = "";

            State state = State.getPlayerCountryUnconfirmed(p);

            string += Lang.getLang("your_state") + ": " + state.name + " " + Lang.getLang("unconfirmed") + "\n";
            string += Lang.getLang("player_amount") + ": " + state.players.size() + "\n";
            string += Lang.getLang("chunk_amount") + ": " + state.territory.size() + "\n";
            string += Lang.getLang("before_deletion", state.deletionCooldown) + "\n";
            string += Lang.getLang("invite_more", (State.minimumPlayersForState-state.players.size()));
            sender.sendMessage(string);
        }else{
            sender.sendMessage(Lang.getLang("not_citizen"));
        }

    }

}
