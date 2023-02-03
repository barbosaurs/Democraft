package me.barbosaur.nations;

import me.barbosaur.nations.libs.NBTEditor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class TntLaunchers implements Listener {

    public static HashMap<Block, Vector> launchersAngle = new HashMap<Block, Vector>();
    public static HashMap<Block, Integer> launcherTNTcount = new HashMap<>();
    public static HashMap<Block, Integer> launcherCooldown = new HashMap<>();
    public static List<Entity> tnts = new ArrayList<>();
    public static HashMap<Entity, Player> tntShooters = new HashMap<>();

    @EventHandler
    public void onClick(PlayerInteractEvent e){

        if(e.getClickedBlock() == null){
            return;
        }

        Player p = e.getPlayer();
        if(e.getHand() == null) {
            return;
        }

        if (!e.getHand().equals(EquipmentSlot.HAND)) {
            return;
        }

        if (!validLauncher(e.getClickedBlock())) {
            return;
        }

        if (State.getLevel(p.getDisplayName()) < State.tntLauncherLevel && !p.isOp()) {
            p.sendMessage(Lang.getLang("tnt_launchers_not_opened", State.tntLauncherLevel));
            return;
        }

        if (!Objects.equals(State.getPlayerCountry(p.getDisplayName()), State.getByChunk(e.getClickedBlock().getChunk())) && !p.isOp()) {
            p.sendMessage(Lang.getLang("its_not_your_territory"));
            return;
        }

        if (launchersAngle.containsKey(e.getClickedBlock())) {
            ItemStack item = e.getPlayer().getInventory().getItemInMainHand();
            if (item.getType().equals(Material.AIR)) {
                if (launcherTNTcount.get(e.getClickedBlock()) <= 0) {
                    p.sendMessage(Lang.getLang("no_projectiles"));
                    return;
                }

                if (launcherCooldown.get(e.getClickedBlock()) > 0) {
                    p.sendMessage(Lang.getLang("wait", launcherCooldown.get(e.getClickedBlock())));
                    return;
                }
                p.sendMessage(Lang.getLang("fire"));
                launcherCooldown.put(e.getClickedBlock(), State.tntLauncherCooldownGive);
                World w = e.getClickedBlock().getWorld();
                Entity tnt = w.spawnEntity(e.getClickedBlock().getRelative(0, 2, 0).getLocation(), EntityType.PRIMED_TNT);
                NBTEditor.set(tnt, 1000, "Fuse");
                tnts.add(tnt);
                tntShooters.put(tnt, p);
                tnt.setVelocity(new Vector(launchersAngle.get(e.getClickedBlock()).getX(), launchersAngle.get(e.getClickedBlock()).getY(), launchersAngle.get(e.getClickedBlock()).getZ()));
                launcherTNTcount.put(e.getClickedBlock(), launcherTNTcount.get(e.getClickedBlock()) - 1);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (tnt.isOnGround()) {
                            tnts.remove(tnt);
                            Vector tntLoc = tnt.getLocation().toVector();
                            tntShooters.get(tnt).sendMessage(Lang.getLang("hit", tntLoc.getX(), tntLoc.getY(), tntLoc.getZ()));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    tntShooters.remove(tnt);
                                }
                            }.runTaskLater(Nations.getPlugin(), 20);
                            NBTEditor.set(tnt, 1, "Fuse");
                            cancel();
                        } else {
                            NBTEditor.set(tnt, 1000, "Fuse");
                        }
                    }
                }.runTaskTimer(Nations.getPlugin(), 1, 1);
            } else if (item.getType().equals(Material.TNT)) {
                launcherTNTcount.put(e.getClickedBlock(), launcherTNTcount.get(e.getClickedBlock()) + 1);
                item.setAmount(item.getAmount() - 1);
                p.sendMessage(Lang.getLang("projectile_loaded", launcherTNTcount.get(e.getClickedBlock())));
            }
        } else if (p.getInventory().getItemInMainHand().getType().equals(State.forTntLauncher)
                && (p.getInventory().getItemInMainHand().getAmount() >= State.forTntLauncherAmount)) {
            launchersAngle.put(e.getClickedBlock(), new Vector(1, 1, 1));
            launcherTNTcount.put(e.getClickedBlock(), 0);
            launcherCooldown.put(e.getClickedBlock(), 0);
            p.sendMessage(Lang.getLang("created_launcher"));
            p.getInventory().getItemInMainHand().setAmount(p.getInventory().getItemInMainHand().getAmount() - 32);

        } else {
            p.sendMessage(Lang.getLang("resource_to_create_launcher", State.forTntLauncherAmount, State.forTntLauncher.name().toLowerCase(Locale.ROOT)));
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(launcherTNTcount.containsKey(e.getBlock())){
            launcherTNTcount.remove(e.getBlock());
            launchersAngle.remove(e.getBlock());
        }else if(launcherTNTcount.containsKey(e.getBlock().getRelative(0, -1, 0))){
            launcherTNTcount.remove(e.getBlock().getRelative(0, -1, 0));
            launchersAngle.remove(e.getBlock().getRelative(0, -1, 0));
        }else if(launcherTNTcount.containsKey(e.getBlock().getRelative(0, 1, 0))){
            launcherTNTcount.remove(e.getBlock().getRelative(0, 1, 0));
            launchersAngle.remove(e.getBlock().getRelative(0, 1, 0));
        }
    }

    public static boolean validLauncher(Block clickedBlock){
        if(clickedBlock.getType().equals(Material.DIAMOND_BLOCK)
        && clickedBlock.getRelative(0,-1,0).getType().equals(Material.IRON_BLOCK)
        && clickedBlock.getRelative(0,1,0).getType().equals(Material.IRON_BLOCK)){
            return true;
        }
        return false;
    }

}
