package me.barbosaur.nations.commands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.stateSubcommands.*;
import me.barbosaur.nations.commands.stateSubcommands.Help;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.*;

public class StateCommand implements TabExecutor {

    public static HashMap<String, StateSubcommand> subcmds = new HashMap<>();

    public static void addSubcmds(){
        subcmds.put("create", new Create());
        subcmds.put("invite", new Invite());
        subcmds.put("acceptinvite", new AcceptInvite());
        subcmds.put("list", new ListStates());
        subcmds.put("join", new Join());
        subcmds.put("info", new Info());
        subcmds.put("accept", new Accept());
        subcmds.put("deny", new Deny());
        subcmds.put("claim", new Claim());
        subcmds.put("leave", new Leave());
        subcmds.put("color", new Color());
        subcmds.put("reload", new Reload());
        subcmds.put("kick", new Kick());
        subcmds.put("grant", new Grant());
        subcmds.put("degrant", new Degrant());
        subcmds.put("transfer", new Transfer());
        subcmds.put("me", new Me());
        subcmds.put("upgrade", new Upgrade());
        subcmds.put("help", new Help());
        subcmds.put("save", new Save());
        subcmds.put("load", new Load());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player) {
            if (!((Player) sender).getLocation().getWorld().getName().equalsIgnoreCase("world")) {
                sender.sendMessage(Lang.getLang("incorrect_world"));
                return true;
            }
        }

        String subcmd = args[0];
        String p = sender.getName();
        Chunk start = Bukkit.getWorld("world").getChunkAt(0, 0);
        if(sender instanceof Player){
            start = ((Player) sender).getLocation().getChunk();
        }
        StateSubcommand stateSubcommand = (subcmds.get(subcmd));

        if (stateSubcommand == null) {
            sender.sendMessage(Lang.getLang("incorrect_arg"));
            return true;
        }

        stateSubcommand.executeCommand(args, sender, p, subcmd, start);

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if(args.length == 1){
            return new ArrayList<>(StateCommand.subcmds.keySet());
        }

        if (args.length == 2){
            if(args[0].equalsIgnoreCase("join")){
                List<String> strings = new ArrayList<>();
                for(State s : Nations.states){
                    strings.add(s.name);
                }
                return strings;
            }

            if(args[0].equalsIgnoreCase("color")){
                List<String> strings = new ArrayList<>();
                strings.addAll(Nations.colors.keySet());
                return strings;
            }
        }

        return null;
    }



}
