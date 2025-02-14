package com.sandcore.mmo.stats;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * Loads configuration options for the Stats GUI from "gui.yml".
 * Administrators can customize the GUI layout, including the title, inventory size,
 * background material, and definition of the main item that displays all stats.
 */
public class StatsGUIConfig {

    private final FileConfiguration config;

    public StatsGUIConfig(JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder(), "gui.yml");
        if (!file.exists()) {
            plugin.saveResource("gui.yml", false);
        }
        this.config = YamlConfiguration.loadConfiguration(file);
    }

    // Returns the GUI title (supports color codes using '&').
    public String getTitle() {
        return config.getString("stats_gui.title", "Player Stats");
    }

    // Returns the inventory size.
    public int getSize() {
        return config.getInt("stats_gui.size", 27);
    }

    // Returns the background material.
    public Material getBackgroundMaterial() {
        String matName = config.getString("stats_gui.background.material", "BLACK_STAINED_GLASS_PANE");
        Material mat = Material.getMaterial(matName.toUpperCase());
        return (mat != null) ? mat : Material.BLACK_STAINED_GLASS_PANE;
    }

    // Returns the configuration section for the main item.
    public ConfigurationSection getMainItemSection() {
        return config.getConfigurationSection("stats_gui.mainItem");
    }
} 