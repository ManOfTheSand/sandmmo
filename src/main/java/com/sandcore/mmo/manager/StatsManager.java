package com.sandcore.mmo.manager;

import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class StatsManager {
    private final Logger logger = Logger.getLogger(StatsManager.class.getName());

    // Base values
    public static final double BASE_MAX_HEALTH = 100.0;
    public static final double BASE_MAX_MANA = 50.0;
    public static final double BASE_HEALTH_REGEN = 2.0;
    public static final double BASE_MANA_REGEN = 1.0;
    public static final int BASE_STRENGTH = 10;
    public static final int BASE_DEXTERITY = 10;
    public static final int BASE_INTELLECT = 10;

    // Per-level increments
    public static final double INCREMENT_MAX_HEALTH = 10.0;
    public static final double INCREMENT_MAX_MANA = 5.0;
    public static final double INCREMENT_HEALTH_REGEN = 0.5;
    public static final double INCREMENT_MANA_REGEN = 0.5;
    public static final int INCREMENT_STRENGTH = 1;
    public static final int INCREMENT_DEXTERITY = 1;
    public static final int INCREMENT_INTELLECT = 1;

    // Internal container for a player's allocated bonus and free points
    public static class StatsData {
        public int bonusMaxHealth = 0;
        public int bonusMaxMana = 0;
        public int bonusHealthRegen = 0;
        public int bonusManaRegen = 0;
        public int bonusStrength = 0;
        public int bonusDexterity = 0;
        public int bonusIntellect = 0;
        // Starting free points – adjust as necessary.
        public int freeStatPoints = 5;
    }

    private final Map<UUID, StatsData> statsData = new ConcurrentHashMap<>();

    // Retrieve or initialize data for a player.
    public StatsData getPlayerStats(UUID uuid) {
        return statsData.computeIfAbsent(uuid, k -> new StatsData());
    }

    // Total attribute calculations based on player's level and allocated bonuses.
    public double getTotalMaxHealth(Player player, int level) {
        StatsData data = getPlayerStats(player.getUniqueId());
        return BASE_MAX_HEALTH + (INCREMENT_MAX_HEALTH * level) + data.bonusMaxHealth;
    }

    public double getTotalMaxMana(Player player, int level) {
        StatsData data = getPlayerStats(player.getUniqueId());
        return BASE_MAX_MANA + (INCREMENT_MAX_MANA * level) + data.bonusMaxMana;
    }

    public double getTotalHealthRegen(Player player, int level) {
        StatsData data = getPlayerStats(player.getUniqueId());
        return BASE_HEALTH_REGEN + (INCREMENT_HEALTH_REGEN * level) + data.bonusHealthRegen;
    }

    public double getTotalManaRegen(Player player, int level) {
        StatsData data = getPlayerStats(player.getUniqueId());
        return BASE_MANA_REGEN + (INCREMENT_MANA_REGEN * level) + data.bonusManaRegen;
    }

    public int getTotalStrength(Player player, int level) {
        StatsData data = getPlayerStats(player.getUniqueId());
        return BASE_STRENGTH + (INCREMENT_STRENGTH * level) + data.bonusStrength;
    }

    public int getTotalDexterity(Player player, int level) {
        StatsData data = getPlayerStats(player.getUniqueId());
        return BASE_DEXTERITY + (INCREMENT_DEXTERITY * level) + data.bonusDexterity;
    }

    public int getTotalIntellect(Player player, int level) {
        StatsData data = getPlayerStats(player.getUniqueId());
        return BASE_INTELLECT + (INCREMENT_INTELLECT * level) + data.bonusIntellect;
    }

    // Allocates a free stat point to a specified attribute.
    // Allowed attribute strings: "maxhealth", "maxmana", "healthregen", "manaregen", "strength", "dexterity", "intellect"
    public boolean allocateStatPoint(Player player, String attribute) {
        StatsData data = getPlayerStats(player.getUniqueId());
        if (data.freeStatPoints <= 0) {
            logger.warning("Player " + player.getName() + " has no free stat points available.");
            return false;
        }
        switch (attribute.toLowerCase()) {
            case "maxhealth":
                data.bonusMaxHealth++;
                break;
            case "maxmana":
                data.bonusMaxMana++;
                break;
            case "healthregen":
                data.bonusHealthRegen++;
                break;
            case "manaregen":
                data.bonusManaRegen++;
                break;
            case "strength":
                data.bonusStrength++;
                break;
            case "dexterity":
                data.bonusDexterity++;
                break;
            case "intellect":
                data.bonusIntellect++;
                break;
            default:
                logger.warning("Unknown attribute: " + attribute);
                return false;
        }
        data.freeStatPoints--;
        logger.info("Allocated 1 point to " + attribute + " for player " + player.getName());
        return true;
    }

    public void reloadStats() {
        statsData.clear();
        // Add any additional reload logic needed
    }

    // Returns the available stat points for a player.
    public int getAvailablePoints(Player player) {
        // Dummy logic: Replace with your actual implementation.
        return 5;
    }
    
    // Returns the computed stat value for a given stat for the player.
    public double getStatValue(Player player, String stat) {
        // Dummy logic: Replace with your calculation.
        return 100.0;
    }
    
    // Returns the base value for the given stat.
    public double getBaseStat(Player player, String stat) {
        // Dummy logic: Replace with your calculation.
        return 50.0;
    }
    
    // Returns the per-level increment for the given stat.
    public double getPerLevelIncrement(Player player, String stat) {
        // Dummy logic: Replace with your calculation.
        return 5.0;
    }
} 