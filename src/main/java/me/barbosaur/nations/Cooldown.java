package me.barbosaur.nations;

import me.barbosaur.nations.libs.Notifications;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class Cooldown implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String subcmd = args[0];
        String subcmd2 = args[1];
        String subcmd3 = args[2];
        if(!sender.hasPermission("nations.admin")){
            sender.sendMessage(Lang.getLang("not_enough_perms"));
            return true;
        }

        if(subcmd.equalsIgnoreCase("state")){
            if(!State.CountryExists(args[3])) {
                sender.sendMessage(Lang.getLang("state_doesnt_exist"));
                return true;
            }

            if (subcmd2.equalsIgnoreCase("set")) {
                if (args.length != 5) {
                    sender.sendMessage(Lang.getLang("incorrect_args"));
                    return true;
                }
                if (subcmd3.equalsIgnoreCase("invasion_cooldown")) {
                    for(State state : Nations.states){
                        if(state.name.equals(args[3])){
                            state.invasionCooldown = Integer.parseInt(args[4]);
                            sender.sendMessage(Lang.getLang("invasion_cooldown_set", state.invasionCooldown));
                            break;
                        }
                    }
                } else if (subcmd3.equalsIgnoreCase("chunk_take_cooldown")) {
                    for(State state : Nations.states){
                        if(state.name.equals(args[3])){
                            state.chunkTakeCooldown = Integer.parseInt(args[4]);
                            sender.sendMessage(Lang.getLang("chunk_take_cooldown_set", state.chunkTakeCooldown));
                            break;
                        }
                    }
                }
            } else if (subcmd2.equalsIgnoreCase("get")) {
                if (args.length != 4) {
                    sender.sendMessage(Lang.getLang("incorrect_args"));
                    return true;
                }

                if (subcmd3.equalsIgnoreCase("invasion_cooldown")) {
                    for(State state : Nations.states){
                        if(state.name.equals(args[3])){
                            sender.sendMessage(Lang.getLang("invasion_cooldown_get", state.invasionCooldown));
                            break;
                        }
                    }
                } else if (subcmd3.equalsIgnoreCase("chunk_take_cooldown")) {
                    for(State state : Nations.states){
                        if(state.name.equals(args[3])){
                            sender.sendMessage(Lang.getLang("chunk_take_cooldown_get", state.chunkTakeCooldown));
                            break;
                        }
                    }
                }
            }

        }else if(subcmd.equalsIgnoreCase("player")){
            if(Nations.stateCooldown.containsKey(args[2])) {
                if (subcmd2.equalsIgnoreCase("get")) {
                    if (args.length == 3) {
                        sender.sendMessage(Lang.getLang("state_cooldown_get", Nations.stateCooldown.get(args[2])));
                    }
                } else if (subcmd2.equalsIgnoreCase("set")) {
                    if(args.length == 4){
                        Nations.stateCooldown.put(args[2], Integer.parseInt(args[3]));
                        sender.sendMessage(Lang.getLang("state_cooldown_set", Nations.stateCooldown.get(args[2])));
                    }
                }
            }
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(args.length == 1){
            List<String> strings = new ArrayList<>();
            strings.add("player");
            strings.add("state");
            return strings;
        }else if(args.length == 2){
            List<String> strings = new ArrayList<>();
            strings.add("set");
            strings.add("get");
        }else if(args.length == 3){
            if(args[0].equalsIgnoreCase("player")){
                return new ArrayList<>(Nations.discords.keySet());
            }else if(args[0].equalsIgnoreCase("state")){
                List<String> strings = new ArrayList<>();
                strings.add("invasion_cooldown");
                strings.add("chunk_take_cooldown");
                return strings;
            }
        }else if(args.length == 4){
            if(args[0].equalsIgnoreCase("state")){
                List<String> strings = new ArrayList<>();
                for(State s : Nations.states){
                    strings.add(s.name);
                }
                return strings;
            }
        }

        return null;
    }

    public static void onStart(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(State state : Nations.unconfirmedStates){
                    if(state.deletionCooldown > 0) {
                        state.deletionCooldown -= 1;
                    }else{
                        for(String s : state.players) {
                            if(!State.IsCitizen(s)) {
                                Notifications.notify(s, Lang.getLang("unc_state_deleted"));
                            }
                        }
                        Nations.unconfirmedStates.remove(state);
                    }
                }
                for(String s : Nations.stateCooldown.keySet()) {
                    if (Nations.stateCooldown.get(s) > 0) {
                        Nations.stateCooldown.put(s, Nations.stateCooldown.get(s) - 1);
                    }
                }
                for(int i = 0; i < Nations.states.size(); i++){
                    if(Nations.states.get(i).chunkTakeCooldown > 0){
                        Nations.states.get(i).chunkTakeCooldown -= 1;
                        if(Nations.states.get(i).chunkTakeCooldown <= 0){
                            Nations.states.get(i).canTakeChunks = 5 + (Nations.states.get(i).level + 1) * 3;
                            Nations.states.get(i).notifyAll(Lang.getLang("you_can_take_chunks",
                                    Nations.states.get(i).canTakeChunks));
                        }
                    }
                    if(Nations.states.get(i).invasionCooldown > 0){
                        Nations.states.get(i).invasionCooldown -= 1;
                    }
                }

                for(Block b : TntLaunchers.launcherCooldown.keySet()){
                    if(TntLaunchers.launcherCooldown.get(b) > 0) {
                        TntLaunchers.launcherCooldown.put(b, TntLaunchers.launcherCooldown.get(b) - 1);
                    }
                }

            }
        }.runTaskTimer(Nations.getPlugin(), 20, 20);
    }

}
