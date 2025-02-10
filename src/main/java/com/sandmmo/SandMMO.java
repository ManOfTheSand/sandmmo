package com.sandmmo;

import com.sandmmo.commands.ClassCommand;
import com.sandmmo.commands.StatsCommand;
import com.sandmmo.config.ClassConfig;
import com.sandmmo.listeners.PlayerListener;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.sandmmo.gui.ClassSelectionGUI;
import org.bukkit.plugin.java.JavaPlugin;

public class SandMMO extends JavaPlugin {
    private ClassManager classManager;
    private PlayerDataManager playerDataManager;
    private ClassSelectionGUI classSelectionGUI;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        classManager = new ClassManager(new ClassConfig(this));
        playerDataManager = new PlayerDataManager(this);
        classSelectionGUI = new ClassSelectionGUI(this);

        // Register commands
        getCommand("stats").setExecutor(new StatsCommand(this));
        getCommand("class").setExecutor(new ClassCommand(this));

        // Register event listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(classSelectionGUI, this);
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public ClassSelectionGUI getClassSelectionGUI() {
        return classSelectionGUI;
    }
}