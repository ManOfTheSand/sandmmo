package com.sandmmo;

import org.bukkit.plugin.java.JavaPlugin;
import com.willfp.eco.core.PluginLike;
import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.configuration.ConfigHandler;
import com.sandmmo.config.ClassesConfig;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerDataManager;
import com.sandmmo.gui.ClassGUI;
import java.io.File;

public class SandMMO extends JavaPlugin implements PluginLike {

    private ClassGUI classGUI;
    private ClassesConfig classesConfig;
    private ClassManager classManager;
    private final ConfigHandler configHandler = new DummyConfigHandler();

    @Override
    public void onEnable() {
        getLogger().info("Initializing SandMMO");

        // Ensure the plugin folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Check for lang.yml; if missing, save the default resource.
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
        classesConfig = new ClassesConfig(this);
        PlayerDataManager playerDataManager = new PlayerDataManager(this);
        classManager = new ClassManager(classesConfig, playerDataManager);
        classGUI = new ClassGUI(classesConfig, classManager);
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SandMMO");
    }

    // Used by your commands and other subsystems.
    public ClassGUI getClassGUI() {
        return classGUI;
    }

    // Implement the abstract method from PluginLike.
    @Override
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    // Minimal dummy implementation of ConfigHandler for Eco integration.
    private static class DummyConfigHandler implements com.willfp.eco.core.configuration.ConfigHandler {
        @Override
        public String getConfigName() {
            return "lang.yml";
        }

        @Override
        public void reload() {
            // Add reload logic here if needed.
        }

        @Override
        public void save() {
            // Add save logic here if needed.
        }

        @Override
        public boolean isValid() {
            // Return true to indicate the config is valid.
            return true;
        }

        // Implement any other required methods of ConfigHandler here.
    }
}