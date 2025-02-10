package com.sandmmo;

import com.sandmmo.config.ClassesConfig;
import com.sandmmo.config.MessagesConfig;
import com.sandmmo.gui.ClassGUI;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandBase;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SandMMO extends EcoPlugin {
    private static SandMMO instance;

    // Configurations
    private ClassesConfig classesConfig;
    private MessagesConfig messagesConfig;

    // Managers
    private ClassManager classManager;
    private PlayerDataManager playerDataManager;

    // GUI
    private ClassGUI classGUI;

    @Override
    protected void postEnable() {
        instance = this;

        // Load configurations
        this.classesConfig = new ClassesConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        // Initialize managers
        this.playerDataManager = new PlayerDataManager(this);
        this.classManager = new ClassManager(classesConfig, playerDataManager);
        this.classGUI = new ClassGUI(classesConfig, classManager);

        // Register commands
        new CommandBase(this, "class")
                .setExecutor((sender, args) -> {
                    if (sender instanceof Player player) {
                        classGUI.open(player);
                        return true;
                    }
                    return false;
                });

        // Register listeners
        registerListeners(
                new PlayerJoinListener(playerDataManager),
                new PlayerQuitListener(playerDataManager),
                classGUI
        );

        getLogger().info("SandMMO has been enabled!");
    }

    @Override
    protected void postDisable() {
        // Save all player data
        Bukkit.getOnlinePlayers().forEach(player ->
                playerDataManager.savePlayerData(player));

        getLogger().info("SandMMO has been disabled!");
    }

    public static SandMMO getInstance() {
        return instance;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }
}