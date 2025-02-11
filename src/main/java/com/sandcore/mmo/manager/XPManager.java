package com.sandcore.mmo.manager;

import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class XPManager {
    private final Logger logger = Logger.getLogger(XPManager.class.getName());
    // Store player's accumulated XP.
    private final Map<UUID, Integer> playerXP = new HashMap<>();
    // Store player's current level. Defaults to 1.
    private final Map<UUID, Integer> playerLevel = new HashMap<>();

    public XPManager() {
        // Initialize XPManager.
    }

    public int calculateRequiredXP(int currentLevel) {
        // Example XP calculation: level * 100.
        return currentLevel * 100;
    }

    // Returns the XP threshold required to level up from the given level.
    private int getXpThreshold(int level) {
        return level * 100;
    }

    /**
     * Awards XP to a player, checks for level ups, and updates the player's class data via ClassManager.
     *
     * @param player the player to award XP to
     * @param xp the amount of XP to award
     */
    public void awardXP(Player player, int xp) {
        UUID uuid = player.getUniqueId();
        int currentXP = playerXP.getOrDefault(uuid, 0);
        int newXP = currentXP + xp;
        playerXP.put(uuid, newXP);
        logger.info("Awarded " + xp + " XP to " + player.getName() + ". Total XP: " + newXP);

        // Get current level (defaults to 1 if not set)
        int level = playerLevel.getOrDefault(uuid, 1);
        boolean leveledUp = false;

        // Check for level up until the XP is not sufficient for the next level.
        while (newXP >= getXpThreshold(level)) {
            newXP -= getXpThreshold(level);
            level++;
            playerXP.put(uuid, newXP);
            playerLevel.put(uuid, level);
            logger.info("Player " + player.getName() + " leveled up to level " + level);

            // Call updateLevel in ClassManager to adjust player's class data.
            try {
                // Assuming ClassManager has an updateLevel(Player) method.
                ServiceRegistry.getClassManager().updateLevel(player);
            } catch (Exception e) {
                logger.severe("Failed to update player level in ClassManager for " + player.getName() +
                        ": " + e.getMessage());
            }
            leveledUp = true;
        }
        if (!leveledUp) {
            logger.info("Player " + player.getName() + " did not level up. Current XP: " + newXP);
        }
    }

    public void reload() {
        // Add reload logic for XP configurations
    }
} 