package me.barbosaur.nations;

import org.bukkit.ChatColor;

public class Lang {

    public static String getLang(String path, Object... format){

        String formatted = String.format(Nations.lang.getString(path), format);

        return ChatColor.translateAlternateColorCodes('&', formatted);
    }

    public static String getLangFromList(String path, int index, Object... format){
        String formatted = String.format(Nations.lang.getStringList(path).get(index), format);

        return ChatColor.translateAlternateColorCodes('&', formatted);
    }

}
