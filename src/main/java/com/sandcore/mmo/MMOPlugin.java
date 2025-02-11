package com.sandcore.mmo;

import com.sandcore.mmo.gui.AsyncGUIHandler;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MMOPlugin extends JavaPlugin {
    private AsyncGUIHandler guiHandler;
    
    @Override
    public void onEnable() {
        // Initialize core systems
        guiHandler = new AsyncGUIHandler(this);
        
        // Register commands
        getCommand("stats").setExecutor((sender, command, label, args) -> {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                guiHandler.openGUI(player, "main_menu");
            }
            return true;
        });
        
        getLogger().info("MMO plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MMO plugin disabled!");
    }
} 