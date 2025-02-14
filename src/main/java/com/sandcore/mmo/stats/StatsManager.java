package com.sandcore.mmo.stats;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * StatsManager handles player stat calculations.
 * For demonstration, each stat value is computed using a base value plus a component derived from the player's level.
 */
public class StatsManager {
    private static final Logger logger = Logger.getLogger(StatsManager.class.getName());
    private final Map<UUID, PlayerStatAllocation> allocations = new HashMap<>();

    /**
     * Calculates and returns the effective stat value for a given stat key.
     * Example formulas:
     * - maxHealth = 100 + level * 10
     * - maxMana = 50 + level * 5
     * - healthRegen = 1 + level * 0.1
     * - manaRegen = 0.5 + level * 0.1
     * - strength = 10 + level * 2
     * - dexterity = 10 + level * 1.5
     * - defense = 5 + level * 1.2
     * - magicDamage = 15 + level * 2.5
     *
     * @param player the player
     * @param statKey stat identifier
     * @return computed stat value
     */
    public double getStatValue(Player player, String statKey) {
        PlayerStatAllocation allocation = getAllocation(player);
        int level = allocation.level;
        switch (statKey) {
            case "maxHealth":
                return 100 + level * 10;
            case "maxMana":
                return 50 + level * 5;
            case "healthRegen":
                return 1 + level * 0.1;
            case "manaRegen":
                return 0.5 + level * 0.1;
            case "strength":
                return 10 + level * 2;
            case "dexterity":
                return 10 + level * 1.5;
            case "defense":
                return 5 + level * 1.2;
            case "magicDamage":
                return 15 + level * 2.5;
            default:
                return 0;
        }
    }

    /**
     * Retrieves the stat allocation for a player.
     * If no allocation exists, a default is created (level 1, 5 free stat points).
     *
     * @param player the player
     * @return the player's allocation record
     */
    public PlayerStatAllocation getAllocation(Player player) {
        UUID uuid = player.getUniqueId();
        if (!allocations.containsKey(uuid)) {
            allocations.put(uuid, new PlayerStatAllocation(1, 5));
        }
        return allocations.get(uuid);
    }

    public static class PlayerStatAllocation {
        public int level;
        public int freeStatPoints;

        public PlayerStatAllocation(int level, int freeStatPoints) {
            this.level = level;
            this.freeStatPoints = freeStatPoints;
        }
    }
} 