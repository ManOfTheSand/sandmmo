package com.sandmmo;

import com.sandmmo.config.ClassConfig;
import com.sandmmo.listeners.PlayerJoinListener;
import com.sandmmo.player.PlayerDataManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.sandmmo.managers.ClassManager;  // Changed from com.sandmmo.classes
import com.sandmmo.player.PlayerDataManager;

public final class SandMMO extends JavaPlugin {
    private ClassManager classManager;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
        // Load configurations
        saveDefaultConfig();
        ClassConfig classConfig = new ClassConfig(this);

        // Initialize managers
        this.classManager = new ClassManager(classConfig);
        this.playerDataManager = new PlayerDataManager(this);

        // Register events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        // Register commands
        getCommand("class").setExecutor(new ClassCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));

        getLogger().info("SandMMO enabled!");
    }

    @Override
    public void onDisable() {
        playerDataManager.saveAllData();
        getLogger().info("SandMMO disabled!");
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}