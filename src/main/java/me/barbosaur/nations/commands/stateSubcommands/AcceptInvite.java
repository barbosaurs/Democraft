package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Discord;
import me.barbosaur.nations.Events;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class AcceptInvite implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(Nations.stateCooldown.get(p) <= 0) {
            if (args.length == 2) {
                if (!State.IsCitizen(p)) {
                    if (Discord.isVerified(p)) {
                        State uncState = null;
                        for(State state : Nations.unconfirmedStates){
                            if(state.name.equals(args[1])){
                                uncState = state;
                                break;
                            }
                        }
                        if((uncState != null) && uncState.invitations.contains(p)){
                            Nations.unconfirmedStates.remove(uncState);
                            uncState.invitations.remove(p);
                            uncState.players.add(p);
                            sender.sendMessage("Вы вошли в неподтвержденное государство");
                            if(uncState.players.size() >= 5){
                                if(!State.CountryExists(uncState.name)) {
                                    Nations.states.add(uncState);
                                    uncState.notifyAll("Теперь ваше государство подтверждено");
                                    uncState.chunkTakeCooldown = 60;
                                }else{
                                    sender.sendMessage("Произошла ошибочка. Государство теперь подтверждено, попробуйте /state join " + uncState.name);
                                }
                                State.updateMap();
                            }else{
                                Nations.unconfirmedStates.add(uncState);
                            }
                        }else{
                            sender.sendMessage("Нет такого приглашения");
                        }
                    } else {
                        sender.sendMessage("У вас не привязан дискорд. /discord link <ник#0000>");
                    }
                } else {
                    sender.sendMessage("Вы уже являетесь гражданином другого государства");
                }
            } else {
                sender.sendMessage("Неверное количество аргументов");
            }
        }else{
            sender.sendMessage("Подождите еще " + Nations.stateCooldown.get(p) + " секунд");
        }
    }

}
