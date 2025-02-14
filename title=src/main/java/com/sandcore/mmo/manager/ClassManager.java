package com.sandcore.mmo.manager;

import com.sandcore.mmo.classes.ClassDefinition;
import com.sandcore.mmo.classes.SkillUnlock;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.*;

/**
 * Loads all class definitions from classes.yml and provides lookup methods.
 */
public class ClassManager {

    private final Map<String, ClassDefinition> classes = new HashMap<>();

    public ClassManager(File dataFolder) {
        loadClasses(new File(dataFolder, "classes.yml"));
    }

    public void loadClasses(File file) {
        if (!file.exists()) {
            return;
        }
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection classesSection = config.getConfigurationSection("classes");
        if (classesSection == null) return;
        for (String key : classesSection.getKeys(false)) {
            ConfigurationSection sec = classesSection.getConfigurationSection(key);
            if (sec == null) continue;

            String displayName = sec.getString("displayName", key);
            String description = sec.getString("description", "");

            // Load startingStats from config.
            Map<String, Double> startingStats = new HashMap<>();
            ConfigurationSection startSec = sec.getConfigurationSection("startingStats");
            if (startSec != null) {
                for (String statKey : startSec.getKeys(false)) {
                    startingStats.put(statKey, startSec.getDouble(statKey, 0));
                }
            }

            // Load perLevelStats from config.
            Map<String, Double> perLevelStats = new HashMap<>();
            ConfigurationSection perLevelSec = sec.getConfigurationSection("perLevelStats");
            if (perLevelSec != null) {
                for (String statKey : perLevelSec.getKeys(false)) {
                    perLevelStats.put(statKey, perLevelSec.getDouble(statKey, 0));
                }
            }

            // Load skills from config.
            List<SkillUnlock> skills = new ArrayList<>();
            ConfigurationSection skillsSec = sec.getConfigurationSection("skills");
            if (skillsSec != null) {
                for (String skillKey : skillsSec.getKeys(false)) {
                    ConfigurationSection skillSec = skillsSec.getConfigurationSection(skillKey);
                    if (skillSec != null) {
                        String skillId = skillSec.getString("id", skillKey);
                        int levelRequired = skillSec.getInt("levelRequired", 1);
                        skills.add(new SkillUnlock(skillId, levelRequired));
                    }
                }
            }

            ClassDefinition cd = new ClassDefinition(key, displayName, description, startingStats, perLevelStats, skills);
            classes.put(key.toLowerCase(), cd);
        }
    }

    public ClassDefinition getClassDefinition(String id) {
        return classes.get(id.toLowerCase());
    }

    public Collection<ClassDefinition> getAllClasses() {
        return classes.values();
    }
} 