package me.barbosaur.nations.villageAndPillage;

import org.bukkit.World;
import org.bukkit.entity.Villager;
import org.bukkit.generator.structure.Structure;

import java.util.ArrayList;
import java.util.List;

public class Structures {

    public static List<PillagerState> pillagerStates = new ArrayList<>();
    public static List<VillageState> villageStates = new ArrayList<>();

    public static void updateStructures(World world){
        List<Villager> villagers = new ArrayList<>(world.getEntitiesByClass(Villager.class));

        for(Villager villager : villagers){
            world.locateNearestStructure(villager.getLocation(), Structure.PILLAGER_OUTPOST, 1000, true);
        }
    }

}
