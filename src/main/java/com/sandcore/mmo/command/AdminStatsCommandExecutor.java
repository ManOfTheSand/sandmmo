package com.sandcore.mmo.command;

import com.sandcore.mmo.SandCoreMain;
import com.sandcore.mmo.manager.PlayerStatsApplier;
import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command executor for admin stat updates.
 * 
 * Command syntax:
 *    /sandmmo admin stats set <player> <attribute> <value>
 *    /sandmmo admin stats add <player> <attribute> <value>
 * 
 * This executor verifies admin permissions ("sandmmo.admin.stats"), parses the command,
 * and:
 *   - For a "set" operation, updates the target player's specified attribute to an exact value.
 *   - For an "add" operation, it increments the current value by the specified amount.
 * Then, it reapplies the player's stats in-game via PlayerStatsApplier which also refreshes the stats GUI.
 */
public class AdminStatsCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check.
        if (!sender.hasPermission("sandmmo.admin.stats")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // Validate argument length.
        // Expected structure: /sandmmo admin stats <set|add> <player> <attribute> <value>
        if (args.length < 6) {
            sender.sendMessage(ChatColor.RED + "Usage: /sandmmo admin stats <set|add> <player> <attribute> <value>");
            return true;
        }

        // Parse command arguments.
        // args[0] should be "admin" and args[1] should be "stats", so we start parsing at args[2].
        String action = args[2];
        String targetPlayerName = args[3];
        String attribute = args[4].toLowerCase();
        // Map certain aliases: if admin uses "health" or "mana", map to "maxHealth" or "maxMana" respectively.
        if (attribute.equals("health")) {
            attribute = "maxHealth";
        } else if (attribute.equals("mana")) {
            attribute = "maxMana";
        }
        double valueChange;
        try {
            valueChange = Double.parseDouble(args[5]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid numeric value: " + args[5]);
            return true;
        }

        // Lookup the target player.
        Player target = Bukkit.getPlayerExact(targetPlayerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player " + targetPlayerName + " not found.");
            return true;
        }

        // Retrieve the central StatsManager instance.
        StatsManager statsManager = ServiceRegistry.getStatsManager();
        if (statsManager == null) {
            sender.sendMessage(ChatColor.RED + "StatsManager is currently unavailable.");
            return true;
        }

        try {
            if (action.equalsIgnoreCase("set")) {
                // Update the specified stat to the new value.
                // You must implement setStat(Player, String, double) inside StatsManager.
                statsManager.setStat(target, attribute, valueChange);
            } else if (action.equalsIgnoreCase("add")) {
                // Add the specified value to the player's current stat.
                // You must implement addStat(Player, String, double) inside StatsManager.
                statsManager.addStat(target, attribute, valueChange);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid action. Use 'set' or 'add'.");
                return true;
            }

            // Reapply stats for the target player; this updates in-game attributes and refreshes the GUI.
            PlayerStatsApplier.applyStats(target);

            sender.sendMessage(ChatColor.GREEN + "Successfully " + action + "ed " + attribute +
                                   " for " + target.getName() + " by " + valueChange + ".");
            target.sendMessage(ChatColor.GREEN + "Your " + attribute + " has been updated by an admin.");

        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Error updating stats: " + ex.getMessage());
            ex.printStackTrace();
        }

        return true;
    }
} 