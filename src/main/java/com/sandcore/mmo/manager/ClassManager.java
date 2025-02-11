package com.sandcore.mmo.manager;

import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public class ClassManager {

    private final Logger logger = Logger.getLogger(ClassManager.class.getName());
    // Stores class configurations loaded from the YAML file.
    private final Map<String, Map<String, Object>> classConfigs = new HashMap<>();
    // Example in-memory storage for player's current class.
    private final Map<UUID, String> playerClasses = new HashMap<>();

    /**
     * Loads the class configurations from the classes.yml file located in the mmoMinecraft resource folder.
     */
    public void loadClasses() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("mmoMinecraft/classes.yml")) {
            if (input == null) {
                logger.severe("Failed to load mmoMinecraft/classes.yml: file not found.");
                return;
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(new InputStreamReader(input, StandardCharsets.UTF_8));
            ConfigurationSection classesSection = config.getConfigurationSection("classes");
            if (classesSection == null) {
                logger.warning("No classes section found in classes.yml.");
                return;
            }
            for (String className : classesSection.getKeys(false)) {
                ConfigurationSection section = classesSection.getConfigurationSection(className);
                if (section != null) {
                    // Load all key-value pairs for the class.
                    Map<String, Object> classData = new HashMap<>(section.getValues(false));
                    classConfigs.put(className, classData);
                    logger.info("Loaded class configuration: " + className);
                }
            }
            logger.info("Successfully loaded " + classConfigs.size() + " classes.");
        } catch (Exception e) {
            logger.severe("Error loading classes.yml: " + e.getMessage());
        }
        FileConfiguration pluginConfig = ServiceRegistry.getPlugin().getConfig();
        logger.info("Reloaded class configurations");
    }

    /**
     * Returns the configuration map for the given class name.
     * 
     * @param className the name of the class to retrieve
     * @return a map of configuration values, or null if not found
     */
    public Map<String, Object> getClassConfig(String className) {
        Map<String, Object> config = classConfigs.get(className);
        if (config == null) {
            logger.warning("Requested class configuration for '" + className + "' not found.");
        }
        return config;
    }

    /**
     * Updates the player's class.
     * 
     * @param player the player to update
     * @param newClass the new class name
     * @return true if update succeeds; false if the new class does not exist.
     */
    public boolean updatePlayerClass(Player player, String newClass) {
        if (!classConfigs.containsKey(newClass)) {
            logger.warning("Attempted to update player " + player.getName() + " to unknown class: " + newClass);
            return false;
        }
        // For demonstration, we store the player's class update in an in-memory map.
        playerClasses.put(player.getUniqueId(), newClass);
        logger.info("Updated " + player.getName() + "'s class to " + newClass);
        return true;
    }

    /**
     * Updates the player's level in the class system.
     *
     * @param player the player whose level was updated.
     */
    public void updateLevel(Player player) {
        // For now: log the update. Expand this as needed to adjust the player's class data.
        logger.info("Updated level for player " + player.getName());
    }
} 