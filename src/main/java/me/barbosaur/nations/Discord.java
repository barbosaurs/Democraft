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

public class Discord extends ListenerAdapter implements TabExecutor {

    public static JDA jda;

    public Discord() throws LoginException {

        jda = JDABuilder.create(Nations.config.getString("token"), GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MEMBERS, GatewayIntent.DIRECT_MESSAGES).enableCache(CacheFlag.MEMBER_OVERRIDES).setMemberCachePolicy(MemberCachePolicy.ALL).build();

    }

    public static boolean isVerified(String player){
        return Nations.discords.containsKey(player);
    }

    private static HashMap<String, Integer> codes =  new HashMap<>();
    private static HashMap<String, String> tempDiscords = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        String subcmd = args[0];

        if(subcmd.equalsIgnoreCase("link")) {
            if (!Nations.discords.containsKey(sender.getName())) {
                if (args.length == 2) {
                    if(!Nations.discords.values().contains(args[1])) {
                        Guild guild = jda.getGuildById("776137007876407297");
                        Member member = guild.getMemberByTag(args[1]);
                        if (member != null) {
                            System.out.println("nonull");
                            User user = member.getUser();
                            int code = ThreadLocalRandom.current().nextInt(1000, 9999);

                            for (int i = 0; codes.containsValue(code); i++) {
                                code = ThreadLocalRandom.current().nextInt(1000, 9999);
                            }

                            codes.put(sender.getName(), code);
                            tempDiscords.put(sender.getName(), args[1]);

                            int finalCode = code;
                            user.openPrivateChannel().queue((channel) ->
                            {
                                channel.sendMessage("Ваш код подтверждения: " + finalCode + ". /discord code <код>").queue();
                            });
                        } else {
                            TextComponent component = new TextComponent("*клик*");
                            component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/D7eB3GjytS"));
                            sender.spigot().sendMessage(new TextComponent("Неправильный тег в дискорде (ник#0000). Попробуйте зайти на дискорд сервер Демокрафта "), component);
                        }
                    }
                } else {
                    sender.sendMessage("Неправильное количество аргументов. Введите тег в дискорде (ник#0000)");
                }
            } else {
                sender.sendMessage("Вы уже подтвердили свой дискорд");
            }
        }else if(subcmd.equalsIgnoreCase("code")){

            if (!Nations.discords.containsKey(sender.getName())) {
                if (args.length == 2) {

                    if(codes.get(sender.getName()).equals(Integer.valueOf(args[1]))){
                        Nations.discords.put(sender.getName(), tempDiscords.get(sender.getName()));
                        codes.remove(sender.getName());
                        tempDiscords.remove(sender.getName());
                        sender.sendMessage("Аккаунт подтвержден");
                    }else{
                        sender.sendMessage("Неверный код!");
                    }

                } else {
                    sender.sendMessage("Неправильное количество аргументов. Введите код который вам пришел");
                }
            } else {
                sender.sendMessage("Вы уже подтвердили свой дискорд");
            }
        }else{
            sender.sendMessage("/discord link <дискорд-тэг>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {

        if(command.getLabel().equalsIgnoreCase("discord")){
            if(args.length == 1){
                List<String> strings = new ArrayList<>();
                strings.add("link");
                strings.add("code");
                return strings;
            }
        }
        return null;
    }
}
