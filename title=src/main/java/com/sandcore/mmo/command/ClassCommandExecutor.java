package com.sandcore.mmo.command;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.classes.ClassDefinition;
import com.sandcore.mmo.manager.PlayerClassDataManager;
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
            sender.sendMessage(ChatColor.RED + "Command can only be used by a player.");
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
            String id = args[0];
            ClassDefinition def = classManager.getClassDefinition(id);
            if (def == null) {
                player.sendMessage(ChatColor.RED + "That class does not exist.");
            } else {
                PlayerClassDataManager.setPlayerClass(player, id);
                player.sendMessage(ChatColor.GREEN + "Your class has been set to " + def.getDisplayName());
            }
        } else {
            player.sendMessage(ChatColor.RED + "Usage: /class [classId]");
        }
        return true;
    }
} 