package com.sandmmo.config;

import com.sandmmo.classes.PlayerClass;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ClassConfig {
    private final JavaPlugin plugin;
    private final Map<String, PlayerClass> classes = new HashMap<>();

    public ClassConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "classes.yml");
        if (!file.exists()) plugin.saveResource("classes.yml", false);

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.getConfigurationSection("classes").getKeys(false).forEach(className -> {
            String path = "classes." + className + ".";
            classes.put(className, new PlayerClass(
                    className,
                    config.getString(path + "display-name"),
                    config.getString(path + "color")
            ));
        });
    }

    public Map<String, PlayerClass> getClasses() {
        return classes;
    }
}