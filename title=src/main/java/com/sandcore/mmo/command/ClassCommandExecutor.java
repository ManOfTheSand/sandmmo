package com.sandcore.mmo.command;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.classes.ClassDefinition;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command executor for /class.
 * Usage:
 *   /class         -> lists available classes.
 *   /class <id>    -> sets the player's class.
 */
public class ClassCommandExecutor implements CommandExecutor {

    private final ClassManager classManager;

    public ClassCommandExecutor(ClassManager classManager) {
        this.classManager = classManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be run by a player.");
            return true;
        }
        Player player = (Player) sender;
        if (args.length == 0) {
            // List all available classes.
            StringBuilder sb = new StringBuilder();
            sb.append(ChatColor.GREEN).append("Available Classes: ");
            for (ClassDefinition def : classManager.getAllClasses()) {
                sb.append(def.getDisplayName()).append(" ");
            }
            player.sendMessage(sb.toString());
        } else if (args.length == 1) {
            // Set player's class.
            String classId = args[0];
            ClassDefinition def = classManager.getClassDefinition(classId);
            if (def == null) {
                player.sendMessage(ChatColor.RED + "That class does not exist.");
            } else {
                // In a full implementation, you'll store the player's class selection (e.g. in a database or files).
                // For demonstration, we simply send a confirmation.
                player.sendMessage(ChatColor.GREEN + "Your class has been set to " + def.getDisplayName());
                // Optionally, update player's stats and unlock skills.
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /class [classID]");
        }
        return true;
    }
} 