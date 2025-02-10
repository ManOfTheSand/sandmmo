package com.sandmmo.managers;

import com.sandmmo.classes.PlayerClass;
import com.sandmmo.config.ClassConfig;

public class ClassManager {
    private final ClassConfig classConfig;

    public ClassManager(ClassConfig classConfig) {
        this.classConfig = classConfig;
    }

    public PlayerClass getClass(String className) {
        return classConfig.getClass(className);
    }

    public String[] getAvailableClasses() {
        // Get class names from configuration
        return classConfig.getConfig().getConfigurationSection("classes")
                .getKeys(false)
                .toArray(new String[0]);
    }
}