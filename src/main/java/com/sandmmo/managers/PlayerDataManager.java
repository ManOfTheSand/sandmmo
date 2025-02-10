package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.io.File;
import java.io.IOException;

public class PlayerDataManager {
    private final SandMMO plugin;
    private final File dataFolder;

    public PlayerDataManager(SandMMO plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists()) dataFolder.mkdirs();
    }

    public void savePlayerData(Player player) {
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        // Save data to config
        config.set("class", getPlayerClass(player));
        config.set("level", getPlayerLevel(player));
        config.set("experience", getPlayerExperience(player));
        // Save more data as needed

        try {
            config.save(file);
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to save player data: " + e.getMessage());
        }
    }

    public void loadPlayerData(Player player) {
        File file = new File(dataFolder, player.getUniqueId() + ".yml");
        if (!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        // Load data from config
        setPlayerClass(player, config.getString("class", ""));
        setPlayerLevel(player, config.getInt("level", 1));
        setPlayerExperience(player, config.getInt("experience", 0));
        // Load more data as needed
    }

    public String getPlayerClass(Player player) {
        return player.hasMetadata("class") ? player.getMetadata("class").get(0).asString() : "";
    }

    public void setPlayerClass(Player player, String className) {
        player.setMetadata("class", new FixedMetadataValue(plugin, className));
    }

    public int getPlayerLevel(Player player) {
        return player.hasMetadata("level") ? player.getMetadata("level").get(0).asInt() : 1;
    }

    public void setPlayerLevel(Player player, int level) {
        player.setMetadata("level", new FixedMetadataValue(plugin, level));
    }

    public int getPlayerExperience(Player player) {
        return player.hasMetadata("experience") ? player.getMetadata("experience").get(0).asInt() : 0;
    }

    public void setPlayerExperience(Player player, int experience) {
        player.setMetadata("experience", new FixedMetadataValue(plugin, experience));
    }
}