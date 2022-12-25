package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Discord;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateCommand;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

public class Create implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(Discord.isVerified(p)) {
            if (args.length == 2) {
                String name = args[1];
                if (!State.IsCitizen(p)) {
                    if (!State.IsOccupied(chunk)) {
                        if (!State.CountryExists(name)) {
                            if(Nations.stateCooldown.get(p) <= 0) {
                                State newState = new State(name, p);
                                newState.territory.add(chunk);
                                newState.deletionCooldown = State.deletionCooldownGive;
                                sender.sendMessage("Государство с именем " + name + " зарегистрировано и ожидает подтверждения. Для этого пригласите сюда 5 игроков комендой /state invite <игрок> в течение 1 часа");
                                Nations.unconfirmedStates.add(newState);
                                for (State state : Nations.unconfirmedStates) {
                                    if(!state.name.equals(newState.name)) {
                                        if (State.ContainsChunk(state.territory, chunk)) {
                                            Nations.stateCooldown.put(state.leader, State.stateCooldownGive);
                                            Nations.unconfirmedStates.remove(state);
                                            state.notifyAll("Территория вашего неподтвержденного государства была занята");
                                            break;
                                        }
                                    }
                                }
                            }else{
                                sender.sendMessage("Вы не можете создавать государство еще " + Nations.stateCooldown.get(p) + " секунд");
                            }
                        } else {
                            sender.sendMessage("Такая страна уже существует");
                        }
                    } else {
                        sender.sendMessage("Эта территория занята");
                    }
                } else {
                    sender.sendMessage("Вы уже являетесь гражданином другого государства");
                }
            } else {
                sender.sendMessage("Неверное количество аргументов!");
            }
        }else{
            sender.sendMessage("У вас не привязан дискорд. /discord link <ник#0000>");
        }
    }

}
