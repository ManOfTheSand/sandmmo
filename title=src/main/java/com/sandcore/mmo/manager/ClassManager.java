package com.sandcore.mmo.manager;

import com.sandcore.mmo.classes.ClassDefinition;
import com.sandcore.mmo.classes.SoundSettings;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Collection;

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
            String lore = sec.getString("lore", "");

            // Load startingStats.
            Map<String, Double> startingStats = new HashMap<>();
            ConfigurationSection statsSec = sec.getConfigurationSection("startingStats");
            if (statsSec != null) {
                for (String statKey : statsSec.getKeys(false)) {
                    startingStats.put(statKey, statsSec.getDouble(statKey, 0));
                }
            }

            // Load keyCombos.
            Map<String, String> keyCombos = new HashMap<>();
            ConfigurationSection combosSec = sec.getConfigurationSection("keyCombos");
            if (combosSec != null) {
                for (String comboKey : combosSec.getKeys(false)) {
                    String skillId = combosSec.getString(comboKey, "");
                    keyCombos.put(comboKey, skillId);
                }
            }

            // Load sound settings.
            SoundSettings castingSound = loadSoundSettings(sec, "castingSound");
            SoundSettings comboClickSound = loadSoundSettings(sec, "comboClickSound");
            SoundSettings comboFailSound = loadSoundSettings(sec, "comboFailSound");

            ClassDefinition def = new ClassDefinition(key, displayName, lore, startingStats, keyCombos,
                                                        castingSound, comboClickSound, comboFailSound);
            classes.put(key.toLowerCase(), def);
        }
    }

    private SoundSettings loadSoundSettings(ConfigurationSection sec, String path) {
        if (sec.isConfigurationSection(path)) {
            ConfigurationSection soundSec = sec.getConfigurationSection(path);
            String name = soundSec.getString("name", "BLOCK_NOTE_BLOCK_PLING");
            float volume = (float) soundSec.getDouble("volume", 1.0);
            float pitch = (float) soundSec.getDouble("pitch", 1.0);
            return new SoundSettings(name, volume, pitch);
        }
        return new SoundSettings("BLOCK_NOTE_BLOCK_PLING", 1.0f, 1.0f);
    }

    public ClassDefinition getClassDefinition(String id) {
        return classes.get(id.toLowerCase());
    }

    public Collection<ClassDefinition> getAllClasses() {
        return classes.values();
    }
} 