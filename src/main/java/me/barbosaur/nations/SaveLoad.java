package me.barbosaur.nations;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SaveLoad {

    public static void saveStates(){

        Nations.statesYML.getFile().delete();
        Nations.unconfirmed_states.getFile().delete();
        Nations.tnt_launchers.getFile().delete();
        YamlDocument unconfirmed_states = null;
        YamlDocument statesYML = null;
        YamlDocument tnt_launchers = null;

        try {
            statesYML = YamlDocument.create(new File(Nations.getPlugin().getDataFolder(),
                            "states.yml"), Nations.getPlugin().getResource("states.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            unconfirmed_states = YamlDocument.create(new File(Nations.getPlugin().getDataFolder(),
                            "unconfirmed_states.yml"), Nations.getPlugin().getResource("unconfirmed_states.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            tnt_launchers = YamlDocument.create(new File(Nations.getPlugin().getDataFolder(),
                            "tnt_launchers.yml"), Nations.getPlugin().getResource("tnt_launchers.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT,
                    UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
        }catch (IOException e){

        }

        for(State state : Nations.states){
            String path = "states."+state.name;
            statesYML.set(path + ".leader", state.leader);
            statesYML.set(path+".players", state.players);
            statesYML.set(path+".level", state.level);
            statesYML.set(path+".requests", state.requests);
            statesYML.set(path+".color", state.color);
            statesYML.set(path+".invitations", state.invitations);
            statesYML.set(path+".leaders", state.leaders);
            statesYML.set(path+".cantakechunks", state.canTakeChunks);
            statesYML.set(path+".invasioncooldown", state.invasionCooldown);
            statesYML.set(path+".chunktakecooldown", state.chunkTakeCooldown);
            for(int i = 0; i<state.territory.size(); i++){
                statesYML.set(path+".territory."+String.valueOf(i)+".x", state.territory.get(i).getX());
                statesYML.set(path+".territory."+String.valueOf(i)+".z", state.territory.get(i).getZ());
            }
            System.out.println(state.name + " saved");
        }

        int iL = 0;

        for(Block block : TntLaunchers.launcherTNTcount.keySet()){
            String path = "states." + iL;

            tnt_launchers.set(path + ".x", block.getX());
            tnt_launchers.set(path + ".y", block.getY());
            tnt_launchers.set(path + ".z", block.getZ());
            tnt_launchers.set(path + ".tntcount", TntLaunchers.launcherTNTcount.get(block));
            tnt_launchers.set(path + ".angle.x", TntLaunchers.launchersAngle.get(block).getX());
            tnt_launchers.set(path + ".angle.y", TntLaunchers.launchersAngle.get(block).getY());
            tnt_launchers.set(path + ".angle.z", TntLaunchers.launchersAngle.get(block).getZ());
            tnt_launchers.set(path + ".world", block.getWorld().getName());

            iL++;
        }

        for(State state : Nations.unconfirmedStates){
            String path = "states."+state.name;
            unconfirmed_states.set(path + ".leader", state.leader);
            unconfirmed_states.set(path+".players", state.players);
            unconfirmed_states.set(path+".level", state.level);
            unconfirmed_states.set(path+".requests", state.requests);
            unconfirmed_states.set(path+".color", state.color);
            unconfirmed_states.set(path+".invitations", state.invitations);
            unconfirmed_states.set(path+".leaders", state.leaders);
            unconfirmed_states.set(path+".deletioncooldown", state.deletionCooldown);
            unconfirmed_states.set(path+".cantakechunks", state.canTakeChunks);
            unconfirmed_states.set(path+".invasioncooldown", state.invasionCooldown);
            unconfirmed_states.set(path+".chunktakecooldown", state.chunkTakeCooldown);
            for(int i = 0; i<state.territory.size(); i++){
                unconfirmed_states.set(path+".territory."+String.valueOf(i)+".x", state.territory.get(i).getX());
                unconfirmed_states.set(path+".territory."+String.valueOf(i)+".z", state.territory.get(i).getZ());
            }
            System.out.println(state.name + " saved (unconfirmed)");
        }

        for(String s : Nations.discords.keySet()){
            String path = "discords." + s;
            Nations.discord.set(path, Nations.discords.get(s));
        }

        for(String s : Nations.stateCooldown.keySet()){
            String path = "statecooldown." + s;
            Nations.player_cooldowns.set(path, Nations.stateCooldown.get(s));
        }

        try {
            statesYML.save();
            unconfirmed_states.save();
            tnt_launchers.save();
            Nations.discord.save();
            Nations.player_cooldowns.save();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static void loadStates(){
        try{
            Nations.statesYML.reload();
            Nations.unconfirmed_states.reload();
            Nations.tnt_launchers.reload();
            Nations.player_cooldowns.reload();
            Nations.config.reload();
            Nations.discord.reload();
        }catch(IOException e){

        }
        if(Nations.statesYML.getSection("states") != null) {
            for (Object s : Nations.statesYML.getSection("states").getKeys()) {
                String path = "states." + s;
                String leader = Nations.statesYML.getString(path + ".leader");
                State state = new State((String) s, leader);
                for (Object chunk : Nations.statesYML.getSection(path + ".territory").getKeys()) {
                    String pathChunk = path + ".territory." + (String)chunk;
                    World world = Bukkit.getWorld("world");
                    assert world != null;
                    Chunk chunk1 = world.getChunkAt(Nations.statesYML.getInt(pathChunk + ".x"),
                            Nations.statesYML.getInt(pathChunk + ".z"));
                    state.territory.add(chunk1);
                }
                state.players = Nations.statesYML.getStringList(path + ".players");;
                state.level = Nations.statesYML.getInt(path+".level");;
                state.color = Nations.statesYML.getInt(path+".color");;
                state.requests = Nations.statesYML.getStringList(path+".requests");
                state.invitations = Nations.statesYML.getStringList(path+".invitations");
                state.leaders = Nations.statesYML.getStringList(path+".leaders");
                state.canTakeChunks = Nations.statesYML.getInt(path+".cantakechunks");
                state.invasionCooldown = Nations.statesYML.getInt(path + ".invasioncooldown");
                state.chunkTakeCooldown = Nations.statesYML.getInt(path+".chunktakecooldown");
                state.deletionCooldown = 0;
                Nations.states.add(state);
                System.out.println(state.name + "loaded");
            }
        }

        if(Nations.tnt_launchers.getSection("launchers") != null){
            for(Object s : Nations.tnt_launchers.getSection("launchers").getKeys()){

                String path = "launchers." + s;
                World w = Bukkit.getWorld(Nations.tnt_launchers.getString(path+".world"));
                int x = Nations.tnt_launchers.getInt(path + ".x");
                int y = Nations.tnt_launchers.getInt(path + ".y");
                int z = Nations.tnt_launchers.getInt(path + ".z");
                int tntCount = Nations.tnt_launchers.getInt(path + ".tntcount");
                int xA = Nations.tnt_launchers.getInt(path + ".angle.x");
                int yA = Nations.tnt_launchers.getInt(path + ".angle.y");
                int zA = Nations.tnt_launchers.getInt(path + ".angle.z");
                TntLaunchers.launcherTNTcount.put(w.getBlockAt(x, y, z), tntCount);
                TntLaunchers.launchersAngle.put(w.getBlockAt(x, y, z), new Vector(xA, yA, zA));

            }
        }

        if(Nations.unconfirmed_states.getSection("states") != null) {
            for (Object s : Nations.unconfirmed_states.getSection("states").getKeys()) {
                String path = "states." + s;
                String leader = Nations.unconfirmed_states.getString(path + ".leader");
                State state = new State((String) s, leader);
                for (Object chunk : Nations.unconfirmed_states.getSection(path + ".territory").getKeys()) {
                    String pathChunk = path + ".territory." + (String)chunk;
                    World world = Bukkit.getWorld("world");
                    assert world != null;
                    Chunk chunk1 = world.getChunkAt(Nations.unconfirmed_states.getInt(pathChunk + ".x"),
                            Nations.unconfirmed_states.getInt(pathChunk + ".z"));
                    state.territory.add(chunk1);
                }
                state.players = Nations.unconfirmed_states.getStringList(path + ".players");;
                state.level = Nations.unconfirmed_states.getInt(path+".level");;
                state.color = Nations.unconfirmed_states.getInt(path+".color");;
                state.requests = Nations.unconfirmed_states.getStringList(path+".requests");
                state.invitations = Nations.unconfirmed_states.getStringList(path+".invitations");
                state.leaders = Nations.unconfirmed_states.getStringList(path+".leaders");
                state.canTakeChunks = Nations.unconfirmed_states.getInt(path+".cantakechunks");
                state.invasionCooldown = Nations.unconfirmed_states.getInt(path + ".invasioncooldown");
                state.chunkTakeCooldown = Nations.unconfirmed_states.getInt(path+".chunktakecooldown");
                state.deletionCooldown = Nations.unconfirmed_states.getInt(path+".deletioncooldown");
                Nations.states.add(state);
                System.out.println(state.name + "loaded");
            }
        }

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

        if(Nations.player_cooldowns.getSection("statecooldown") != null){
            for(Object s : Nations.player_cooldowns.getSection("statecooldown").getKeys()){
                int cooldown = Nations.player_cooldowns.getInt((String)s);
                Nations.stateCooldown.put((String) s, cooldown);
            }
        }
    }

}
