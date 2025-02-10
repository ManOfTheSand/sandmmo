package com.sandmmo;

import com.sandmmo.config.ClassesConfig;
import com.sandmmo.config.MessagesConfig;
import com.sandmmo.gui.ClassGUI;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.willfp.eco.core.EcoPlugin;
import org.bukkit.entity.Player;

public class SandMMO extends EcoPlugin {
    private static SandMMO instance;
    private ClassesConfig classesConfig;
    private MessagesConfig messagesConfig;
    private PlayerDataManager playerDataManager;
    private ClassManager classManager;
    private ClassGUI classGUI;

    public void onPluginEnable() {
        instance = this;

        // Configs
        this.classesConfig = new ClassesConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        // Managers
        this.playerDataManager = new PlayerDataManager(this);
        this.classManager = new ClassManager(classesConfig, playerDataManager);
        this.classGUI = new ClassGUI(classesConfig, classManager);

        // Register commands
        registerCommands();
    }

    private void registerCommands() {
        new ClassCommand(this)
                .setExecutor((sender, args) -> {
                    if (sender instanceof Player player) {
                        classGUI.open(player);
                        return true;
                    }
                    return false;
                })
                .register();
    }

    public static SandMMO getInstance() {
        return instance;
    }
}