package me.barbosaur.nations;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Tag;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Help implements Listener {

    public static HashMap<String, Integer> tipsLevel = new HashMap<>();

    @EventHandler
    public void onItemGet(EntityPickupItemEvent e){

        if(!(e.getEntity() instanceof Player)) {
            return;
        }

        Player p = (Player) e.getEntity();

        if(!tipsLevel.containsKey(p.getDisplayName())){
            tipsLevel.put(p.getDisplayName(), 1);
        }

        Material material = e.getItem().getItemStack().getType();

        tip(material, p);

    }

    @EventHandler
    public void inventoryClickEvent(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        if(e.getCurrentItem() == null){
            return;
        }

        Player p = (Player) e.getWhoClicked();

        if(!tipsLevel.containsKey(p.getDisplayName())){
            tipsLevel.put(p.getDisplayName(), 1);
        }

        Material material = e.getCurrentItem().getType();

        tip(material, p);
    }

    private static void tip(Material material, Player p){
        if (levelTag(material, 1, Tag.LOGS, p)
                || levelTag(material, 1, Tag.PLANKS, p)){
            soundMsg(p, Lang.getLang("tips.tip_1"));
            tipsLevel.put(p.getDisplayName(), 2);

        }else if(material.equals(Material.COBBLESTONE)
                && tipsLevel.get(p.getDisplayName()) == 2){
            soundMsg(p, Lang.getLang("tips.tip_2"));
            tipsLevel.put(p.getDisplayName(), 3);
            new BukkitRunnable(){
                @Override
                public void run(){
                    soundMsg(p, Lang.getLang("tips.tip_3"));
                    tipsLevel.put(p.getDisplayName(), 4);
                }
            }.runTaskLater(Nations.getPlugin(), 100);

        }
    }

    private static boolean levelTag(Material material, int level, Tag<Material> tag, Player p){
        return tag.isTagged(material)
                && tipsLevel.get(p.getDisplayName()) == level;
    }

    public static void soundMsg(Player p, String msg){
        p.sendMessage(msg);
        p.playSound(p, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
    }

}
