package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import com.sandmmo.data.ClassData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class ClassManager {
    private final SandMMO plugin;
    private final Map<String, ClassData> classes;

    public ClassManager(SandMMO plugin) {
        this.plugin = plugin;
        this.classes = new HashMap<>();
        loadClasses();
    }

    public void loadClasses() {
        classes.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("classes");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                String displayName = section.getString(key + ".display-name");
                List<String> description = section.getStringList(key + ".description");
                String icon = section.getString(key + ".icon");
                classes.put(key, new ClassData(displayName, description, icon));
            }
        }
    }

    public void selectClass(Player player, String className) {
        // Implementation for selecting a class
    }

    public Map<String, ClassData> getAllClasses() {
        return classes;
    }
}