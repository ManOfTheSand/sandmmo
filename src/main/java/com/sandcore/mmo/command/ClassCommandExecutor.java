package com.sandcore.mmo.command;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.manager.ClassManager.PlayerClass;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class ClassCommandExecutor implements CommandExecutor {

    /**
     * Executes the class command. If no arguments are provided, lists available classes;
     * if a class identifier is provided, attempts to set the player's class.
     *
     * Usage: /sandmmo class [classId]
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can execute this command.
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command.");
            return true;
        }
        Player player = (Player) sender;
        ClassManager classManager = ServiceRegistry.getClassManager();
        if (classManager == null) {
            player.sendMessage(ChatColor.RED + "Class system is currently unavailable.");
            return true;
        }
        if (args.length == 0) {
            // List all available classes.
            player.sendMessage(ChatColor.YELLOW + "Available Classes:");
            Map<String, PlayerClass> classes = classManager.getAvailableClasses();
            if (classes.isEmpty()) {
                player.sendMessage(ChatColor.RED + "No classes available.");
                return true;
            }
            for (PlayerClass pc : classes.values()) {
                player.sendMessage(ChatColor.AQUA + pc.getId() + " - " + pc.getDisplayName() + ": " + pc.getDescription());
            }
            player.sendMessage(ChatColor.YELLOW + "Usage: /sandmmo class <classId>");
            return true;
        }
        // Attempt to set the player's class.
        String classId = args[0];
        boolean success = classManager.setPlayerClass(player, classId);
        if (success) {
            PlayerClass pc = classManager.getPlayerClass(player);
            player.sendMessage(ChatColor.GREEN + "Your class has been set to " + pc.getDisplayName() + "!");
        } else {
            player.sendMessage(ChatColor.RED + "Invalid class ID: " + classId);
        }
        return true;
    }
} 