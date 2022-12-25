package me.barbosaur.nations;

import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import me.barbosaur.nations.commands.CallibrateCommand;
import me.barbosaur.nations.commands.StateCommand;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.MarkerSet;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Nations extends JavaPlugin {

    public static List<State> states = new ArrayList<State>();

    public static List<State> unconfirmedStates = new ArrayList<>();

    public static HashMap<Chunk, Integer> claimingTimer = new HashMap<>();

    public static HashMap<Chunk, State> claimingBy = new HashMap<>();

    public static HashMap<Chunk, BossBar> bossbars = new HashMap<>();

    public static HashMap<String, String> discords = new HashMap<>();

    private static Nations plugin;

    public static YamlDocument statesYML, config, discord, player_cooldowns, unconfirmed_states, tnt_launchers;

    public static DynmapAPI dapi = null;
    public static MarkerSet markerset = null;

    public static HashMap<String, Integer> colors = new HashMap<>();

    public static HashMap<String, Integer> stateCooldown = new HashMap<>();

    @Override
    public void onEnable() {
        StateCommand.addSubcmds();
        plugin = this;
        colors.put("pink", 0xeb34c9);
        colors.put("lime", 0x34ebab);
        colors.put("gold", 0xFFD300);
        colors.put("light_green", 0xe1eb34);
        colors.put("red", 0xc90000);
        // Plugin startup logic
        getCommand("state").setExecutor(new StateCommand());
        getServer().getPluginManager().registerEvents(new TntLaunchers(), this);
        getCommand("calibrate").setExecutor(new CallibrateCommand());
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new BetterExplosions(), this);

        try {
            statesYML = YamlDocument.create(new File(getDataFolder(), "states.yml"), getResource("states.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            discord = YamlDocument.create(new File(getDataFolder(), "discord.yml"), getResource("discord.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            player_cooldowns = YamlDocument.create(new File(getDataFolder(), "player_cooldowns.yml"), getResource("player_cooldowns.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            unconfirmed_states = YamlDocument.create(new File(getDataFolder(), "unconfirmed_states.yml"), getResource("unconfirmed_states.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            tnt_launchers = YamlDocument.create(new File(getDataFolder(), "tnt_launchers.yml"), getResource("tnt_launchers.yml"),
                    GeneralSettings.DEFAULT, LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setVersioning(new BasicVersioning("file-version")).build());
            config.reload();
        }catch (IOException e){

        }
        SaveLoad.loadStates();

        dapi = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        markerset = dapi.getMarkerAPI().createMarkerSet("id", "label/name", dapi.getMarkerAPI().getMarkerIcons(), false);

        State.updateMap();

        BetterExplosions.onStart();
        try {
            getCommand("discord").setExecutor(new Discord());
        } catch (LoginException e) {
            e.printStackTrace();
        }

        State.onStart();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        SaveLoad.saveStates();
        State.deleteMarkers();
    }

    public static Nations getPlugin(){
        return plugin;
    }

}
