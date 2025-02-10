package com.sandmmo;

import com.managers.SkillManager;
import com.managers.PlayerManager;
import com.managers.ClassManager;
import com.commands.ClassCommand;
import com.listeners.ClassListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class SandMMO extends JavaPlugin {
    private SkillManager skillManager;
    private PlayerManager playerManager;
    private ClassManager classManager;

    @Override
    public void onEnable() {
        // Make sure to save the default config if it doesn't exist.
        saveDefaultConfig();

        // Initialize managers
        this.classManager = new ClassManager(this);
        this.playerManager = new PlayerManager(this);
        this.skillManager = new SkillManager(this);

        // Register your command
        getCommand("class").setExecutor(new ClassCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new ClassListener(this), this);

        getLogger().info("SandMMO enabled!");
    }

    @Override
    public void onDisable() {
        // Save player data
        playerManager.saveAllData();
        getLogger().info("SandMMO disabled!");
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public ClassManager getClassManager() {
        return classManager;
    }
}