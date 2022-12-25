package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Transfer implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(State.IsCitizen(p)) {
            if (args.length == 2) {
                if (State.getPlayerCountry(p).leader.equals(p)) {
                    if (State.getPlayerCountry(p).players.contains(args[1])) {
                        if (State.getPlayerCountry(p).leaders.contains(args[1])) {
                            for(State state : Nations.states){
                                if(state.leader.equals(p)){
                                    state.leader = args[1];
                                    sender.sendMessage("Вы передали свое государство игроку " + args[1]);
                                }
                            }
                        }else{
                            sender.sendMessage("Этот игрок не является соправителем");
                        }
                    }else{
                        sender.sendMessage("Такого игрока нет в вашем государстве");
                    }
                }else{
                    sender.sendMessage("У вас недостаточно прав");
                }
            }else{
                sender.sendMessage("Неверное количество аргументов");
            }
        }else{
            sender.sendMessage("Вы не являетесь гражданином государства");
        }
    }

}
