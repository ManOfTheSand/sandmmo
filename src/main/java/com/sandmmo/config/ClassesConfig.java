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
        super(
                "classes",
                false,
                plugin,
                ClassesConfig.class,
                "classes.yml",
                ConfigType.YAML
        );
        loadClasses();
    }




    private void loadClasses() {
        Config section = this.getSubsection("classes");
        for (String classId : section.getKeys(false)) {
            classes.put(classId, new MMOClass(
                    section.getString(classId + ".display-name"),
                    section.getStrings(classId + ".description"),
                    section.getDouble(classId + ".base-health"),
                    section.getDouble(classId + ".health-per-level"),
                    section.getDouble(classId + ".base-damage"),
                    section.getDouble(classId + ".damage-per-level")
            ));
        }
    }

    public Map<String, MMOClass> getClasses() {
        return Collections.unmodifiableMap(classes);
    }

    public static class MMOClass {
        private final String displayName;
        private final List<String> description;
        private final double baseHealth;
        private final double healthPerLevel;
        private final double baseDamage;
        private final double damagePerLevel;

        public MMOClass(String displayName, List<String> description, double baseHealth, double healthPerLevel, double baseDamage, double damagePerLevel) {
            this.displayName = displayName;
            this.description = description;
            this.baseHealth = baseHealth;
            this.healthPerLevel = healthPerLevel;
            this.baseDamage = baseDamage;
            this.damagePerLevel = damagePerLevel;
        }

        public String displayName() {
            return displayName;
        }

        public List<String> description() {
            return description;
        }

        public double baseHealth() {
            return baseHealth;
        }

        public double healthPerLevel() {
            return healthPerLevel;
        }

        public double baseDamage() {
            return baseDamage;
        }

        public double damagePerLevel() {
            return damagePerLevel;
        }
    }
}