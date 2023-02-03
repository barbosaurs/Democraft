package me.barbosaur.nations;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.dynmap.DynmapAPI;
import org.dynmap.markers.AreaMarker;
import org.dynmap.markers.MarkerSet;

import java.util.ArrayList;
import java.util.List;

public class DynMap {

    public static DynmapAPI dapi = null;
    public static MarkerSet markerset = null;

    public static List<AreaMarker> markers = new ArrayList<>();

    public static void dynmapInit(){
        dapi = (DynmapAPI) Bukkit.getServer().getPluginManager().getPlugin("dynmap");
        markerset = dapi.getMarkerAPI().createMarkerSet("id", "label/name", dapi.getMarkerAPI().getMarkerIcons(), false);
        updateMap();
    }

    public static void deleteMarkers(){
        for(AreaMarker marker1 : markers){
            marker1.deleteMarker();
        }
    }

    public static void updateMap(){
        for(AreaMarker marker1 : markers){
            marker1.deleteMarker();
        }
        for(int i = 0; i<Nations.states.size(); i++){
            for(int u = 0; u<Nations.states.get(i).territory.size(); u++){

                int xMin = Nations.states.get(i).territory.get(u).getX() * 16;
                int zMin = Nations.states.get(i).territory.get(u).getZ() * 16;

                AreaMarker marker = markerset.createAreaMarker(u+","+Nations.states.get(i).name,
                        Nations.states.get(i).name + ", уровень: " + Nations.states.get(i).level + ", игроков: " + Nations.states.get(i).players.size(), true,
                        "world",new double[] {xMin, xMin+16},new double[] {zMin, zMin+16}, false);
                if(marker != null) {
                    marker.setFillStyle(0.5, Nations.states.get(i).color);
                    marker.setLineStyle(0, 0.5, Nations.states.get(i).color);
                    markers.add(marker);
                }else{
                    System.out.println("Null marker!");
                }
            }
        }

        /*for(int i = 0; i < Pillagers.pillagerTerritory.size(); i++){
            int xMin = Pillagers.pillagerTerritory.get(i).getX() * 16;
            int zMin = Pillagers.pillagerTerritory.get(i).getZ() * 16;

            AreaMarker marker = markerset.createAreaMarker(i+","+"pillagers",
                     "Разбойники", true,
                    "world",new double[] {xMin, xMin+16},new double[] {zMin, zMin+16}, false);
            if(marker != null) {
                marker.setFillStyle(0.5, 0x5c1a00);
                marker.setLineStyle(0, 0.5, 0x5c1a00);
                markers.add(marker);
            }else{
                System.out.println("Null marker!");
            }
        }*/
    }

}
