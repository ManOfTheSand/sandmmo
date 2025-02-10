package com.sandmmo;

import org.bukkit.plugin.java.JavaPlugin;
import com.willfp.eco.core.PluginLike;
import com.willfp.eco.core.EcoPlugin;
import com.sandmmo.config.ClassesConfig;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.sandmmo.gui.ClassGUI;
import java.io.File;

public class SandMMO extends JavaPlugin implements PluginLike {

    private ClassGUI classGUI;
    private ClassesConfig classesConfig;
    private ClassManager classManager;

    @Override
    public void onEnable() {
        getLogger().info("Initializing SandMMO");

        // Ensure plugin folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Check for lang.yml; if not found, save the default resource
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            saveResource("lang.yml", false);
            getLogger().info("Default lang.yml saved.");
        } else {
            getLogger().info("lang.yml found.");
        }

        // Retrieve Eco plugin instance if available.
        EcoPlugin eco = (EcoPlugin) getServer().getPluginManager().getPlugin("eco");
        if (eco == null) {
            getLogger().warning("Eco plugin not found! Economy features will be disabled.");
        } else {
            getLogger().info("Eco plugin detected.");
        }

        // Initialize dependencies for ClassGUI.
        // ClassesConfig requires a PluginLike; since this class now implements it, pass 'this'.
        classesConfig = new ClassesConfig(this);
        // ClassManager now requires ClassesConfig and a PlayerDataManager.
        PlayerDataManager playerDataManager = new PlayerDataManager(this);
        classManager = new ClassManager(classesConfig, playerDataManager);
        classGUI = new ClassGUI(classesConfig, classManager);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SandMMO");
    }

    // Used for accessing the ClassGUI from other parts of the plugin.
    public ClassGUI getClassGUI() {
        return this.classGUI;
    }

    // Minimal implementation of PluginLike.
    @Override
    public String getName() {
        return super.getName();
    }
}