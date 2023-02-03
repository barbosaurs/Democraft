package me.barbosaur.nations;

import dev.dejvokep.boostedyaml.YamlDocument;
import me.barbosaur.nations.commands.*;
import me.barbosaur.nations.elytra.ElytraBombing;
import me.barbosaur.nations.elytra.ElytraKamikadze;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.boss.BossBar;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Nations extends JavaPlugin{

    public static List<State> states = new ArrayList<State>();

    public static List<State> unconfirmedStates = new ArrayList<>();

    public static HashMap<Chunk, Integer> claimingTimer = new HashMap<>();

    public static HashMap<Chunk, State> claimingBy = new HashMap<>();

    public static HashMap<Chunk, BossBar> bossbars = new HashMap<>();

    public static HashMap<String, String> discords = new HashMap<>();

    public static HashMap<String, Integer> colors = new HashMap<>();

    public static HashMap<String, Integer> stateCooldown = new HashMap<>();

    public static HashMap<String, Integer> inactiveTime = new HashMap<>();

    private static Nations plugin;

    public static YamlDocument statesYML, config, discord, player_cooldowns, unconfirmed_states, tnt_launchers, inactive_time, lang, tips_level;

    @Override
    public void onEnable() {
        plugin = this;
        Configs.configsInit();
        StateCommand.addSubcmds();
        addColors();
        commandsEventsInit();
        SaveLoad.loadStates();
        DynMap.dynmapInit();
        InactiveTime.onStart();
        Cooldown.onStart();
        try {
            Discord.jdaInit();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        SaveLoad.saveStates();
        DynMap.deleteMarkers();
    }

    public static Nations getPlugin(){
        return plugin;
    }

    private void commandsEventsInit(){
        getCommand("state").setExecutor(new StateCommand());
        getCommand("cooldown").setExecutor(new Cooldown());
        getCommand("calibrate").setExecutor(new CallibrateCommand());
        getCommand("democraft").setExecutor(new DemocraftCommand());
        getCommand("discord").setExecutor(new DiscordCommand());
        getServer().getPluginManager().registerEvents(new Events(), this);
        getServer().getPluginManager().registerEvents(new TntLaunchers(), this);
        getServer().getPluginManager().registerEvents(new ElytraBombing(), this);
        getServer().getPluginManager().registerEvents(new ElytraKamikadze(), this);
        getServer().getPluginManager().registerEvents(new Help(), this);
        getServer().getPluginManager().registerEvents(new InactiveTime(), this);
    }

    private static void addColors(){
        colors.put("pink", 0xeb34c9);
        colors.put("lime", 0x34ebab);
        colors.put("gold", 0xFFD300);
        colors.put("light_green", 0xe1eb34);
        colors.put("red", 0xc90000);
    }
}
