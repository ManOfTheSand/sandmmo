package com.sandcore.mmo.command;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.manager.PlayerStatsApplier;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ResetStatsCommandExecutor implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // This command is only for players.
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }
        Player player = (Player) sender;
        
        // Get the player's current class via the ClassManager.
        ClassManager classManager = ServiceRegistry.getClassManager();
        if (classManager == null) {
            player.sendMessage("Class Manager is not available.");
            return true;
        }
        ClassManager.PlayerClass playerClass = classManager.getPlayerClass(player);
        if (playerClass == null) {
            player.sendMessage("You have not selected a class yet.");
            return true;
        }
        
        // Reset the player's stats to the starting values from their class.
        PlayerStatsApplier.applyStats(player);
        
        player.sendMessage("§aYour stats have been reset to the starting values of your class: " + playerClass.getDisplayName());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // No arguments for this command; return an empty list.
        return Collections.emptyList();
    }
} 