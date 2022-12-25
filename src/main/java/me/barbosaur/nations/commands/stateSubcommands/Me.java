package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.State;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.Locale;

public class Me implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){

        if(State.IsCitizen(p)){

            String string = "";

            State state = State.getPlayerCountry(p);

            string += "Ваше государство: " + state.name + "\n";
            string += "Игроков: " + state.players.size() + "\n";
            string += "Игроки: ";
            for(int i = 0; i < state.players.size(); i++){
                if((i+1)>=state.players.size()){
                    string += state.players.get(i) + "\n";
                }else{
                    string += state.players.get(i) + ", ";
                }
            }
            string += "Чанков территории: " + state.territory.size() + "\n";
            if(state.canTakeChunks > 0){
                string += "Вы можете занять " + state.canTakeChunks + " чанков\n";
            }else{
                string += "Вы сможете занимать чанки через " + state.chunkTakeCooldown + " секунд\n";
            }
            if(state.invasionCooldown > 0){
                string += "Вы сможете завоевывать территорию через " + state.invasionCooldown + " секунд";
            }
            string += "Уровень: " + state.level + "\n";
            if(Upgrade.getLevel(state.level+1) != null) {
                string += "Для прокачки нужно: ";
                for (Material mat : Upgrade.getLevel(state.level + 1).toUpgradeTo.keySet()) {
                    string += "\n" + mat.name().toLowerCase(Locale.ROOT) + ": " + Upgrade.getLevel(state.level + 1).toUpgradeTo.get(mat);
                }
            }else{
                string += "У вас максимальный уровень";
            }

            sender.sendMessage(string);

        }else if(State.IsCitizenUnconfirmed(p)){

            String string = "";

            State state = State.getPlayerCountryUnconfirmed(p);

            string += "Ваше государство: " + state.name + " (неподтверждено) \n";
            string += "Игроков: " + state.players.size() + "\n";
            string += "Чанков территории: " + state.territory.size() + "\n";
            string += "До удаления государства: " + state.deletionCooldown + " секунд \n";
            string += "Пригласите еще " + (5-state.players.size()) + " человек, чтобы подтвердить государство";

        }else{
            sender.sendMessage("Вы не являетесь гражданином государства");
        }

    }

}
