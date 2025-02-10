package com.sandmmo.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import com.willfp.eco.api.EcoAPI;

public class MMOInfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // For demonstration, simulate retrieving some MMO-related data.
        String playerInfo = "Level: 10, XP: 1500, Gold: 200";

        sender.sendMessage(ChatColor.GREEN + "Your MMO Info: " + playerInfo);
        return true;
    }
}
