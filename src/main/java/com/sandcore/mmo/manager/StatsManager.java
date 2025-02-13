package com.sandcore.mmo.manager;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sandcore.mmo.util.ServiceRegistry;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * StatsManager handles all the player attribute values.
 *
 * <p>Attributes:
 * <ul>
 *   <li>maxHealth: Total hit points. (Base value + per level increment + allocated bonus)</li>
 *   <li>maxMana: Total mana available.</li>
 *   <li>healthRegen: Health regeneration rate.</li>
 *   <li>manaRegen: Mana regeneration rate.</li>
 *   <li>strength: Determines physical damage output.</li>
 *   <li>dexterity: Affects attack speed, critical chance, and evasion.</li>
 *   <li>intellect: Influences mana capacity and magical damage.</li>
 *   <li>defense: Reduces incoming physical damage.</li>
 *   <li>magicDefense: Reduces incoming magical damage.</li>
 * </ul>
 *
 * Attributes are computed as:
 * <pre>
 *  effective = base + (perLevel * playerLevel) + allocatedBonus
 * </pre>
 *
 * In addition, admin commands may override individual stat values.
 */
public class StatsManager {
    private static final Logger logger = Logger.getLogger(StatsManager.class.getName());

    // Base values and per-level increments – defaults can be overridden by configuration.
    public double baseMaxHealth = 100.0;
    public double perLevelMaxHealth = 10.0;

    public double baseMaxMana = 50.0;
    public double perLevelMaxMana = 5.0;

    public double baseHealthRegen = 2.0;
    public double perLevelHealthRegen = 0.5;

    public double baseManaRegen = 1.0;
    public double perLevelManaRegen = 0.5;

    public int baseStrength = 10;
    public int perLevelStrength = 1;

    public int baseDexterity = 10;
    public int perLevelDexterity = 1;

    public int baseIntellect = 10;
    public int perLevelIntellect = 1;

    public int baseDefense = 5;
    public int perLevelDefense = 1;

    public int baseMagicDefense = 5;
    public int perLevelMagicDefense = 1;

    /**
     * Inner class representing the bonus stat allocation that a player has earned
     * (for example, through leveling up or spending stat points).
     */
    public static class PlayerStatAllocation {
        public int bonusMaxHealth = 0;
        public int bonusMaxMana = 0;
        public double bonusHealthRegen = 0;
        public double bonusManaRegen = 0;
        public int bonusStrength = 0;
        public int bonusDexterity = 0;
        public int bonusIntellect = 0;
        public int bonusDefense = 0;
        public int bonusMagicDefense = 0;
        // Starting free stat points (could be increased by leveling up).
        public int freeStatPoints = 5;
        public int bonusHealth;
        public double bonusHealthRegen;
        public int bonusDefense;
        public int bonusMagicDefense;
        public int bonusDamage;
        public int bonusMagicDamage;
        public int bonusStamina;
        public double bonusStaminaRegen;
        public int bonusMagicRegen;
        public int bonusMana;
        public double bonusCritDamage;
        public double bonusMagicCritDamage;
        public double bonusCritChance;
        public double bonusSpeed;
        public double bonusLuck;
    }

    // Store each player's allocated stat points.
    private final Map<UUID, PlayerStatAllocation> allocations = new ConcurrentHashMap<>();

    /**
     * Returns the player's allocation record, creating it if needed.
     */
    public PlayerStatAllocation getAllocation(Player player) {
        return allocations.computeIfAbsent(player.getUniqueId(), id -> new PlayerStatAllocation());
    }

    // In-memory overrides set by admin commands: Map of player UUID string to a map of <stat, value>
    private final Map<String, Map<String, Double>> statOverrides = new ConcurrentHashMap<>();

    private Map<String, Double> getOverrides(Player player) {
        return statOverrides.computeIfAbsent(player.getUniqueId().toString(), k -> new ConcurrentHashMap<>());
    }

    /**
     * If an admin override is set, return that value; otherwise, return NaN.
     */
    public double getOverride(Player player, String stat) {
        Map<String, Double> overrides = getOverrides(player);
        return overrides.getOrDefault(stat, Double.NaN);
    }

    /**
     * Sets an admin override for the given stat.
     */
    public void setStat(Player player, String stat, double value) {
        Map<String, Double> overrides = getOverrides(player);
        overrides.put(stat, value);
        logger.info("setStat: Set " + stat + " for " + player.getName() + " to " + value);
    }

    /**
     * Adds a value to the given stat. If no override exists, the current effective value is computed.
     */
    public void addStat(Player player, String stat, double value) {
        Map<String, Double> overrides = getOverrides(player);
        double current = overrides.getOrDefault(stat, Double.NaN);
        if (Double.isNaN(current)) {
            current = recalcEffectiveAttribute(player, stat);
        }
        double newValue = current + value;
        overrides.put(stat, newValue);
        logger.info("addStat: Added " + value + " to " + stat + " for " + player.getName() + ". New value: " + newValue);
    }

    /**
     * Recalculates and returns the effective value for a given stat.
     * The formula is:
     *   effective = base + (perLevel * level) + allocatedBonus
     *
     * <p>For simplicity, this example assumes player level is 1 (replace with actual level retrieval).
     */
    public double recalcEffectiveAttribute(Player player, String stat) {
        // TODO: Replace with proper level retrieval (e.g., from XPManager)
        int level = 1;
        PlayerStatAllocation alloc = getAllocation(player);
        double effective;
        switch (stat) {
            case "maxHealth":
                effective = baseMaxHealth + perLevelMaxHealth * level + alloc.bonusMaxHealth;
                break;
            case "maxMana":
                effective = baseMaxMana + perLevelMaxMana * level + alloc.bonusMaxMana;
                break;
            case "healthRegen":
                effective = baseHealthRegen + perLevelHealthRegen * level + alloc.bonusHealthRegen;
                break;
            case "manaRegen":
                effective = baseManaRegen + perLevelManaRegen * level + alloc.bonusManaRegen;
                break;
            case "strength":
                effective = baseStrength + perLevelStrength * level + alloc.bonusStrength;
                break;
            case "dexterity":
                effective = baseDexterity + perLevelDexterity * level + alloc.bonusDexterity;
                break;
            case "intellect":
                effective = baseIntellect + perLevelIntellect * level + alloc.bonusIntellect;
                break;
            case "defense":
                effective = baseDefense + perLevelDefense * level + alloc.bonusDefense;
                break;
            case "magicDefense":
                effective = baseMagicDefense + perLevelMagicDefense * level + alloc.bonusMagicDefense;
                break;
            default:
                effective = 0;
        }
        double overrideValue = getOverride(player, stat);
        if (!Double.isNaN(overrideValue)) {
            return overrideValue;
        }
        return effective;
    }

    /**
     * Recalculates stats for the player. (You may expand this to save calculated values if desired.)
     */
    public void recalculateStats(Player player) {
        // In this implementation, effective values are computed on-demand.
        logger.info("Recalculating stats for player " + player.getName());
    }

    /**
     * Loads configuration values from stats.yml.
     * The expected structure is:
     * <pre>
     * base:
     *   maxHealth: ...
     *   maxMana: ...
     *   healthRegen: ...
     *   manaRegen: ...
     *   strength: ...
     *   dexterity: ...
     *   intellect: ...
     *   defense: ...
     *   magicDefense: ...
     * increment:
     *   maxHealth: ...
     *   maxMana: ...
     *   healthRegen: ...
     *   manaRegen: ...
     *   strength: ...
     *   dexterity: ...
     *   intellect: ...
     *   defense: ...
     *   magicDefense: ...
     * </pre>
     */
    public void loadConfiguration(JavaPlugin plugin) {
        try {
            File file = new File(plugin.getDataFolder(), "stats.yml");
            YamlConfiguration config;
            if (file.exists()) {
                config = YamlConfiguration.loadConfiguration(file);
            } else {
                try (InputStream is = plugin.getResource("stats.yml")) {
                    config = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
                }
            }
            baseMaxHealth = config.getDouble("base.maxHealth", baseMaxHealth);
            perLevelMaxHealth = config.getDouble("increment.maxHealth", perLevelMaxHealth);

            baseMaxMana = config.getDouble("base.maxMana", baseMaxMana);
            perLevelMaxMana = config.getDouble("increment.maxMana", perLevelMaxMana);

            baseHealthRegen = config.getDouble("base.healthRegen", baseHealthRegen);
            perLevelHealthRegen = config.getDouble("increment.healthRegen", perLevelHealthRegen);

            baseManaRegen = config.getDouble("base.manaRegen", baseManaRegen);
            perLevelManaRegen = config.getDouble("increment.manaRegen", perLevelManaRegen);

            baseStrength = config.getInt("base.strength", baseStrength);
            perLevelStrength = config.getInt("increment.strength", perLevelStrength);

            baseDexterity = config.getInt("base.dexterity", baseDexterity);
            perLevelDexterity = config.getInt("increment.dexterity", perLevelDexterity);

            baseIntellect = config.getInt("base.intellect", baseIntellect);
            perLevelIntellect = config.getInt("increment.intellect", perLevelIntellect);

            baseDefense = config.getInt("base.defense", baseDefense);
            perLevelDefense = config.getInt("increment.defense", perLevelDefense);

            baseMagicDefense = config.getInt("base.magicDefense", baseMagicDefense);
            perLevelMagicDefense = config.getInt("increment.magicDefense", perLevelMagicDefense);

            logger.info("StatsManager: Loaded configuration for player attributes.");
        } catch (Exception e) {
            Bukkit.getLogger().severe("Error loading stats configuration: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Returns the available free stat points for the given player.
     */
    public int getAvailablePoints(Player player) {
        return getAllocation(player).freeStatPoints;
    }

    /**
     * Returns the effective stat value for the given player and attribute.
     */
    public double getStatValue(Player player, String stat) {
        return recalcEffectiveAttribute(player, stat);
    }

    /**
     * Returns the base value for the given stat.
     * For example, for "maxHealth" it returns baseMaxHealth.
     */
    public double getBaseStat(Player player, String stat) {
        switch (stat) {
            case "maxHealth":    return baseMaxHealth;
            case "maxMana":      return baseMaxMana;
            case "healthRegen":  return baseHealthRegen;
            case "manaRegen":    return baseManaRegen;
            case "strength":     return baseStrength;
            case "dexterity":    return baseDexterity;
            case "intellect":    return baseIntellect;
            case "defense":      return baseDefense;
            case "magicDefense": return baseMagicDefense;
            default:             return 0;
        }
    }

    /**
     * Returns the per-level increment for the given stat.
     */
    public double getPerLevelIncrement(Player player, String stat) {
        switch (stat) {
            case "maxHealth":    return perLevelMaxHealth;
            case "maxMana":      return perLevelMaxMana;
            case "healthRegen":  return perLevelHealthRegen;
            case "manaRegen":    return perLevelManaRegen;
            case "strength":     return perLevelStrength;
            case "dexterity":    return perLevelDexterity;
            case "intellect":    return perLevelIntellect;
            case "defense":      return perLevelDefense;
            case "magicDefense": return perLevelMagicDefense;
            default:             return 0;
        }
    }

    /**
     * Reloads the stats configuration by re-reading the stats.yml file.
     */
    public void reloadStats() {
        JavaPlugin plugin = ServiceRegistry.getPlugin();
        if (plugin != null) {
            loadConfiguration(plugin);
        }
    }
} 