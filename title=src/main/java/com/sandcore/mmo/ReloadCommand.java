package com.sandcore.mmo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sandcore.mmo.stats.StatsGUI;
import com.sandcore.mmo.stats.StatsGUIConfig;
import com.sandcore.mmo.stats.StatsGUIHolder;
import com.sandcore.mmo.stats.StatsManager;
import com.sandcore.mmo.stats.ClassesConfig;

/**
 * A stub implementation for the /reload command.
 */
public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin, Object statsManager, Object classesConfig) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (!sender.hasPermission("sandmmo.reload")) {
             sender.sendMessage(ChatColor.RED + "You do not have permission to reload configurations.");
             return true;
         }
         plugin.reloadConfig();
         sender.sendMessage(ChatColor.GREEN + "Configurations reloaded successfully!");
         return true;
    }
} 