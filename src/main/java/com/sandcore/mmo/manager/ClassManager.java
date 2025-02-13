package com.sandcore.mmo.manager;

import com.sandcore.mmo.SandCoreMain;
import com.sandcore.mmo.manager.StatsManager.PlayerStatAllocation;
import com.sandcore.mmo.manager.PlayerStatsApplier;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * ClassManager handles the loading, storage, and retrieval of player class definitions.
 *
 * <p>It loads class definitions from a YAML file (classes.yml) located in the resources folder.
 * Each definition includes:
 * <ul>
 *   <li><b>Class Identifier:</b> A unique string such as "warrior" or "mage".</li>
 *   <li><b>Display Name:</b> Shown in the GUI (e.g., "Warrior").</li>
 *   <li><b>Description/Lore:</b> Extra text describing the class.</li>
 *   <li><b>Bonus Attributes:</b> Additional modifiers (bonus maxHealth, bonus strength, bonus defense,
 *       bonus magicDefense, bonus mana, etc.) that are applied on top of the player's base stats.</li>
 * </ul>
 *
 * <p>When a player's class is set via {@code setPlayerClass()}, the bonus modifiers are copied into
 * the player's StatsManager allocation record. Then, {@code PlayerStatsApplier.applyStats(Player)} is
 * called to reapply all effective stats in-game.
 *
 * <p>Example YAML structure for classes.yml:
 * <pre>
 * classes:
 *   warrior:
 *     displayName: "Warrior"
 *     description: "A tough melee fighter who excels in close combat."
 *     bonusAttributes:
 *       maxHealth: 20
 *       maxMana: 0
 *       healthRegen: 1
 *       manaRegen: 0
 *       strength: 5
 *       dexterity: 2
 *       intellect: 0
 *       defense: 3
 *       magicDefense: 0
 *   mage:
 *     displayName: "Mage"
 *     description: "A master of the arcane arts, wielding powerful magic."
 *     bonusAttributes:
 *       maxHealth: 0
 *       maxMana: 30
 *       healthRegen: 0
 *       manaRegen: 2
 *       strength: 0
 *       dexterity: 0
 *       intellect: 5
 *       defense: 0
 *       magicDefense: 2
 * </pre>
 */
public class ClassManager {
    private static final Logger logger = Logger.getLogger(ClassManager.class.getName());
    // Holds all loaded class definitions indexed by their lowercase class ID.
    private final Map<String, PlayerClass> classes = new HashMap<>();
    // Holds the current class selection for each player (indexed by player UUID).
    private final Map<UUID, PlayerClass> playerClasses = new HashMap<>();
    private final JavaPlugin plugin;

    /**
     * Constructor. Loads the classes from the provided plugin instance.
     *
     * @param plugin the plugin instance
     */
    public ClassManager(JavaPlugin plugin) {
        this.plugin = plugin;
        loadClasses();
    }

    /**
     * Inner class representing a player's class definition.
     */
    public static class PlayerClass {
        private final String id;            // Unique identifier (e.g., "warrior")
        private final String displayName;   // Displayed name (e.g., "Warrior")
        private final String description;   // Description or lore
        // Bonus attributes provided by this class.
        private final double bonusMaxHealth;    // Additional max health provided.
        private final double bonusMaxMana;      // Additional max mana provided.
        private final double bonusHealthRegen;  // Additional health regeneration.
        private final double bonusManaRegen;    // Additional mana regeneration.
        private final int bonusStrength;        // Additional strength.
        private final int bonusDexterity;       // Additional dexterity.
        private final int bonusIntellect;       // Additional intellect.
        private final int bonusDefense;         // Additional defense.
        private final int bonusMagicDefense;    // Additional magic defense.
        private final Map<String, Integer> startingStats;

        /**
         * Constructs a new PlayerClass.
         *
         * @param id                the class identifier
         * @param displayName       the display name for GUIs
         * @param description       a short description or lore
         * @param bonusMaxHealth    additional max health bonus
         * @param bonusMaxMana      additional max mana bonus
         * @param bonusHealthRegen  additional health regeneration bonus
         * @param bonusManaRegen    additional mana regeneration bonus
         * @param bonusStrength     additional strength bonus
         * @param bonusDexterity    additional dexterity bonus
         * @param bonusIntellect    additional intellect bonus
         * @param bonusDefense      additional defense bonus
         * @param bonusMagicDefense additional magic defense bonus
         * @param startingStats     starting stats for the class
         */
        public PlayerClass(String id, String displayName, String description,
                           double bonusMaxHealth, double bonusMaxMana, double bonusHealthRegen,
                           double bonusManaRegen, int bonusStrength, int bonusDexterity,
                           int bonusIntellect, int bonusDefense, int bonusMagicDefense,
                           Map<String, Integer> startingStats) {
            this.id = id;
            this.displayName = displayName;
            this.description = description;
            this.bonusMaxHealth = bonusMaxHealth;
            this.bonusMaxMana = bonusMaxMana;
            this.bonusHealthRegen = bonusHealthRegen;
            this.bonusManaRegen = bonusManaRegen;
            this.bonusStrength = bonusStrength;
            this.bonusDexterity = bonusDexterity;
            this.bonusIntellect = bonusIntellect;
            this.bonusDefense = bonusDefense;
            this.bonusMagicDefense = bonusMagicDefense;
            this.startingStats = startingStats;
        }

        // Getters for class properties.
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public double getBonusMaxHealth() { return bonusMaxHealth; }
        public double getBonusMaxMana() { return bonusMaxMana; }
        public double getBonusHealthRegen() { return bonusHealthRegen; }
        public double getBonusManaRegen() { return bonusManaRegen; }
        public int getBonusStrength() { return bonusStrength; }
        public int getBonusDexterity() { return bonusDexterity; }
        public int getBonusIntellect() { return bonusIntellect; }
        public int getBonusDefense() { return bonusDefense; }
        public int getBonusMagicDefense() { return bonusMagicDefense; }
        public Map<String, Integer> getStartingStats() { return startingStats; }
    }

    /**
     * Loads class definitions from the YAML file "classes.yml".
     * The file is first looked for in the plugin's data folder; if not present,
     * it is loaded from the plugin resources.
     */
    public void loadClasses() {
        try {
            File file = new File(plugin.getDataFolder(), "classes.yml");
            YamlConfiguration config;
            if (file.exists()) {
                config = YamlConfiguration.loadConfiguration(file);
            } else {
                try (InputStream is = plugin.getResource("classes.yml")) {
                    config = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
                }
            }
            classes.clear();
            if (config.contains("classes")) {
                for (String id : config.getConfigurationSection("classes").getKeys(false)) {
                    String displayName = config.getString("classes." + id + ".displayName", id);
                    String description = config.getString("classes." + id + ".description", "");
                    double bonusMaxHealth = config.getDouble("classes." + id + ".bonusAttributes.maxHealth", 0);
                    double bonusMaxMana = config.getDouble("classes." + id + ".bonusAttributes.maxMana", 0);
                    double bonusHealthRegen = config.getDouble("classes." + id + ".bonusAttributes.healthRegen", 0);
                    double bonusManaRegen = config.getDouble("classes." + id + ".bonusAttributes.manaRegen", 0);
                    int bonusStrength = config.getInt("classes." + id + ".bonusAttributes.strength", 0);
                    int bonusDexterity = config.getInt("classes." + id + ".bonusAttributes.dexterity", 0);
                    int bonusIntellect = config.getInt("classes." + id + ".bonusAttributes.intellect", 0);
                    int bonusDefense = config.getInt("classes." + id + ".bonusAttributes.defense", 0);
                    int bonusMagicDefense = config.getInt("classes." + id + ".bonusAttributes.magicDefense", 0);
                    Map<String, Integer> startingStats = new HashMap<>();
                    if (config.isConfigurationSection("classes." + id + ".startingStats")) {
                        for (String stat : config.getConfigurationSection("classes." + id + ".startingStats").getKeys(false)) {
                            startingStats.put(stat, config.getInt("classes." + id + ".startingStats." + stat));
                        }
                    }
                    PlayerClass playerClass = new PlayerClass(id, displayName, description,
                            bonusMaxHealth, bonusMaxMana, bonusHealthRegen, bonusManaRegen,
                            bonusStrength, bonusDexterity, bonusIntellect, bonusDefense, bonusMagicDefense,
                            startingStats);
                    classes.put(id.toLowerCase(), playerClass);
                }
                logger.info("ClassManager: Loaded " + classes.size() + " class definitions.");
            } else {
                logger.warning("ClassManager: No class definitions found in classes.yml");
            }
        } catch (Exception e) {
            logger.severe("ClassManager: Error loading class definitions: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the player's currently selected class.
     *
     * @param player the target player
     * @return the PlayerClass representing the player's class, or null if not set
     */
    public PlayerClass getPlayerClass(Player player) {
        return playerClasses.get(player.getUniqueId());
    }

    /**
     * Sets the player's class by class identifier. When set, the corresponding bonus attributes
     * are applied to the player's stats, and the in-game attributes are updated.
     *
     * @param player  the target player
     * @param classId the class identifier to set (case-insensitive)
     * @return true if the class was set successfully; false if the classId does not exist
     */
    public boolean setPlayerClass(Player player, String classId) {
        classId = classId.toLowerCase();
        if (!classes.containsKey(classId)) {
            logger.warning("ClassManager: Invalid class ID '" + classId + "' for player " + player.getName());
            return false;
        }
        PlayerClass chosenClass = classes.get(classId);
        playerClasses.put(player.getUniqueId(), chosenClass);
        logger.info("ClassManager: Set " + player.getName() + "'s class to " + chosenClass.getDisplayName());
        // Integrate with StatsManager: update player's bonus attributes.
        StatsManager statsManager = ServiceRegistry.getStatsManager();
        if (statsManager != null) {
            PlayerStatAllocation alloc = statsManager.getAllocation(player);
            // Update bonus attributes from the selected class.
            alloc.bonusMaxHealth = (int) chosenClass.getBonusMaxHealth();
            alloc.bonusMaxMana = (int) chosenClass.getBonusMaxMana();
            alloc.bonusHealthRegen = chosenClass.getBonusHealthRegen();
            alloc.bonusManaRegen = chosenClass.getBonusManaRegen();
            alloc.bonusStrength = chosenClass.getBonusStrength();
            alloc.bonusDexterity = chosenClass.getBonusDexterity();
            alloc.bonusIntellect = chosenClass.getBonusIntellect();
            alloc.bonusDefense = chosenClass.getBonusDefense();
            alloc.bonusMagicDefense = chosenClass.getBonusMagicDefense();
        }
        // Update in-game attributes by re-applying player stats.
        PlayerStatsApplier.applyStats(player);
        return true;
    }

    /**
     * Allows an administrator to forcefully set a player's class.
     *
     * @param target  the target player
     * @param classId the class identifier to set
     * @return true if the class was set successfully; false otherwise
     */
    public boolean adminSetClass(Player target, String classId) {
        boolean result = setPlayerClass(target, classId);
        if (result) {
            logger.info("ClassManager (Admin): " + target.getName() + " class set to '" + classId + "'");
        }
        return result;
    }

    /**
     * Returns a new map containing all available class definitions.
     *
     * @return Map of classId to PlayerClass.
     */
    public Map<String, PlayerClass> getAvailableClasses() {
        return new HashMap<>(classes);
    }

    /**
     * Dummy updateLevel method to resolve XPManager dependency.
     * This method can be expanded to update class-specific level logic.
     *
     * @param player the player whose level is updated.
     */
    public void updateLevel(Player player) {
        logger.info("ClassManager: updateLevel called for " + player.getName());
    }

    /**
     * This method can be called on plugin reload.
     */
    public void reloadClasses() {
        loadClasses();
    }
} 