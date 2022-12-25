package me.barbosaur.nations;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;

public class Level {

    public int level;
    public List<Material> availableCrafts;
    public HashMap<Material, Integer> toUpgradeTo;

    public Level(int level, List<Material> availableCrafts, HashMap<Material, Integer> toUpgradeTo){
        this.level = level;
        this.availableCrafts = availableCrafts;
        this.toUpgradeTo = toUpgradeTo;
    }

}
