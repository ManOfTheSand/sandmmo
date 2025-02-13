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
        // New bonus stats based on the advanced stat system.
        private final double bonusHealth;
        private final double bonusHealthRegen;
        private final double bonusDefense;
        private final double bonusMagicDefence;
        private final double bonusDamage;
        private final double bonusMagicDamage;
        private final double bonusStamina;
        private final double bonusStaminaRegen;
        private final double bonusMagicRegen;
        private final double bonusMana;
        private final double bonusCritDamage;
        private final double bonusMagicCritDamage;
        private final double bonusCritChance;
        private final double bonusSpeed;
        private final double bonusLuck;
        private final java.util.Map<String, String> keyCombos;
        private final String castingSoundName;
        private final float castingSoundVolume;
        private final float castingSoundPitch;
        // New fields for additional sounds
        private final String comboClickSoundName;
        private final float comboClickSoundVolume;
        private final float comboClickSoundPitch;
        private final String comboFailSoundName;
        private final float comboFailSoundVolume;
        private final float comboFailSoundPitch;

        /**
         * Constructs a new PlayerClass.
         *
         * @param id                the class identifier
         * @param displayName       the display name for GUIs
         * @param description       a short description or lore
         * @param bonusHealth       bonus health
         * @param bonusHealthRegen   bonus health regeneration
         * @param bonusDefense       bonus defense
         * @param bonusMagicDefence  bonus magic defense
         * @param bonusDamage       bonus damage
         * @param bonusMagicDamage  bonus magic damage
         * @param bonusStamina      bonus stamina
         * @param bonusStaminaRegen  bonus stamina regeneration
         * @param bonusMagicRegen    bonus magic regeneration
         * @param bonusMana          bonus mana
         * @param bonusCritDamage    bonus critical damage
         * @param bonusMagicCritDamage bonus magic critical damage
         * @param bonusCritChance    bonus critical chance
         * @param bonusSpeed         bonus speed
         * @param bonusLuck          bonus luck
         * @param keyCombos         key combos for the class
         * @param castingSoundName  casting sound name for the class
         * @param castingSoundVolume casting sound volume for the class
         * @param castingSoundPitch casting sound pitch for the class
         * @param comboClickSoundName combo click sound name for the class
         * @param comboClickSoundVolume combo click sound volume for the class
         * @param comboClickSoundPitch combo click sound pitch for the class
         * @param comboFailSoundName combo fail sound name for the class
         * @param comboFailSoundVolume combo fail sound volume for the class
         * @param comboFailSoundPitch combo fail sound pitch for the class
         */
        public PlayerClass(String id, String displayName, String description,
                           double bonusHealth, double bonusHealthRegen, double bonusDefense, double bonusMagicDefence,
                           double bonusDamage, double bonusMagicDamage, double bonusStamina, double bonusStaminaRegen,
                           double bonusMagicRegen, double bonusMana, double bonusCritDamage, double bonusMagicCritDamage,
                           double bonusCritChance, double bonusSpeed, double bonusLuck,
                           java.util.Map<String, String> keyCombos,
                           String castingSoundName, float castingSoundVolume, float castingSoundPitch,
                           String comboClickSoundName, float comboClickSoundVolume, float comboClickSoundPitch,
                           String comboFailSoundName, float comboFailSoundVolume, float comboFailSoundPitch) {
            this.id = id;
            this.displayName = displayName;
            this.description = description;
            this.bonusHealth = bonusHealth;
            this.bonusHealthRegen = bonusHealthRegen;
            this.bonusDefense = bonusDefense;
            this.bonusMagicDefence = bonusMagicDefence;
            this.bonusDamage = bonusDamage;
            this.bonusMagicDamage = bonusMagicDamage;
            this.bonusStamina = bonusStamina;
            this.bonusStaminaRegen = bonusStaminaRegen;
            this.bonusMagicRegen = bonusMagicRegen;
            this.bonusMana = bonusMana;
            this.bonusCritDamage = bonusCritDamage;
            this.bonusMagicCritDamage = bonusMagicCritDamage;
            this.bonusCritChance = bonusCritChance;
            this.bonusSpeed = bonusSpeed;
            this.bonusLuck = bonusLuck;
            this.keyCombos = keyCombos;
            this.castingSoundName = castingSoundName;
            this.castingSoundVolume = castingSoundVolume;
            this.castingSoundPitch = castingSoundPitch;
            this.comboClickSoundName = comboClickSoundName;
            this.comboClickSoundVolume = comboClickSoundVolume;
            this.comboClickSoundPitch = comboClickSoundPitch;
            this.comboFailSoundName = comboFailSoundName;
            this.comboFailSoundVolume = comboFailSoundVolume;
            this.comboFailSoundPitch = comboFailSoundPitch;
        }

        // Getters for class properties.
        public String getId() { return id; }
        public String getDisplayName() { return displayName; }
        public String getDescription() { return description; }
        public double getBonusHealth() { return bonusHealth; }
        public double getBonusHealthRegen() { return bonusHealthRegen; }
        public double getBonusDefense() { return bonusDefense; }
        public double getBonusMagicDefence() { return bonusMagicDefence; }
        public double getBonusDamage() { return bonusDamage; }
        public double getBonusMagicDamage() { return bonusMagicDamage; }
        public double getBonusStamina() { return bonusStamina; }
        public double getBonusStaminaRegen() { return bonusStaminaRegen; }
        public double getBonusMagicRegen() { return bonusMagicRegen; }
        public double getBonusMana() { return bonusMana; }
        public double getBonusCritDamage() { return bonusCritDamage; }
        public double getBonusMagicCritDamage() { return bonusMagicCritDamage; }
        public double getBonusCritChance() { return bonusCritChance; }
        public double getBonusSpeed() { return bonusSpeed; }
        public double getBonusLuck() { return bonusLuck; }
        public java.util.Map<String, String> getKeyCombos() { return keyCombos; }
        public String getCastingSoundName() { return castingSoundName; }
        public float getCastingSoundVolume() { return castingSoundVolume; }
        public float getCastingSoundPitch() { return castingSoundPitch; }
        public String getComboClickSoundName() { return comboClickSoundName; }
        public float getComboClickSoundVolume() { return comboClickSoundVolume; }
        public float getComboClickSoundPitch() { return comboClickSoundPitch; }
        public String getComboFailSoundName() { return comboFailSoundName; }
        public float getComboFailSoundVolume() { return comboFailSoundVolume; }
        public float getComboFailSoundPitch() { return comboFailSoundPitch; }
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
                    // Read the new advanced stat values from the "startingStats" section.
                    double bonusHealth = config.getDouble("classes." + id + ".startingStats.Health", 0);
                    double bonusHealthRegen = config.getDouble("classes." + id + ".startingStats.HealthRegen", 0);
                    double bonusDefense = config.getDouble("classes." + id + ".startingStats.Defense", 0);
                    double bonusMagicDefence = config.getDouble("classes." + id + ".startingStats.MagicDefence", 0);
                    double bonusDamage = config.getDouble("classes." + id + ".startingStats.Damage", 0);
                    double bonusMagicDamage = config.getDouble("classes." + id + ".startingStats.MagicDamage", 0);
                    double bonusStamina = config.getDouble("classes." + id + ".startingStats.Stamina", 0);
                    double bonusStaminaRegen = config.getDouble("classes." + id + ".startingStats.StaminaRegen", 0);
                    double bonusMagicRegen = config.getDouble("classes." + id + ".startingStats.MagicRegen", 0);
                    double bonusMana = config.getDouble("classes." + id + ".startingStats.Mana", 0);
                    double bonusCritDamage = config.getDouble("classes." + id + ".startingStats.CritDamage", 0);
                    double bonusMagicCritDamage = config.getDouble("classes." + id + ".startingStats.MagicCritDamage", 0);
                    double bonusCritChance = config.getDouble("classes." + id + ".startingStats.CritChance", 0);
                    double bonusSpeed = config.getDouble("classes." + id + ".startingStats.Speed", 0);
                    double bonusLuck = config.getDouble("classes." + id + ".startingStats.Luck", 0);
                    Map<String, String> keyCombos = new HashMap<>();
                    if (config.isConfigurationSection("classes." + id + ".keyCombos")) {
                        for (String key : config.getConfigurationSection("classes." + id + ".keyCombos").getKeys(false)) {
                            keyCombos.put(key.toUpperCase(), config.getString("classes." + id + ".keyCombos." + key));
                        }
                    }

                    String castingSoundName = config.getString("classes." + id + ".castingSound.name", "BLOCK_NOTE_BLOCK_PLING");
                    float castingSoundVolume = (float) config.getDouble("classes." + id + ".castingSound.volume", 1.0);
                    float castingSoundPitch = (float) config.getDouble("classes." + id + ".castingSound.pitch", 1.0);

                    String comboClickSoundName = config.getString("classes." + id + ".comboClickSound.name", "ENTITY_EXPERIENCE_ORB_PICKUP");
                    float comboClickSoundVolume = (float) config.getDouble("classes." + id + ".comboClickSound.volume", 1.0);
                    float comboClickSoundPitch = (float) config.getDouble("classes." + id + ".comboClickSound.pitch", 1.0);

                    String comboFailSoundName = config.getString("classes." + id + ".comboFailSound.name", "ENTITY_VILLAGER_NO");
                    float comboFailSoundVolume = (float) config.getDouble("classes." + id + ".comboFailSound.volume", 1.0);
                    float comboFailSoundPitch = (float) config.getDouble("classes." + id + ".comboFailSound.pitch", 1.0);

                    PlayerClass playerClass = new PlayerClass(id, displayName, description,
                            bonusHealth, bonusHealthRegen, bonusDefense, bonusMagicDefence,
                            bonusDamage, bonusMagicDamage, bonusStamina, bonusStaminaRegen,
                            bonusMagicRegen, bonusMana, bonusCritDamage, bonusMagicCritDamage,
                            bonusCritChance, bonusSpeed, bonusLuck,
                            keyCombos, 
                            castingSoundName, castingSoundVolume, castingSoundPitch,
                            comboClickSoundName, comboClickSoundVolume, comboClickSoundPitch,
                            comboFailSoundName, comboFailSoundVolume, comboFailSoundPitch);
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
        // Save player's class selection to persistent storage.
        savePlayerClass(player, classId);
        // Integrate with StatsManager: update player's bonus attributes.
        StatsManager statsManager = ServiceRegistry.getStatsManager();
        if (statsManager != null) {
            PlayerStatAllocation alloc = statsManager.getAllocation(player);
            // Update bonus attributes from the selected class.
            alloc.bonusHealth = (int) chosenClass.getBonusHealth();
            alloc.bonusHealthRegen = chosenClass.getBonusHealthRegen();
            alloc.bonusDefense = (int) chosenClass.getBonusDefense();
            alloc.bonusMagicDefense = (int) chosenClass.getBonusMagicDefence();
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

    private void savePlayerClass(Player player, String classId) {
        try {
            File file = new File(plugin.getDataFolder(), "players.yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set("players." + player.getUniqueId().toString(), classId);
            config.save(file);
            logger.info("ClassManager: Saved class " + classId + " for player " + player.getName());
        } catch (Exception e) {
            logger.severe("ClassManager: Could not save class for player " + player.getName() + ": " + e.getMessage());
        }
    }
} 