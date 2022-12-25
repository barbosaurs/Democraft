package me.barbosaur.nations.commands;

import org.bukkit.Chunk;
import org.bukkit.command.CommandSender;

import java.util.List;

public interface StateSubcommand {
    void executeCommand(String[] args, CommandSender sender, String p, String subcmd, Chunk chunk);
}
