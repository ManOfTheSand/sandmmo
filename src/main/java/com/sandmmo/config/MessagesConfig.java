package com.sandmmo.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class MessagesConfig {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public MessagesConfig(JavaPlugin plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (!file.exists()) plugin.saveResource("messages.yml", false);
        config = YamlConfiguration.loadConfiguration(file);
    }

    public FileConfiguration get() {
        return config;
    }
}