package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.*;
import me.barbosaur.nations.commands.StateSubcommand;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Upgrade implements StateSubcommand {

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){
        Player player = (Player) sender;
        if(State.IsCitizen(p)){
            for(Level level : Events.levels){
                if(getLevel(State.getLevel(p)+1) == null){
                    sender.sendMessage(Lang.getLang("max_level"));
                    return;
                }
            }
            for(Material mat : getLevel(State.getLevel(p)+1).toUpgradeTo.keySet()) {
                if (!player.getInventory().contains(mat, getLevel(State.getLevel(p)+1).toUpgradeTo.get(mat))) {
                    sender.sendMessage(Lang.getLang("not_enough_items"));
                    return;
                }
            }
            for(Material mat : getLevel(State.getLevel(p) + 1).toUpgradeTo.keySet()){
                removeItems(player.getInventory(), mat, getLevel(State.getLevel(p) + 1).toUpgradeTo.get(mat));
            }
            for(State state : Nations.states){
                if(state.players.contains(p)){
                    state.level++;
                    sender.sendMessage(Lang.getLang("state_upgraded", state.level));
                    DynMap.updateMap();
                    return;
                }
            }

        }else{
            sender.sendMessage(Lang.getLang("not_citizen"));
        }
    }

    public static void removeItems(Inventory inventory, Material type, int amount) {
        if (amount <= 0) return;
        int size = inventory.getSize();
        for (int slot = 0; slot < size; slot++) {
            ItemStack is = inventory.getItem(slot);
            if (is == null) continue;
            if (type == is.getType()) {
                int newAmount = is.getAmount() - amount;
                if (newAmount > 0) {
                    is.setAmount(newAmount);
                    break;
                } else {
                    inventory.clear(slot);
                    amount = -newAmount;
                    if (amount == 0) break;
                }
            }
        }
    }

    public static Level getLevel(int i){
        for(Level level : Events.levels){
            if(level.level == i){
                return level;
            }
        }
        return null;
    }

}
