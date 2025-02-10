package com.sandmmo;

import com.sandmmo.config.ClassConfig;
import com.sandmmo.config.GuiConfig;
import com.sandmmo.config.MessagesConfig;
import com.sandmmo.gui.ClassSelectionGUI;
import com.sandmmo.listeners.PlayerListener;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SandMMO extends JavaPlugin {
    private ClassManager classManager;
    private PlayerDataManager playerDataManager;
    private ClassSelectionGUI classSelectionGUI;
    private ClassConfig classConfig;
    private GuiConfig guiConfig;
    private MessagesConfig messagesConfig;

    @Override
    public void onEnable() {
        // Load configurations first
        this.classConfig = new ClassConfig(this);
        this.guiConfig = new GuiConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        // Initialize managers
        this.classManager = new ClassManager(classConfig);
        this.playerDataManager = new PlayerDataManager(this);
        this.classSelectionGUI = new ClassSelectionGUI(this);

        // Register commands
        getCommand("class").setExecutor(new ClassCommand(this));
        getCommand("stats").setExecutor(new StatsCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
        getServer().getPluginManager().registerEvents(classSelectionGUI, this);
    }

    // Getters
    public ClassManager getClassManager() { return classManager; }
    public PlayerDataManager getPlayerDataManager() { return playerDataManager; }
    public ClassSelectionGUI getClassSelectionGUI() { return classSelectionGUI; }
    public GuiConfig getGuiConfig() { return guiConfig; }
    public MessagesConfig getMessagesConfig() { return messagesConfig; }
}