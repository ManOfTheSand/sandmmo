package com.sandcore.mmo.command;

import com.sandcore.mmo.stats.AsyncAdvancedStatsGUIHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsCommandExecutor implements CommandExecutor {

    private final AsyncAdvancedStatsGUIHandler asyncStatsGUIHandler;

    public StatsCommandExecutor(JavaPlugin plugin) {
        this.asyncStatsGUIHandler = new AsyncAdvancedStatsGUIHandler(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can execute the command.
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can check stats.");
            return true;
        }
        Player player = (Player) sender;
        
        // Retrieve the player's current level from your leveling system.
        // For demonstration purposes, we use a dummy level of 10.
        int level = 10;
        asyncStatsGUIHandler.openStatsGUIAsync(player, level);
        return true;
    }
} 