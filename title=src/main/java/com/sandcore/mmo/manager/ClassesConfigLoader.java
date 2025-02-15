package com.sandcore.mmo.manager;

import java.io.File;
import java.io.IOException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.logging.Level;

/**
 * This class ensures that the classes.yml configuration file
 * exists in the plugin's data folder. If it doesn't, the default
 * configuration from the plugin's resource folder is copied.
 */
public class ClassesConfigLoader {
    private File configFile;
    private FileConfiguration config;

    public ClassesConfigLoader(JavaPlugin plugin) {
        // Ensure the plugin data folder exists.
        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        configFile = new File(plugin.getDataFolder(), "classes.yml");
        if (!configFile.exists()) {
            plugin.getLogger().info("classes.yml not found, generating default configuration...");
            // This will copy the file from your plugin's /src/main/resources folder to the data folder.
            plugin.saveResource("classes.yml", false);
        }
        // Load the configuration.
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Returns the loaded configuration.
     * @return FileConfiguration instance representing classes.yml.
     */
    public FileConfiguration getConfig() {
        return config;
    }

    /**
     * Reloads the configuration from disk.
     */
    public void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    /**
     * Saves the configuration to disk.
     */
    public void saveConfig() {
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
} 