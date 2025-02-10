package com.sandmmo;

import org.bukkit.plugin.java.JavaPlugin;
import com.sandmmo.config.ClassesConfig;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.sandmmo.gui.ClassGUI;

public class SandMMO extends JavaPlugin {

    private ClassGUI classGUI;
    private ClassesConfig classesConfig;
    private ClassManager classManager;

    @Override
    public void onEnable() {
        getLogger().info("Initializing SandMMO");

        // Initialize core components
        classesConfig = new ClassesConfig(this);
        PlayerDataManager playerDataManager = new PlayerDataManager();
        classManager = new ClassManager(classesConfig, playerDataManager);
        classGUI = new ClassGUI(classesConfig, classManager);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SandMMO");
    }

    public ClassGUI getClassGUI() {
        return classGUI;
    }
}