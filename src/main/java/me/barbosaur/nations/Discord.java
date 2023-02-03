package me.barbosaur.nations;

import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Discord extends ListenerAdapter {

    public static JDA jda;
    public static Guild guild;

    public static void jdaInit() throws LoginException {

        jda = JDABuilder.create(Nations.config.getString("token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES).enableCache(CacheFlag.MEMBER_OVERRIDES).setMemberCachePolicy(MemberCachePolicy.ALL).build();
        guild = jda.getGuildById("776137007876407297");

    }

    public static boolean isVerified(String player){
        return Nations.discords.containsKey(player);
    }

    public static HashMap<String, Integer> codes =  new HashMap<>();
    public static HashMap<String, String> tempDiscords = new HashMap<>();

}
