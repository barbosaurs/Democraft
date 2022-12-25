package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Color implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(State.IsCitizen(p)) {
            if(args.length == 2) {
                if(Nations.colors.containsKey(args[1])) {
                    for (int i = 0; i < Nations.states.size(); i++) {
                        if (Nations.states.get(i).leaders.contains(p)) {
                            Nations.states.get(i).color = Nations.colors.get(args[1]);
                            sender.sendMessage("Цвет государства установлен на " + args[1]);
                            State.updateMap();
                        }
                    }
                }else{
                    String list = "";
                    for(int i = 0; i<Nations.colors.keySet().size(); i++){
                        if((i+1)==Nations.colors.keySet().size()){
                            list = list + " " + Nations.colors.keySet().toArray()[i];
                        }else{
                            list = list + " " + Nations.colors.keySet().toArray()[i] + ",";
                        }

                    }

                    sender.sendMessage("Неправильный цвет. Список цветов:" + list);
                }
            }else{
                sender.sendMessage("Неправильное количество аргументов");
            }
        }else{
            sender.sendMessage("Вы не являетесь гражданином государства");
        }
    }

}
