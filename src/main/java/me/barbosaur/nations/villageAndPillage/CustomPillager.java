package me.barbosaur.nations.villageAndPillage;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RangedCrossbowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftMagicNumbers;

public class CustomPillager extends Pillager {

    public CustomPillager(Location loc){
        super(EntityType.PILLAGER, ((CraftWorld) loc.getWorld()).getHandle());
        this.setPosRaw(loc.getX(), loc.getY(), loc.getZ());
        this.setHealth(20.0f);
        this.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(CraftMagicNumbers.getItem(Material.CROSSBOW)));
    }

    @Override
    public void registerGoals() { // This method will apply some custom pathfinders to our pig
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 15.0F, 1.0F));
        this.goalSelector.addGoal(3, new RangedCrossbowAttackGoal(this, 1.0D, 8.0F));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal(this, AbstractVillager.class, false));
    }

}
