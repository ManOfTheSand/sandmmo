package com.sandmmo.config;

import com.sandmmo.classes.PlayerClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ClassConfig {
    private final FileConfiguration config;

    public ClassConfig(JavaPlugin plugin) {
        plugin.saveResource("classes.yml", false);
        this.config = plugin.getConfig();
    }

    public PlayerClass getClass(String className) {
        if (!config.contains("classes." + className)) {
            return null;
        }

        return new PlayerClass(
                config.getString("classes." + className + ".display-name", "Unknown Class"),
                config.getInt("classes." + className + ".base-health", 20),
                config.getInt("classes." + className + ".base-mana", 10),
                config.getDouble("classes." + className + ".strength-multiplier", 1.0)
        );
    }

    public FileConfiguration getConfig() {
        return config;
    }
}