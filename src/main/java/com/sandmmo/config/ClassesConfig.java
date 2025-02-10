package com.sandmmo.config;

import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.ExtendableConfig;
import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.eco.core.PluginLike;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClassesConfig extends ExtendableConfig {
    public ClassesConfig(PluginLike plugin) {
        super(
                "classes",          // configName
                plugin,             // plugin
                ConfigType.YAML,    // configType
                "classes.yml",       // filePath
                true                // automaticallyAddKeys
        );
        loadClasses();
    }


    private void loadClasses() {
        Config section = this.getSubsection("classes");
        section.getKeys(false).forEach(classId ->
                classes.put(classId, new MMOClass(
                        section.getString(classId + ".display-name"),
                        section.getDouble(classId + ".base-health"),
                        section.getDouble(classId + ".health-per-level"),
                        section.getDouble(classId + ".base-damage"),
                        section.getDouble(classId + ".damage-per-level")
                ))
        );
    }

    public Map<String, MMOClass> getClasses() {
        return Collections.unmodifiableMap(classes);
    }

    public record MMOClass(
            String displayName,
            double baseHealth,
            double healthPerLevel,
            double baseDamage,
            double damagePerLevel
    ) {}
}