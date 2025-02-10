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
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Save data to config
        config.set("class", getPlayerClass(player));
        config.set("level", getPlayerLevel(player));
        // Save more data as needed

        try {
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
        // TODO: Implement this method
        return ""; // Placeholder return statement
    }

    public int getPlayerLevel(Player player) {
        // TODO: Implement this method
        return 0; // Placeholder return statement
    }
}