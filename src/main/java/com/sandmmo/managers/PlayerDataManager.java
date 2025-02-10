package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class PlayerDataManager {
    private final SandMMO plugin;
    private final File dataFolder;

    public PlayerDataManager(SandMMO plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if(!dataFolder.exists()) dataFolder.mkdirs();
    }

    public void savePlayerData(Player player) {
        try {
            File file = new File(dataFolder, player.getUniqueId() + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

            config.set("class", getPlayerClass(player));
            config.set("level", getPlayerLevel(player));
            // Add more stats

            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save player data: " + e.getMessage());
        }
    }

    public void loadPlayerData(Player player) {
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        if(!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        // Load data from config
    }

    public String getPlayerClass(Player player) {
        // Implementation
    }

    public int getPlayerLevel(Player player) {
        // Implementation
    }
}