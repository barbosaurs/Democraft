package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class Deny implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(args.length == 2){
            if(State.IsCitizen(p)) {
                for (State state : Nations.states) {
                    if (state.leaders.contains(p)) {
                        if (state.requests.contains(args[1])) {
                            sender.sendMessage("Игрок " + args[1] + " отклонен");
                            state.requests.remove(args[1]);
                        } else {
                            sender.sendMessage("Нет такой заявки");
                        }
                    }
                }
            }else{
                sender.sendMessage("Вы не являетесь гражданином государства");
            }
        }
    }

}
