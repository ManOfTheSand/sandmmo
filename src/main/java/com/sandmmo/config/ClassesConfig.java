package com.sandmmo.config;

import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.ExtendableConfig;
import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.eco.core.PluginLike;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassesConfig extends ExtendableConfig {
    private final Map<String, MMOClass> classes = new HashMap<>();

    public ClassesConfig(PluginLike plugin) {
        super("classes", true, plugin, ClassesConfig.class, "", ConfigType.YAML, new String[0]);
        loadClasses();
    }

    private void loadClasses() {
        classes.clear();
        Config section = this.getSubsection("classes");

        for (String classId : section.getKeys(false)) {
            MMOClass mmoClass = new MMOClass(
                    section.getFormattedString(classId + ".display-name"),
                    section.getStrings(classId + ".description"),
                    section.getString(classId + ".permission"),
                    section.getInt(classId + ".max-level"),
                    section.getDouble(classId + ".base-health"),
                    section.getDouble(classId + ".health-per-level"),
                    section.getDouble(classId + ".base-damage"),
                    section.getDouble(classId + ".damage-per-level"),
                    section.getStringList(classId + ".skills"),
                    section.getStringList(classId + ".requirements")
            );
            classes.put(classId, mmoClass);
        }
    }

    public Map<String, MMOClass> getClasses() {
        return Collections.unmodifiableMap(classes);
    }

    public record MMOClass(
            String displayName,
            List<String> description,
            String permission,
            int maxLevel,
            double baseHealth,
            double healthPerLevel,
            double baseDamage,
            double damagePerLevel,
            List<String> skills,
            List<String> requirements
    ) {}
}