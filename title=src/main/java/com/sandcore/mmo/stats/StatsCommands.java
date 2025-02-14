package com.sandcore.mmo.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * A stub implementation for the /stats command.
 */
public class StatsCommands implements CommandExecutor {

    private final JavaPlugin plugin;
    // In a full implementation, you would inject a proper stats manager and classes config.
    public StatsCommands(JavaPlugin plugin, Object statsManager, Object classesConfig) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (!(sender instanceof Player)) {
             sender.sendMessage(ChatColor.RED + "Only players can use this command.");
             return true;
         }
         Player player = (Player) sender;
         // Open the Stats GUI (stub).
         Bukkit.getScheduler().runTask(plugin, () -> {
             player.sendMessage(ChatColor.GREEN + "Opening your Stats GUI...");
         });
         return true;
    }
} 