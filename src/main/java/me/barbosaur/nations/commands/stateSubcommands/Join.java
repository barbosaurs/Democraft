package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Discord;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.Notifications;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class Join implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(Discord.isVerified(p)) {
            if (args.length == 2) {
                if (!State.IsCitizen(p)) {
                    if (State.CountryExists(args[1])) {
                        if(!State.GetByName(args[1]).requests.contains(p)) {
                            if(Nations.stateCooldown.get(p) <= 0) {
                                for (int i = 0; i < Nations.states.size(); i++) {
                                    State state = Nations.states.get(i);
                                    if (state.name.equalsIgnoreCase(args[1])) {
                                        state.requests.add(p);
                                        for (String s : state.leaders) {
                                            Notifications.notify(s, "Заявка на вступление в " + state.name + " от игрока " + p);
                                        }
                                        sender.sendMessage("Заявка на вступление была отправлена. Ожидайте подтверждения.");
                                        break;
                                    }
                                }
                            }else{
                                sender.sendMessage("Подождите еще " + Nations.stateCooldown.get(p) + " секунд");
                            }
                        }else{
                            sender.sendMessage("Вы уже отправляли заявку в это государство");
                        }
                    } else {
                        sender.sendMessage("Такой страны не существует");
                    }
                } else {
                    sender.sendMessage("Вы уже являетесь гражданином другого государства");
                }
            }
        }else{
            sender.sendMessage("У вас не привязан дискорд. /discord");
        }
    }

}
