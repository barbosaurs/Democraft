package me.barbosaur.nations;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.dynmap.markers.AreaMarker;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class State {

    public String name;
    public List<Chunk> territory;
    public List<String> players;
    public List<String> requests;
    public List<String> invitations;
    public List<String> leaders;
    public String leader;
    public int level;
    public int color;
    public int deletionCooldown;
    public int canTakeChunks;
    public int invasionCooldown;
    public int chunkTakeCooldown;

    public State(String name, String leader){
        this.leader = leader;
        this.name = name;
        this.players = new ArrayList<>();
        this.players.add(leader);
        this.territory = new ArrayList<>();
        this.level = 0;
        this.requests = new ArrayList<>();
        this.invitations = new ArrayList<>();
        this.leaders = new ArrayList<>();
        this.leaders.add(leader);
        this.deletionCooldown = 600;
        this.canTakeChunks = 0;
        this.color = Nations.colors.get(Nations.colors.keySet().toArray()[ThreadLocalRandom.current().nextInt(0, Nations.colors.keySet().size())]);
    }

    public static int stateCooldownGive = 86400;
    public static int invasionCooldownGive = 600;
    public static int chunkTakeCooldownGive = 86400;
    public static int deletionCooldownGive = 3600;
    public static int stateCooldownIfLeftGive = 86400;

    public void notifyAll(String msg){
        for(String s : this.players){
            Notifications.notify(s, msg);
        }
    }

    public static void onStart(){
        new BukkitRunnable() {
            @Override
            public void run() {
                for(State state : Nations.unconfirmedStates){
                    if(state.deletionCooldown > 0) {
                        state.deletionCooldown -= 1;
                    }else{
                        for(String s : state.players) {
                            if(!State.IsCitizen(s)) {
                                Notifications.notify(s, "Ваше неподтвержденное государство было удалено");
                            }
                        }
                        Nations.unconfirmedStates.remove(state);
                    }
                }
                for(String s : Nations.stateCooldown.keySet()) {
                    if (Nations.stateCooldown.get(s) > 0) {
                        Nations.stateCooldown.put(s, Nations.stateCooldown.get(s) - 1);
                    }
                }
                for(int i = 0; i < Nations.states.size(); i++){
                    if(Nations.states.get(i).chunkTakeCooldown > 0){
                        Nations.states.get(i).chunkTakeCooldown -= 1;
                        if(Nations.states.get(i).chunkTakeCooldown <= 0){
                            Nations.states.get(i).canTakeChunks = 5 + (Nations.states.get(i).level + 1) * 3;
                            Nations.states.get(i).notifyAll("Вы можете занять еще " + (5 + (Nations.states.get(i).level + 1) * 3)
                                    + " свободных чанков");
                        }
                    }
                    if(Nations.states.get(i).invasionCooldown > 0){
                        Nations.states.get(i).invasionCooldown -= 1;
                    }
                }

                for(Block b : TntLaunchers.launcherCooldown.keySet()){
                    if(TntLaunchers.launcherCooldown.get(b) > 0) {
                        TntLaunchers.launcherCooldown.put(b, TntLaunchers.launcherCooldown.get(b) - 1);
                    }
                }

            }
        }.runTaskTimer(Nations.getPlugin(), 20, 20);
    }


    public boolean isOnline(int percentage){

        List<String> onlinePlayers = new ArrayList<>();

        for(Player p : Bukkit.getOnlinePlayers()){
            if(Objects.equals(getPlayerCountry(p.getDisplayName()), this)){
                onlinePlayers.add(p.getDisplayName());
            }
        }
        System.out.println(percentage/100.0*this.players.size());
        return onlinePlayers.size() >= Math.round(percentage/100.0*this.players.size());
    }


    public static void replaceStateInList(State remove, State add){
        Nations.states.add(Nations.states.indexOf(remove), add);
    }

    public static void replaceStateInListUnconfirmed(State remove, State add){
        Nations.unconfirmedStates.add(Nations.unconfirmedStates.indexOf(remove), add);
    }

    public static State invitation(String player, String stateName){
        for(State state : Nations.unconfirmedStates){
            if(state.invitations.contains(player) && state.name.equals(stateName)){
                return state;
            }
        }
        return null;
    }

    public static boolean CountryExists(String state){
        for(int i = 0; i<Nations.states.size(); i++){
            if(Nations.states.get(i).name.equalsIgnoreCase(state)){
                return true;
            }
        }
        return false;
    }

    public static State getPlayerCountry(String player){
        for(State state : Nations.states){
            if(state.players.contains(player)){
                return state;
            }
        }
        return null;
    }

    public static State getPlayerCountryUnconfirmed(String player){
        for(State state : Nations.unconfirmedStates){
            if(state.players.contains(player)){
                return state;
            }
        }
        return null;
    }

    public static State leaderOfWhatUncornfirmed(String player){
        for(State state : Nations.unconfirmedStates){
            if(state.leader.equals(player)){
                return state;
            }
        }
        return null;
    }

    public static void RemoveChunkFromList(List<Chunk> list, Chunk chunk){
        for(Chunk chunk1 : list){
            if(chunk.getX() == chunk1.getX()){
                if(chunk.getZ() == chunk1.getZ()){
                    list.remove(chunk1);
                    return;
                }
            }
        }
    }

    public static State GetByName(String name){
        for(int i = 0; i<Nations.states.size(); i++){
            if(Nations.states.get(i).name.equalsIgnoreCase(name)){
                return Nations.states.get(i);
            }
        }
        return null;
    }

    public static State GetByNameUnconfirmed(String name){
        for(int i = 0; i<Nations.unconfirmedStates.size(); i++){
            if(Nations.unconfirmedStates.get(i).name.equalsIgnoreCase(name)){
                return Nations.unconfirmedStates.get(i);
            }
        }
        return null;
    }

    public static boolean IsOccupied(Chunk chunk){
        for(int i = 0; i<Nations.states.size(); i++){
            if(ContainsChunk(Nations.states.get(i).territory, chunk)){
                return true;
            }
        }
        return false;
    }

    public static boolean IsOccupiedUnconfirmed(Chunk chunk){
        for(int i = 0; i<Nations.unconfirmedStates.size(); i++){
            if(ContainsChunk(Nations.unconfirmedStates.get(i).territory, chunk)){
                return true;
            }
        }
        return false;
    }

    public static boolean ContainsChunk(List<Chunk> list, Chunk chunk){

        for(Chunk chunk1 : list){
            if(chunk.getX() == chunk1.getX()){
                if(chunk.getZ() == chunk1.getZ()){
                    return true;
                }
            }

        }
        return false;

    }

    public static boolean ChunkEqual(Chunk chunk1, Chunk chunk2){
        if(chunk1.getX() == chunk2.getX()) {
            if (chunk1.getZ() == chunk2.getZ()) {
                return true;
            }
        }
        return false;
    }

    public List<Player> getPlayers(){
        List<Player> players = new ArrayList<>();
        for(String s : this.players){
            if(Bukkit.getPlayerExact(s) != null) {
                players.add(Bukkit.getPlayerExact(s));
            }
        }
        return players;
    }

    public static boolean canBreak(String player, Chunk chunk){

        for(State state : Nations.states){
            if(ContainsChunk(state.territory, chunk)){
                if(state.players.contains(player)){
                    return true;
                }
            }
        }

        if(!State.IsOccupied(chunk)){
            return true;
        }

        return false;
    }

    public static int getLevel(String player){
        if(IsCitizen(player)){
            return getPlayerCountry(player).level;
        }else{
            return 0;
        }
    }


    public static boolean IsCitizen(String player){
        for(int i = 0; i<Nations.states.size(); i++){
            if(Nations.states.get(i).players.contains(player)){
                return true;
            }
        }
        return false;
    }

    public static boolean IsCitizenUnconfirmed(String player){
        for(int i = 0; i<Nations.unconfirmedStates.size(); i++){
            if(Nations.unconfirmedStates.get(i).players.contains(player)){
                return true;
            }
        }
        return false;
    }


    public static State getByChunk(Chunk chunk){
        for(State state : Nations.states){
            if(ContainsChunk(state.territory, chunk)){
                return state;
            }

        }
        return null;
    }

    public static List<AreaMarker> markers = new ArrayList<>();

    public static void updateMap(){
        for(AreaMarker marker1 : markers){
            marker1.deleteMarker();
        }
        for(int i = 0; i<Nations.states.size(); i++){
            for(int u = 0; u<Nations.states.get(i).territory.size(); u++){

                int xMin = Nations.states.get(i).territory.get(u).getX() * 16;
                int zMin = Nations.states.get(i).territory.get(u).getZ() * 16;

                AreaMarker marker = Nations.markerset.createAreaMarker(u+","+Nations.states.get(i).name,
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
    }

    public static void deleteMarkers(){
        for(AreaMarker marker1 : markers){
            marker1.deleteMarker();
        }
    }

}
