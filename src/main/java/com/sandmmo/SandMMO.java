package com.sandmmo;

import com.sandmmo.config.ClassesConfig;
import com.sandmmo.config.MessagesConfig;
import com.sandmmo.gui.ClassGUI;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.command.CommandBase;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SandMMO extends EcoPlugin {
    private static SandMMO instance;
    private ClassesConfig classesConfig;
    private MessagesConfig messagesConfig;
    private PlayerDataManager playerDataManager;
    private ClassManager classManager;
    private ClassGUI classGUI;

    @Override
    public void onEnable() {
        instance = this;

        // Configs
        this.classesConfig = new ClassesConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        // Managers
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
                })
                .register();
    }

    public static SandMMO getInstance() {
        return instance;
    }
}