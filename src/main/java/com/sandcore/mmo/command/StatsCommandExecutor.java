package com.sandcore.mmo.command;

import com.sandcore.mmo.gui.AsyncGUIHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StatsCommandExecutor implements CommandExecutor {

    private final AsyncGUIHandler guiHandler;

    public StatsCommandExecutor(JavaPlugin plugin, AsyncGUIHandler guiHandler) {
        this.guiHandler = guiHandler;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (!(sender instanceof Player)) {
             sender.sendMessage("Only players can execute this command.");
             return true;
         }
         Player player = (Player) sender;
         // Open the configured GUI using the async handler with key "main_menu"
         guiHandler.openGUI(player, "main_menu");
         return true;
    }
} 