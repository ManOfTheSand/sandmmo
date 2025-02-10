package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class MessagesManager {
    private final SandMMO plugin;
    private FileConfiguration config;

    public MessagesManager(SandMMO plugin) {
        this.plugin = plugin;
        reload();
    }

    public void reload() {
        plugin.saveDefaultConfig();
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public String getMessage(String path) {
        return ChatColor.translateAlternateColorCodes('&', config.getString("messages." + path, ""));
    }

    public void sendMessage(Player player, String path, String... replacements) {
        String message = getMessage(path);
        for (int i = 0; i < replacements.length; i++) {
            message = message.replace("{" + i + "}", replacements[i]);
        }
        player.sendMessage(message);
    }
}