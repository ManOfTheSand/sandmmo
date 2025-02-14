package com.sandcore.mmo.stats;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Loads configuration options for the Stats GUI from config.yml.
 * Administrators can define the title, inventory size, background material, and per-stat settings.
 */
public class StatsGUIConfig {

    private final FileConfiguration config;

    public StatsGUIConfig(JavaPlugin plugin) {
        // Ensure the default config is saved if it doesn't exist.
        plugin.saveDefaultConfig();
        this.config = plugin.getConfig();
    }

    // Returns the GUI title with color codes supported (&).
    public String getTitle() {
        return config.getString("stats_gui.title", "Player Stats");
    }

    // Returns the inventory size for the stats GUI.
    public int getSize() {
        return config.getInt("stats_gui.size", 27);
    }

    /**
     * Returns the configuration section for a given stat.
     * E.g. for "maxHealth", "maxMana", etc.
     */
    public ConfigurationSection getStatSection(String statKey) {
        return config.getConfigurationSection("stats_gui.items." + statKey);
    }

    // Returns the background material (for empty slots) defined in the config.
    public Material getBackgroundMaterial() {
        String matName = config.getString("stats_gui.background.material", "BLACK_STAINED_GLASS_PANE");
        Material mat = Material.getMaterial(matName.toUpperCase());
        return (mat != null) ? mat : Material.BLACK_STAINED_GLASS_PANE;
    }
} 