package com.sandcore.mmo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sandcore.mmo.manager.ClassesManager;
import com.sandcore.mmo.manager.PlayerClass;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Admin command executor for forcing class changes.
 * Usage: /sandmmo admin class set <player> <class>
 */
public class AdminClassesCommandExecutor implements CommandExecutor {
    private JavaPlugin plugin;
    private ClassesManager classesManager;

    public AdminClassesCommandExecutor(JavaPlugin plugin, ClassesManager classesManager) {
        this.plugin = plugin;
        this.classesManager = classesManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for proper admin permission.
        if (!sender.hasPermission("sandmmo.admin.class")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        
        if (args.length < 5) {
            sender.sendMessage(ChatColor.RED + "Usage: /sandmmo admin class set <player> <class>");
            return true;
        }
        
        // Expect arguments: admin class set <player> <class>
        String playerName = args[3];
        String classId = args[4];
        
        Player target = plugin.getServer().getPlayerExact(playerName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }
        
        PlayerClass pClass = classesManager.getPlayerClass(classId);
        if (pClass == null) {
            sender.sendMessage(ChatColor.RED + "Invalid class. Available classes: " + classesManager.getAllClasses().keySet());
            return true;
        }
        
        // Force the player's class change.
        // Integration point: you should update your persistent storage and apply stats here.
        // e.g., PlayerClassStorage.savePlayerClass(target, pClass.getId());
        //       PlayerStatsApplier.apply(target, pClass);
        target.sendMessage(ChatColor.GREEN + "Your class has been set to " + ChatColor.translateAlternateColorCodes('&', pClass.getDisplayName()));
        sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s class to " + pClass.getDisplayName());
        
        return true;
    }
} 