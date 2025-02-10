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
        plugin.getLogger().info("Loading classes...");

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

            Map<String, Object> classData = new HashMap<>();
            classData.put("display-name", classSection.getString("display-name"));
            classData.put("description", classSection.getString("description"));
            classData.put("icon", classSection.getString("icon"));

            classes.put(className, classData);
        }

        plugin.getLogger().info("Loaded " + classes.size() + " classes.");
    }

    public Map<String, Object> getClass(String name) {
        return classes.get(name);
    }

    public Map<String, Map<String, Object>> getClasses() {
        return classes;
    }

    public void selectClass(Player player, String className) {
        Map<String, Object> classData = classes.get(className);
        if (classData == null) {
            player.sendMessage("Invalid class: " + className);
            return;
        }

        // TODO: Apply class effects, attributes, or permissions to the player here.
        player.sendMessage("You have selected the " + classData.get("display-name") + " class!");
    }
}