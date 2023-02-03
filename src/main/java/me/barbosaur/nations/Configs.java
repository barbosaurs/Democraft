package me.barbosaur.nations;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Configs {

    public static void configsInit(){

        try {
            Nations.statesYML = createConfig("states");
            Nations.config = createConfig("config");
            Nations.discord = createConfig("discord");
            Nations.player_cooldowns = createConfig("player_cooldowns");
            Nations.unconfirmed_states = createConfig("unconfirmed_states");
            Nations.tnt_launchers = createConfig("tnt_launchers");
            Nations.inactive_time = createConfig("inactive_time");
            Nations.lang = createConfig("lang");
            Nations.tips_level = createConfig("tips_level");
            Nations.config.reload();
            Nations.lang.reload();
        }catch (IOException ignored){

        }
    }

    private static YamlDocument createConfig(String fileName){
        File dataFolder = Nations.getPlugin().getDataFolder();
        UpdaterSettings updaterSettings = UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build();
        LoaderSettings loaderSettings = LoaderSettings.builder().setAutoUpdate(true).build();
        try {
            return YamlDocument.create(new File(dataFolder, fileName + ".yml"), getResource(fileName + ".yml"),
                    GeneralSettings.DEFAULT, loaderSettings, DumperSettings.DEFAULT, updaterSettings);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static InputStream getResource(String s){
        return Nations.getPlugin().getResource(s);
    }

    public static void reload(){
        try {
            Nations.config.reload();
            Nations.lang.reload();
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

        State.chunkTakeCooldownGive = Nations.config.getInt("chunk_take_cooldown_give");
        State.deletionCooldownGive = Nations.config.getInt("deletion_cooldown_give");
        State.invasionCooldownGive = Nations.config.getInt("invasion_cooldown_give");
        State.stateCooldownGive = Nations.config.getInt("state_cooldown_give");
        State.stateCooldownIfLeftGive = Nations.config.getInt("state_cooldown_if_left_give");
        State.minimumPlayersForState = Nations.config.getInt("min_players_for_state");
        State.netherLevel = Nations.config.getInt("nether_level");
        State.endLevel = Nations.config.getInt("end_level");
        State.tntLauncherLevel = Nations.config.getInt("tnt_launcher_level");
        State.claimTime = Nations.config.getInt("claim_time");
        State.minCaptureHeight = Nations.config.getInt("min_capture_height");
        State.minCapturePercentage = Nations.config.getInt("min_capture_percentage");
        State.forTntLauncher = Material.getMaterial(Nations.config.getString("for_tnt_launcher"));
        State.forTntLauncherAmount = Nations.config.getInt("for_tnt_launcher_amount");
        State.tntLauncherCooldownGive = Nations.config.getInt("tnt_launcher_cooldown_give");
    }

}
