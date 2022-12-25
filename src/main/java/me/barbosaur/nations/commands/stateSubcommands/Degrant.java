package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Degrant implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(State.IsCitizen(p)) {
            if (args.length == 2) {
                if (State.getPlayerCountry(p).leader.equals(p)) {
                    if (State.getPlayerCountry(p).players.contains(args[1])) {
                        if (State.getPlayerCountry(p).leaders.contains(args[1])) {
                            for(State state : Nations.states){
                                if(state.leader.equals(p)){
                                    state.leaders.remove(args[1]);
                                    sender.sendMessage(args[1] + " был снят с поста соправителя государства");
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
