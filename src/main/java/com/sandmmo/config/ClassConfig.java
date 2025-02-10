package com.sandmmo.config;

import com.sandmmo.SandMMO;
import com.sandmmo.classes.PlayerClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ClassConfig {
    private final SandMMO plugin;
    private final File file;
    private final FileConfiguration config;
    private final Map<String, PlayerClass> classes;

    public ClassConfig(SandMMO plugin) {
        this.plugin = plugin;
        this.file = new File(plugin.getDataFolder(), "classes.yml");
        this.config = YamlConfiguration.loadConfiguration(file);
        this.classes = new HashMap<>();
        loadClasses();
    }

    private void loadClasses() {
        ConfigurationSection section = config.getConfigurationSection("classes");
        if (section != null) {
            for (String className : section.getKeys(false)) {
                String displayName = section.getString(className + ".display-name");
                PlayerClass playerClass = new PlayerClass(className, displayName);
                classes.put(className, playerClass);
            }
        }
    }

    public Map<String, PlayerClass> getClasses() {
        return classes;
    }
}