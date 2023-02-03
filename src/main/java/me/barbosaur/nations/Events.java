package me.barbosaur.nations;

import me.barbosaur.nations.libs.Notifications;
import me.barbosaur.nations.villageAndPillage.Pillagers;
import org.bukkit.*;
import org.bukkit.block.data.type.Bed;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.generator.structure.Structure;
import org.bukkit.util.StructureSearchResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Events implements Listener {

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        if(e.getBlock().getType().equals(Material.DIAMOND_BLOCK)){
            e.getPlayer().sendMessage("АЛМАЗНЫЙ БЛОК!");
        }
    }




    public static List<Level> levels = new ArrayList<>();

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if(!(e.getBlock().getBlockData() instanceof Bed)) {
            if (!State.canBreak(e.getPlayer().getDisplayName(), e.getBlock().getChunk())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTrade(InventoryClickEvent e){
        if(e.getInventory().getType().equals(InventoryType.MERCHANT)){
            Material t = e.getCurrentItem().getType();
            if(t.equals(Material.DIAMOND_HELMET) || t.equals(Material.ENCHANTED_BOOK)
                    || t.equals(Material.DIAMOND_CHESTPLATE)
                    || t.equals(Material.DIAMOND_LEGGINGS) || t.equals(Material.DIAMOND_BOOTS)){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(!State.canBreak(e.getPlayer().getDisplayName(), e.getBlock().getChunk())){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent e){
        if(State.IsOccupied(e.getEntity().getLocation().getChunk())){
            if(TntLaunchers.tntShooters.containsKey(e.getEntity())) {
                String player = TntLaunchers.tntShooters.get(e.getEntity()).getDisplayName();
                if (!State.IsCitizen(player)) {
                    LivingEntity entity = (LivingEntity) e.getEntity();
                    entity.setHealth(0.0);
                    e.setCancelled(true);
                }else{
                    Location loc = e.getEntity().getLocation();
                    State.getByChunk(e.getEntity().getLocation().getChunk()).notifyAll("Вас бомбят! (" + loc.getX() + ", "
                            + loc.getY() + ", " + loc.getZ() + ")");
                }
            }else{
                LivingEntity entity = (LivingEntity) e.getEntity();
                entity.setHealth(0.0);
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if((e.getDamager() instanceof Player) && (e.getEntity() instanceof Player)){
            Player player = (Player) e.getDamager();
            if(!Discord.isVerified(player.getDisplayName())){
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(State.IsCitizen(e.getPlayer().getDisplayName())){
            for(State state : Nations.states){
                if(state.leader.equalsIgnoreCase(e.getPlayer().getDisplayName())){
                    for(String name : state.requests){
                        e.getPlayer().sendMessage("Заявка на вступление от " + name);
                    }
                }
            }
        }
        if(Notifications.notifications.containsKey(e.getPlayer().getDisplayName())){
            e.getPlayer().sendMessage(Notifications.notifications.get(e.getPlayer().getDisplayName()));
            Notifications.notifications.remove(e.getPlayer().getDisplayName());
        }
        if(!Nations.stateCooldown.containsKey(e.getPlayer().getDisplayName())){
            Nations.stateCooldown.put(e.getPlayer().getDisplayName(), 0);
        }

    }

    private static HashMap<Player, Chunk> playersInClaiming = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent e){
        Chunk chunk = e.getPlayer().getLocation().getChunk();
        if(Nations.bossbars.containsKey(chunk)){
            Nations.bossbars.get(chunk).addPlayer(e.getPlayer());
            playersInClaiming.put(e.getPlayer(), chunk);
        }else if (playersInClaiming.containsKey(e.getPlayer())){
            Nations.bossbars.get(playersInClaiming.get(e.getPlayer())).removePlayer(e.getPlayer());
            playersInClaiming.remove(e.getPlayer());
        }
    }

    @EventHandler
    public void onGet(CraftItemEvent e){
        if(cantUseItem(e.getWhoClicked().getName(), e.getRecipe().getResult().getType())){
            e.setCancelled(true);
            e.getWhoClicked().sendMessage("У вас не хватает уровня, чтобы скрафтить это");
        }
    }

    @EventHandler
    public void onChunk(ChunkLoadEvent e){

        World w = e.getChunk().getWorld();
        StructureSearchResult searchResult = w.locateNearestStructure(e.getChunk().getBlock(8, 60, 8).getLocation(), Structure.PILLAGER_OUTPOST, 8, false);
        if(searchResult == null) {
            return;
        }

        if(State.ContainsChunk(Pillagers.pillagerTerritory, e.getChunk())){
            return;
        }

        Pillagers.pillagerTerritory.add(e.getChunk());
    }

    @EventHandler
    public void onPortal(PlayerTeleportEvent e){

        if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) && State.getLevel(e.getPlayer().getDisplayName()) < State.netherLevel){
            e.setCancelled(true);
            e.getPlayer().sendMessage("Ад доступен с " + State.netherLevel + " уровня развития государства");
        }else if(e.getCause().equals(PlayerTeleportEvent.TeleportCause.END_PORTAL) && State.getLevel(e.getPlayer().getDisplayName()) < State.endLevel){
            e.setCancelled(true);
            e.getPlayer().sendMessage("Энд доступен с " + State.endLevel + " уровня государства");
        }

    }

    public static boolean cantUseItem(String player, Material item){

        int level = State.getLevel(player);
        List<Material> cantUse = new ArrayList<>();

        for(Level level1 : levels){

            if(level1.level>level){
                cantUse.addAll(level1.availableCrafts);
            }

        }

        return cantUse.contains(item);
    }


}
