package me.barbosaur.nations;

import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.Random;

public class BetterExplosions implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getClickedBlock() != null) {
            World w = e.getClickedBlock().getWorld();

            if (e.getClickedBlock().getType().equals(Material.REDSTONE_BLOCK)) {
                Particle.DustOptions options = new Particle.DustOptions(Color.RED, 1.0f);
                w.spawnParticle(Particle.REDSTONE, e.getClickedBlock().getLocation(), 10, 1, 1, 1, 1, options);
            }else if (e.getClickedBlock().getType().equals(Material.LAPIS_BLOCK)) {
                Particle.DustOptions options = new Particle.DustOptions(Color.BLUE, 1.0f);
                w.spawnParticle(Particle.REDSTONE, e.getClickedBlock().getLocation(), 10, 1, 1, 1, 1, options);
            }
        }

    }

    @EventHandler
    public void onExplosion(ExplosionPrimeEvent e){

        World w = e.getEntity().getWorld();
        if(e.getEntity().isInWater()){
            w.spawnParticle(Particle.WATER_DROP, e.getEntity().getLocation(), 200, 1, 1, 1, 1);
        }else {
            for (int i = 0; i < 50; i++) {
                Vector v1 = e.getEntity().getLocation().toVector();
                Vector v2 = new Vector(randomNumber(-0.4, 0.4), randomNumber(-0.4, 0.3), randomNumber(-0.4, 0.4));
                Vector result = v1.add(v2);
                Entity entity = w.spawnEntity(result.toLocation(w), EntityType.ARROW);
                entity.addScoreboardTag("explosion_particle");
                NBTEditor.set(entity, "minecraft:ui.toast.in", "SoundEvent");
            }
        }

    }

    public static Double randomNumber(Double min, Double max){

        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }


    public static void onStart(){
        new BukkitRunnable() {
            @Override
            public void run() {
                World w = Bukkit.getWorld("world");
                List<Entity> entities = w.getEntities();
                for(Entity entity : entities){
                    if(entity.getScoreboardTags().contains("explosion_particle")){
                        w.spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, entity.getLocation(), 1, 0, 0, 0, 0);
                        if(NBTEditor.getByte(entity, "inGround") != 0){
                            entity.remove();
                        }
                    }
                }
            }
        }.runTaskTimer(Nations.getPlugin(), 1, 1);



    }

}
