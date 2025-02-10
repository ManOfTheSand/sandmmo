package com.sandmmo.config;

import com.sandmmo.classes.PlayerClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class ClassConfig {
    private final FileConfiguration config;

    public ClassConfig(JavaPlugin plugin) {
        plugin.saveResource("classes.yml", false);
        this.config = plugin.getConfig();
    }

    public PlayerClass getClass(String className) {
        return new PlayerClass(
                config.getString("classes." + className + ".display-name"),
                config.getInt("classes." + className + ".base-health"),
                config.getInt("classes." + className + ".base-mana"),
                config.getDouble("classes." + className + ".strength-multiplier")
        );
    }

    public ConfigurationSection getConfig() {
    }
}