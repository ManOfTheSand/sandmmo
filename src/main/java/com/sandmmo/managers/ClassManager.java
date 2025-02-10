package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ClassManager {
    private final SandMMO plugin;
    private final Map<String, Map<String, Object>> classes = new HashMap<>();

    public ClassManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void loadClasses() {
        classes.clear();

        ConfigurationSection classesSection = plugin.getConfig().getConfigurationSection("classes");
        if (classesSection == null) {
            plugin.getLogger().warning("No classes found in config!");
            return;
        }

        for (String className : classesSection.getKeys(false)) {
            ConfigurationSection classSection = classesSection.getConfigurationSection(className);
            if (classSection == null) {
                plugin.getLogger().warning("Invalid class section: " + className);
                continue;
            }

            String displayName = classSection.getString("display-name");
            String description = classSection.getString("description");
            String icon = classSection.getString("icon");

            if (displayName == null || description == null || icon == null) {
                plugin.getLogger().warning("Missing required fields for class: " + className);
                continue;
            }

            Map<String, Object> classData = new HashMap<>();
            classData.put("display-name", displayName);
            classData.put("description", description);
            classData.put("icon", icon);

            ConfigurationSection statsSection = classSection.getConfigurationSection("stats");
            if (statsSection != null) {
                classData.put("stats", statsSection.getValues(false));
            }

            classes.put(className, classData);
        }

        plugin.getLogger().info("Loaded " + classes.size() + " classes.");
    }

    public Map<String, Object> getClass(String name) {
        return classes.get(name);
    }

    public Map<String, Map<String, Object>> getAllClasses() {
        return new HashMap<>(classes);
    }

    public void selectClass(Player player, String className) {
        Map<String, Object> classData = classes.get(className);
        if (classData == null) {
            player.sendMessage("Invalid class: " + className);
            return;
        }

        plugin.getPlayerManager().setPlayerClass(player, className);
        player.sendMessage("You have selected the " + classData.get("display-name") + " class!");
    }

    public Map<String, Double> getClassStats(String className, int level) {
        Map<String, Object> classData = classes.get(className);
        if (classData == null) {
            return null;
        }

        Map<String, Double> stats = new HashMap<>();
        ConfigurationSection statsSection = (ConfigurationSection) classData.get("stats");
        if (statsSection != null) {
            for (String stat : statsSection.getKeys(false)) {
                double baseValue = statsSection.getDouble(stat);
                double scaledValue = baseValue * level;
                stats.put(stat, scaledValue);
            }
        }

        return stats;
    }
}