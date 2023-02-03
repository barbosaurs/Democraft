package me.barbosaur.nations.elytra;

import me.barbosaur.nations.Lang;
import me.barbosaur.nations.libs.NBTEditor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Random;

public class ElytraKamikadze implements Listener {

    private static HashMap<Player, Integer> kamikadzeTnt = new HashMap<>();

    @EventHandler
    public void onBoom(EntityDamageEvent e){
        if(!(e.getEntity() instanceof Player)){
            return;
        }

        if(!(e.getCause().equals(EntityDamageEvent.DamageCause.FLY_INTO_WALL)
        || e.getCause().equals(EntityDamageEvent.DamageCause.FALL))){
            return;
        }

        Player p = (Player) e.getEntity();

        if(!p.isGliding()){
            return;
        }

        if(!p.getInventory().contains(Material.TNT)){
            return;
        }

        if(e.getDamage() <= 10){
            return;
        }

        kamikadzeTnt.put(p, getAmount(p.getInventory(), Material.TNT));

        p.setHealth(0);

    }

    @EventHandler
    public void deathEvent(PlayerDeathEvent e){
        if(!kamikadzeTnt.containsKey(e.getEntity())){
            return;
        }
        World w = e.getEntity().getWorld();
        e.getDrops().clear();

        if(kamikadzeTnt.get(e.getEntity()) > 10){
            kamikadzeTnt.put(e.getEntity(), 10);
        }

        for(int i = 0; i<kamikadzeTnt.get(e.getEntity()); i++){
            Vector v1 = e.getEntity().getLocation().toVector();
            Vector v2 = new Vector(randomNumber(-2.0, 2.0), randomNumber(-2.0, 2.0), randomNumber(-2.0, 2.0));

            Vector result = v1.add(v2);
            Entity tnt = w.spawnEntity(result.toLocation(w), EntityType.PRIMED_TNT);
            NBTEditor.set(tnt, 1, "Fuse");
        }

        e.setDeathMessage(Lang.getLang("death_message_1", e.getEntity().getDisplayName()));
        kamikadzeTnt.remove(e.getEntity());
    }

    public static Double randomNumber(Double min, Double max){

        Random r = new Random();
        return min + (max - min) * r.nextDouble();
    }

    private static int getAmount(PlayerInventory inventory, Material material){

        int amount = 0;

        for(ItemStack stack : inventory.getContents()){
            if(stack == null){
                continue;
            }

            if(stack.getType().equals(material)){
                amount += stack.getAmount();
            }
        }

        return amount;
    }

}
