package com.sandcore.mmo.command;

import com.sandcore.mmo.gui.AsyncStatsGUIHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsCommandExecutor implements CommandExecutor {

    private final AsyncStatsGUIHandler guiHandler;

    public StatsCommandExecutor(JavaPlugin plugin, AsyncStatsGUIHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (!(sender instanceof Player)) {
             sender.sendMessage("Only players can execute this command.");
             return true;
         }
         Player player = (Player) sender;
         // Open the stats GUI showing current attributes and available stat points.
         guiHandler.openGUI(player);
         return true;
    }
} 