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
 * AdminStatsCommandExecutor allows administrators to update player attributes.
 *
 * <p>Usage:
 *   /sandmmo admin stats set <player> <attribute> <value>
 *   /sandmmo admin stats add <player> <attribute> <value>
 *
 * <p>Valid attributes: maxHealth, maxMana, healthRegen, manaRegen, strength, dexterity, intellect, defense, magicDefense.
 * The command applies changes immediately in-game and refreshes the stats GUI.
 */
public class AdminStatsCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Permission check.
        if (!sender.hasPermission("sandmmo.admin.stats")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        
        // Validate arguments – expected: <set|add> <player> <attribute> <value>
        if (args.length < 4) {
            sender.sendMessage(ChatColor.RED + "Usage: /sandmmo admin stats <set|add> <player> <attribute> <value>");
            return true;
        }
        
        String action = args[0].toLowerCase();
        String targetPlayerName = args[1];
        String attribute = args[2].toLowerCase();
        // Map attribute aliases.
        if (attribute.equals("health") || attribute.equals("maxhealth")) {
            attribute = "maxHealth";
        } else if (attribute.equals("mana") || attribute.equals("maxmana")) {
            attribute = "maxMana";
        }
        
        // Validate attribute against allowed keys.
        java.util.List<String> validAttributes = java.util.Arrays.asList(
            "maxHealth", "maxMana", "healthRegen", "manaRegen",
            "strength", "dexterity", "intellect", "defense", "magicDefense"
        );
        if (!validAttributes.contains(attribute)) {
            sender.sendMessage(ChatColor.RED + "Invalid attribute: " + attribute +
                ". Valid attributes: " + validAttributes);
            return true;
        }
        
        double valueChange;
        try {
            valueChange = Double.parseDouble(args[3]);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Invalid numeric value: " + args[3]);
            return true;
        }
        
        // Lookup the target player.
        Player target = Bukkit.getPlayerExact(targetPlayerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player " + targetPlayerName + " not found.");
            return true;
        }
        
        // Retrieve the StatsManager.
        StatsManager statsManager = ServiceRegistry.getStatsManager();
        if (statsManager == null) {
            sender.sendMessage(ChatColor.RED + "Stats system is currently unavailable.");
            return true;
        }
        
        sender.sendMessage(ChatColor.YELLOW + "Processing " + action + " on " + attribute +
                " for " + target.getName() + " with value " + valueChange + ".");
        try {
            if (action.equals("set")) {
                statsManager.setStat(target, attribute, valueChange);
                sender.sendMessage(ChatColor.GREEN + "Set " + attribute + " for " +
                        target.getName() + " to " + valueChange + ".");
            } else if (action.equals("add")) {
                statsManager.addStat(target, attribute, valueChange);
                sender.sendMessage(ChatColor.GREEN + "Added " + valueChange + " to " +
                        attribute + " for " + target.getName() + ".");
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid action. Use 'set' or 'add'.");
                return true;
            }
            
            Bukkit.getLogger().info("AdminStatsCommandExecutor: " + action + " " + attribute +
                    " for " + target.getName() + " by " + valueChange + ".");
            
            // Reapply stats and refresh the GUI.
            PlayerStatsApplier.applyStats(target);
            target.sendMessage(ChatColor.GREEN + "Your " + attribute + " has been updated by an admin.");
        } catch (Exception ex) {
            sender.sendMessage(ChatColor.RED + "Error updating stats: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return true;
    }
} 