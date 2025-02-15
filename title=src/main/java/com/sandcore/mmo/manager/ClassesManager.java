package com.sandcore.mmo.manager;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

/**
 * Manages loading and retrieval of player classes from configuration.
 */
public class ClassesManager {
    private Map<String, PlayerClass> classes = new HashMap<>();

    /**
     * Loads classes from classes.yml using the provided plugin.
     * @param plugin The JavaPlugin instance.
     */
    public void loadClasses(JavaPlugin plugin) {
        try (InputStream is = plugin.getResource("classes.yml")) {
            if (is == null) {
                plugin.getLogger().severe("classes.yml not found in resources!");
                return;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(is);
            // Assuming top-level key "classes"
            if (!config.isConfigurationSection("classes")) {
                plugin.getLogger().severe("No 'classes' section found in classes.yml");
                return;
            }
            
            Set<String> classIds = config.getConfigurationSection("classes").getKeys(false);
            for (String id : classIds) {
                String path = "classes." + id;
                String displayName = config.getString(path + ".displayName", id);
                String description = config.getString(path + ".description", "");
                String iconName = config.getString(path + ".icon", "STONE");
                Material icon = Material.getMaterial(iconName.toUpperCase());
                if (icon == null) {
                    plugin.getLogger().warning("Invalid icon '"+iconName+"' for class " + id + ". Defaulting to STONE.");
                    icon = Material.STONE;
                }
                
                // Load casting abilities for current class.
                Map<String, CastingAbility> castingAbilities = new HashMap<>();
                if (config.isConfigurationSection(path + ".castingAbilities")) {
                    Set<String> abilities = config.getConfigurationSection(path + ".castingAbilities").getKeys(false);
                    for (String combo : abilities) {
                        String abilityPath = path + ".castingAbilities." + combo;
                        String skill = config.getString(abilityPath + ".skill", "Unknown");
                        int cooldown = config.getInt(abilityPath + ".cooldown", 0);
                        // Load parameters from configuration; this returns a map of keys/values.
                        Map<String, Object> parameters = config.getConfigurationSection(abilityPath + ".parameters") != null 
                                ? config.getConfigurationSection(abilityPath + ".parameters").getValues(false)
                                : new HashMap<>();
                        CastingAbility ability = new CastingAbility(skill, cooldown, parameters);
                        castingAbilities.put(combo, ability);
                    }
                }
                
                // Load requirements.
                int level = config.getInt(path + ".requirements.level", 0);
                String unlockCriteria = config.getString(path + ".requirements.unlockCriteria", "None");
                Requirements requirements = new Requirements(level, unlockCriteria);
                
                // Create PlayerClass
                PlayerClass playerClass = new PlayerClass(id, displayName, description, icon, castingAbilities, requirements);
                classes.put(id.toLowerCase(), playerClass);  // stored in lower-case for case-insensitive lookup.
            }
            plugin.getLogger().info("Loaded " + classes.size() + " player classes.");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Error loading classes.yml", e);
        }
    }
    
    /**
     * Returns a PlayerClass for the given id, or null if not found.
     * @param id The class identifier.
     * @return The PlayerClass or null.
     */
    public PlayerClass getPlayerClass(String id) {
        if (id == null) return null;
        return classes.get(id.toLowerCase());
    }
    
    /**
     * Returns all loaded player classes.
     * @return Map of player classes.
     */
    public Map<String, PlayerClass> getAllClasses() {
        return classes;
    }
} 