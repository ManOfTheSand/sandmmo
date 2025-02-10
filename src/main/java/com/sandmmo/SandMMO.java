package com.sandmmo;

import com.sandmmo.commands.ClassCommand;
import com.sandmmo.config.ClassesConfig;
import com.sandmmo.config.MessagesConfig;
import com.sandmmo.gui.ClassGUI;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.willfp.eco.core.EcoPlugin;

public class SandMMO extends EcoPlugin {
    private static SandMMO instance;
    private ClassesConfig classesConfig;
    private MessagesConfig messagesConfig;
    private PlayerDataManager playerDataManager;
    private ClassManager classManager;
    private ClassGUI classGUI;

    // Use EcoPlugin's onPluginEnable (or attach your initialization in an alternative method)
    public void onPluginEnable() {
        instance = this;

        // Configs
        this.classesConfig = new ClassesConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        // Managers
        this.playerDataManager = new PlayerDataManager(this);
        this.classManager = new ClassManager(classesConfig, playerDataManager);
        this.classGUI = new ClassGUI(classesConfig, classManager);

        // Register commands using Bukkit's command system.
        registerCommands();
    }

    private void registerCommands() {
        // Ensure your plugin.yml defines the "class" command.
        getCommand("class").setExecutor(new ClassCommand(this));
    }

    public static SandMMO getInstance() {
        return instance;
    }

    public ClassGUI getClassGUI() {
        return classGUI;
    }
}