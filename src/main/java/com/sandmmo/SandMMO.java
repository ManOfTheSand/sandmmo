package com.sandmmo;

import org.bukkit.plugin.java.JavaPlugin;
import com.willfp.eco.core.PluginLike;
import com.willfp.eco.core.config.ConfigHandler;
import com.willfp.eco.core.config.LangYml;
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
    private final LangYml langYml = new DummyLangYml();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Initializing SandMMO");

        // Ensure data folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Initialize core components
        classesConfig = new ClassesConfig(this);
        PlayerDataManager playerDataManager = new PlayerDataManager();
        classManager = new ClassManager(classesConfig, playerDataManager);
        classGUI = new ClassGUI(classesConfig, classManager);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Disabling SandMMO");
    }

    // Required by PluginLike
    @Override
    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    // Required by PluginLike
    @Override
    public LangYml getLangYml() {
        return langYml;
    }

    // Required by commands
    public ClassGUI getClassGUI() {
        return classGUI;
    }

    /* Eco Interface Implementations */
    private class DummyConfigHandler implements ConfigHandler {
        @Override public String getConfigName() { return "config.yml"; }
        @Override public void reload() {}
        @Override public void save() {}
        @Override public boolean isValid() { return true; }
    }

    private class DummyLangYml extends LangYml {
        public DummyLangYml() {
            super(SandMMO.this, "lang.yml");
        }
    }
}