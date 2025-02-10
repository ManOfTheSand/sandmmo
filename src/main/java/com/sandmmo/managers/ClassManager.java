package com.sandmmo.managers;

import com.sandmmo.classes.PlayerClass;
import com.sandmmo.config.ClassConfig;
import org.bukkit.configuration.file.FileConfiguration;

public class ClassManager {
    private final ClassConfig classConfig;

    public ClassManager(ClassConfig classConfig) {
        this.classConfig = classConfig;
    }

    public PlayerClass getClass(String className) {
        return classConfig.getClass(className);
    }

    public String[] getAvailableClasses() {
        FileConfiguration config = classConfig.getConfig();
        if (!config.contains("classes")) return new String[0];

        return config.getConfigurationSection("classes")
                .getKeys(false)
                .toArray(new String[0]);
    }
}