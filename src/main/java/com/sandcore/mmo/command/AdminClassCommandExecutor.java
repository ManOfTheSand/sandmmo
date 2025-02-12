package com.sandcore.mmo.command;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminClassCommandExecutor implements CommandExecutor {

    /**
     * Executes the admin class command.
     * Usage: /sandmmo admin class <player> <classId>
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check for proper permissions.
        if (!sender.hasPermission("sandmmo.admin.class")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /sandmmo admin class <player> <classId>");
            return true;
        }
        String targetName = args[0];
        String classId = args[1];
        Player target = Bukkit.getPlayerExact(targetName);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + "Player " + targetName + " not found.");
            return true;
        }
        ClassManager classManager = ServiceRegistry.getClassManager();
        if (classManager == null) {
            sender.sendMessage(ChatColor.RED + "Class system is currently unavailable.");
            return true;
        }
        boolean success = classManager.adminSetClass(target, classId);
        if (success) {
            sender.sendMessage(ChatColor.GREEN + "Set " + target.getName() + "'s class to " + classId + " successfully.");
            target.sendMessage(ChatColor.GREEN + "An administrator has set your class to " + classId + ".");
        } else {
            sender.sendMessage(ChatColor.RED + "Failed to set class. Invalid class ID: " + classId);
        }
        return true;
    }
} 