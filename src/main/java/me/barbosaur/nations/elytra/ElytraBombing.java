package me.barbosaur.nations.elytra;

import me.barbosaur.nations.Nations;
import me.barbosaur.nations.libs.NBTEditor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class ElytraBombing implements Listener {

    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        if(!e.getAction().equals(Action.RIGHT_CLICK_AIR)){
            return;
        }

        if(!Objects.equals(e.getHand(), EquipmentSlot.HAND)){
            return;
        }

        if(!e.hasItem()){
            return;
        }

        if(!Objects.requireNonNull(e.getItem()).getType().equals(Material.TNT)){
            return;
        }

        if(!e.getPlayer().getInventory().getItemInOffHand().getType().equals(Material.FLINT_AND_STEEL)){
            return;
        }

        e.getPlayer().getInventory().getItemInMainHand().setAmount(e.getPlayer().getInventory().getItemInMainHand().getAmount() - 1);

        World w = e.getPlayer().getWorld();
        Entity tnt = w.spawnEntity(e.getPlayer().getLocation(), EntityType.PRIMED_TNT);
        NBTEditor.set(tnt, 1000, "Fuse");
        new BukkitRunnable(){
            @Override
            public void run(){
                if(tnt.isOnGround()){
                    NBTEditor.set(tnt, 1, "Fuse");
                    cancel();
                    return;
                }

                NBTEditor.set(tnt, 1000, "Fuse");
            }
        }.runTaskTimer(Nations.getPlugin(), 1, 1);

    }

}
