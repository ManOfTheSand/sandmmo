package com.sandmmo.config;

import com.willfp.eco.core.EcoPlugin;
import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.ExtendableConfig;
import com.willfp.eco.core.config.interfaces.Config;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class ClassesConfig extends ExtendableConfig {
    private final Map<String, ClassData> classes = new HashMap<>();

    public ClassesConfig(JavaPlugin plugin) {
        // Pass EcoPlugin.getInstance() to satisfy the PluginLike parameter expected by ExtendableConfig.
        super("classes", true, EcoPlugin.getInstance(), ConfigType.YAML);
        this.loadClasses();
    }

    private void loadClasses() {
        classes.clear();
        Config section = this.getSubsection("classes");

        section.getKeys(false).forEach(key -> {
            String displayName = section.getFormattedString(key + ".display-name");
            List<String> description = section.getFormattedStrings(key + ".description");
            String icon = section.getString(key + ".icon");
            double health = section.getDouble(key + ".stats.health");
            double attack = section.getDouble(key + ".stats.attack-damage");

            classes.put(key, new ClassData(displayName, description, icon, health, attack));
        });
    }

    public Map<String, ClassData> getClasses() {
        return classes;
    }

    public record ClassData(String displayName, List<String> description,
                            String icon, double health, double attack) {}
}