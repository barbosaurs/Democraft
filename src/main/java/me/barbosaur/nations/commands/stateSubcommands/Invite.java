package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.Notifications;
import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class Invite implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(args.length == 2){
            State state = State.leaderOfWhatUncornfirmed(p);
            State state1 = state;
            if(state != null){
                if(!State.IsCitizen(args[1])) {
                    if (Nations.discords.containsKey(args[1])) {
                        if (!state.invitations.contains(args[1])) {
                            state1.invitations.add(args[1]);
                            State.replaceStateInListUnconfirmed(state, state1);
                            sender.sendMessage("Вы отправили приглашение игроку " + args[1]);
                            Notifications.notify(args[1], "Вам пришло приглашение в государство " + state.name + ". Пропишите /state acceptinvite " + state.name + ", чтобы присоединиться");
                        } else {
                            sender.sendMessage("Вы уже пригласили этого игрока");
                        }
                    } else {
                        sender.sendMessage("У приглашаемого игрока неподтвержден дискорд");
                    }
                }else{
                    sender.sendMessage("Этот игрок уже является членом государства");
                }
            }else{
                sender.sendMessage("Вы не являетесь лидером неподтвержденного государства");
            }
        }else{
            sender.sendMessage("Неверное количество аргументов!");
        }
    }

}
