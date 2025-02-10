package com.sandmmo.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class MessageHelper {
    public static final String PREFIX = ChatColor.DARK_GRAY + "[" +
            ChatColor.LIGHT_PURPLE + "MMO" +
            ChatColor.DARK_GRAY + "] " + ChatColor.RESET;

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(PREFIX + ChatColor.translateAlternateColorCodes('&', message));
    }

    public static void logInfo(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.WHITE + message);
    }

    public static void logError(String message) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + ChatColor.RED + message);
    }
}