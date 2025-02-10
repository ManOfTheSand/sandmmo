package com.sandmmo.config;

import org.bukkit.configuration.file.FileConfiguration;
import java.util.List;
import java.util.Map;

public class ClassConfig {
    private static FileConfiguration config;

    public static void load(FileConfiguration configuration) {
        config = configuration;
    }

    public static Map<String, Object> getClassData(String className) {
        return config.getConfigurationSection("classes." + className).getValues(true);
    }

    public static List<String> getAvailableClasses() {
        return config.getStringList("classes");
    }
}