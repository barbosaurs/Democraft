package me.barbosaur.nations.commands.stateSubcommands;

import me.barbosaur.nations.Discord;
import me.barbosaur.nations.Nations;
import me.barbosaur.nations.commands.StateSubcommand;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class Help implements StateSubcommand {

    private static HashMap<Player, Integer> tutorStep = new HashMap<>();

    @Override
    public void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk){

        Player player = (Player) sender;
        tutorStep.put(player, 0);
        TextComponent component = new TextComponent("*клик*");
        component.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/D7eB3GjytS"));
        sender.spigot().sendMessage(new TextComponent("Чтобы начать развитие, вам нужно зайти на дискорд сервер Демокрафта "), component);
        player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(tutorStep.get(player) == 0){
                    sender.sendMessage("======>\nЗатем пропишите /discord link ник#0000");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 1){
                    sender.sendMessage("======>\nВам придет сообщение с кодом. Введите его в игре командой /discord code <код>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 2){
                    sender.sendMessage("======>\nЗатем вы можете создать свое государство командой /state create <название>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 3) {
                    sender.sendMessage("======>\nИли вступить в уже существующее командой /state join <название>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 4){
                    sender.sendMessage("======>\nЧтобы увидеть список доступных государств пропишите /state list");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 5){
                    sender.sendMessage("======>\nЕсли вы вступаете в государство, то после подачи заявки ждите подтверждения");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 6){
                    sender.sendMessage("======>\nЕсли вы создаете свое государство, то вам нужно будет в течение часа пригласить людей командой /state invite <игрок>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if (tutorStep.get(player) == 7){
                    sender.sendMessage("======>\nЧтобы принять людей в свое государство нужно написать команду /state accept <игрок> или чтобы отклонить пропишите /state deny <игрок>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 8){
                    sender.sendMessage("======>\nПо умолчанию в вашем государстве будет 1 чанк территории на котором вы создавали его");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 9){
                    sender.sendMessage("======>\nЧтобы занимать новую территорию пропишите команду /state claim");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 10){
                    sender.sendMessage("======>\nТаким же образом вы можете завоевывать чужой чанк простояв на нем 10 минут");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 11) {
                    sender.sendMessage("======>\nВы можете посмотреть на чьей вы территории командой /state info");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 12){
                    sender.sendMessage("======>\nЧтобы посмотреть информацию о вашем государстве, прокачке пропишите команду /state me");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 13){
                    sender.sendMessage("======>\nЧтобы прокачивать государство соберите необходимые ресурсы указанные в /state me и пропишите /state upgrade держа их в инвентаре");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 14){
                    sender.sendMessage("======>\nЕсли вы являетесь основателем государства вы можете назначать людей на должность соправителя командой /state grant <игрок>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 15){
                    sender.sendMessage("======>\nИли снимать командой /state degrant <игрок>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 16){
                    sender.sendMessage("======>\nВы также можете передать государство игроку командой /state transfer <игрок>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 17){
                    sender.sendMessage("======>\nЕсли вы являетесь основателем или вас назначили на пост соправителя, вы можете принимать или отклонять заявки игроков");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 18){
                    sender.sendMessage("======>\nТакже в этом случае вы можете менять цвет государства /state color <цвет>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 19){
                    TextComponent component1 = new TextComponent("*клик*");
                    component1.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://135.181.237.62:25584/"));
                    sender.spigot().sendMessage(new TextComponent("======>\nОн будет отображаться на карте сервера "), component1);
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) == 20){
                    sender.sendMessage("======>\nА еще выгонять участников командой /state kick <игрок>");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                }else if(tutorStep.get(player) > 20){
                    sender.sendMessage("======>\nСчастливой игры!");
                    player.playSound(player, Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 10.0f, 1.0f);
                    tutorStep.remove(player);
                    cancel();
                }
                tutorStep.put(player, tutorStep.get(player) + 1);

            }
        }.runTaskTimer(Nations.getPlugin(), 100, 100);

    }

}
