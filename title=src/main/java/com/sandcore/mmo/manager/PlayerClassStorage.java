package com.sandcore.mmo.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

/**
 * Handles persistently storing and retrieving a player's chosen class.
 */
public class PlayerClassStorage {
    private JavaPlugin plugin;
    private File storageFile;
    private YamlConfiguration config;
    
    public PlayerClassStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        storageFile = new File(plugin.getDataFolder(), "playerclasses.yml");
        if (!storageFile.exists()) {
            try {
                storageFile.createNewFile();
            } catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Could not create playerclasses.yml", e);
            }
        }
        config = YamlConfiguration.loadConfiguration(storageFile);
    }
    
    /**
     * Saves the chosen class for the player.
     * @param player The player.
     * @param classId The chosen class id.
     */
    public void savePlayerClass(Player player, String classId) {
        config.set(player.getUniqueId().toString(), classId);
        try {
            config.save(storageFile);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Could not save playerclasses.yml", e);
        }
    }
    
    /**
     * Retrieves the saved class for the player, or null if not set.
     * @param player The player.
     * @return The class id, or null.
     */
    public String getPlayerClass(Player player) {
        return config.getString(player.getUniqueId().toString());
    }
} 