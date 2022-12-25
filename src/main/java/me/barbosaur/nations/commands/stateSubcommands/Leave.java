package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateCommand;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class Leave implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(State.IsCitizen(p)) {
            for (int i = 0; i < Nations.states.size(); i++) {
                if (Nations.states.get(i).leader.equalsIgnoreCase(p)) {
                    Nations.stateCooldown.put(p, State.stateCooldownIfLeftGive);
                    Nations.states.remove(Nations.states.get(i));
                    sender.sendMessage("Государство удалено");
                } else if (Nations.states.get(i).players.contains(p)) {
                    Nations.states.get(i).players.remove(p);
                    sender.sendMessage("Вы вышли из государства");
                    Nations.stateCooldown.put(p, State.stateCooldownIfLeftGive);
                    if(Nations.states.get(i).players.size() < 5){
                        Nations.states.remove(i);
                    }
                }
            }
        }else if(State.IsCitizenUnconfirmed(p)){
            for (int i = 0; i < Nations.unconfirmedStates.size(); i++) {
                if (Nations.unconfirmedStates.get(i).leader.equalsIgnoreCase(p)) {
                    Nations.unconfirmedStates.remove(Nations.unconfirmedStates.get(i));
                    sender.sendMessage("Неподтвержденное государство удалено");
                    Nations.stateCooldown.put(p, State.stateCooldownGive);
                } else if (Nations.unconfirmedStates.get(i).players.contains(p)) {
                    Nations.unconfirmedStates.get(i).players.remove(p);
                    sender.sendMessage("Вы вышли из неподтвержденного государства");
                    Nations.stateCooldown.put(p, State.stateCooldownGive);
                }
            }
        }else{
            sender.sendMessage("Вы не являетесь гражданином государства");
        }
    }

}
