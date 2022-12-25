package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Events;
import me.barbosaur.nations.Level;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Reload implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        if(sender.hasPermission("nations.reload")) {
            try {
                Nations.config.reload();
            } catch (IOException e) {

            }
            Events.levels = new ArrayList<>();
            if(Nations.config.getSection("levels") != null){
                for(Object s : Nations.config.getSection("levels").getKeys()){

                    String path = "levels." + s;
                    List<Material> mats = new ArrayList<>();
                    List<String> matstring = Nations.config.getStringList(path + ".availablecrafts");
                    HashMap<Material, Integer> toUpgrade = new HashMap<>();
                    for(String s1 : matstring){
                        mats.add(Material.getMaterial(s1));
                    }
                    for(Object s1 : Nations.config.getSection(path + ".toupgrade").getKeys()){
                        int amount = Nations.config.getInt(path+".toupgrade." + s1);
                        toUpgrade.put(Material.getMaterial(s1.toString()), amount);
                    }
                    Level level = new Level(Integer.parseInt((String) s), mats, toUpgrade);
                    Events.levels.add(level);
                }
            }

            if(Nations.discord.getSection("discords") != null){
                for(Object s : Nations.discord.getSection("discords").getKeys()){
                    String discord = Nations.discord.getString("discords." + s);
                    Nations.discords.put((String) s, discord);
                }
            }
        }else{
            sender.sendMessage("У вас недостаточно прав");
        }
    }

}
