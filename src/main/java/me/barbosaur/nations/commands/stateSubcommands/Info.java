package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class Info implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(!State.IsOccupied(chunk)){
            sender.sendMessage(Lang.getLang("free_chunk"));
            return;
        }

        for(State state : Nations.states){
            if(!State.ContainsChunk(state.territory, chunk)){
                continue;
            }

            if(Nations.claimingBy.containsKey(chunk)){
                sender.sendMessage(Lang.getLang("controversial_territory", state.name, Nations.claimingBy.get(chunk)));
                break;
            }
            sender.sendMessage(Lang.getLang("occupied_territory", state.name));
            break;
        }
    }

}
