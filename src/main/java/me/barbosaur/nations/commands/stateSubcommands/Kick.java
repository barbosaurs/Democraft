package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.Notifications;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Kick implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(State.IsCitizen(p)){
            if(args.length == 2){
                if(State.getPlayerCountry(p).leaders.contains(p)) {
                    if (State.getPlayerCountry(p).players.contains(args[1])) {
                        if(!State.getPlayerCountry(p).leaders.contains(args[1])){
                            if(State.getPlayerCountry(p).players.size() >= 5) {
                                for (int i = 0; i < Nations.states.size(); i++) {
                                    if (Nations.states.get(i).players.contains(p)) {
                                        Nations.states.get(i).players.remove(args[1]);
                                        Notifications.notify(args[1], "Вы были исключены из государства " + Nations.states.get(i));
                                        sender.sendMessage("Вы исключили игрока " + args[1]);
                                        Nations.stateCooldown.put(args[1], State.stateCooldownIfLeftGive);
                                        break;
                                    }
                                }
                            }else{
                                sender.sendMessage("У вас будет недостаточно игроков");
                            }
                        }else{
                            sender.sendMessage("Вы не можете исключить этого игрока");
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
