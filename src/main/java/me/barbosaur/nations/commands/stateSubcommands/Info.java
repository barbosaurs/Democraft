package me.barbosaur.nations.commands.stateSubcommands;

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
            sender.sendMessage("Вы на свободной территории");
        }else{
            for(State state : Nations.states){

                if(State.ContainsChunk(state.territory, chunk)){
                    if(Nations.claimingBy.containsKey(chunk)){
                        sender.sendMessage("Вы на спорной территории государств " + state.name + " и " + Nations.claimingBy.get(chunk).name);
                        break;
                    }
                    sender.sendMessage("Вы на территории государства " + state.name);
                    break;
                }
            }
        }
    }

}
