package com.sandmmo.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import com.sandmmo.SandMMO;

public class MessagesManager {
    private final SandMMO plugin;
    private FileConfiguration config;

    public MessagesManager(SandMMO plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveResource("messages.yml", false);
        config = plugin.getConfig();
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&',
                config.getString("messages." + path, "&cMessage not found: " + path));
    }
}