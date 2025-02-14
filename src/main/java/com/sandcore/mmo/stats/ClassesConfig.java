package com.sandcore.mmo.stats;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.logging.Logger;

/**
 * Loads and provides access to class configuration from classes.yml.
 * Example structure in classes.yml:
 *
 * classes:
 *   warrior:
 *     bonus:
 *       maxHealth: 20
 *       strength: 5
 *       defense: 3
 *   mage:
 *     bonus:
 *       maxMana: 30
 *       magicDamage: 10
 */
public class ClassesConfig {
    private final FileConfiguration config;
    private final Logger logger = Logger.getLogger(ClassesConfig.class.getName());

    public ClassesConfig(File dataFolder) {
        File file = new File(dataFolder, "classes.yml");
        if (!file.exists()) {
            logger.warning("classes.yml not found in data folder.");
            this.config = null;
        } else {
            this.config = YamlConfiguration.loadConfiguration(file);
        }
    }

    /**
     * Returns a ClassStats instance representing bonus stats for the given class.
     *
     * @param classId the class identifier (e.g., "warrior", "mage")
     * @return a ClassStats instance; if not defined, returns a default with all zeros.
     */
    public ClassStats getClassStats(String classId) {
        if (config == null) return new ClassStats();
        String path = "classes." + classId + ".bonus";
        if (!config.isConfigurationSection(path)) {
            return new ClassStats();
        }
        double bonusMaxHealth = config.getDouble(path + ".maxHealth", 0);
        double bonusMaxMana = config.getDouble(path + ".maxMana", 0);
        double bonusHealthRegen = config.getDouble(path + ".healthRegen", 0);
        double bonusManaRegen = config.getDouble(path + ".manaRegen", 0);
        double bonusStrength = config.getDouble(path + ".strength", 0);
        double bonusDexterity = config.getDouble(path + ".dexterity", 0);
        double bonusDefense = config.getDouble(path + ".defense", 0);
        double bonusMagicDamage = config.getDouble(path + ".magicDamage", 0);
        return new ClassStats(bonusMaxHealth, bonusMaxMana, bonusHealthRegen, bonusManaRegen,
                bonusStrength, bonusDexterity, bonusDefense, bonusMagicDamage);
    }
} 