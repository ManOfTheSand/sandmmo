package com.sandcore.mmo.stats;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.logging.Logger;

/**
 * Applies computed stat values to a player's in-game attributes.
 * For example, it updates the player's max health attribute based on the computed stat.
 */
public class PlayerStatsApplier {

    private static final Logger logger = Logger.getLogger(PlayerStatsApplier.class.getName());

    public static void applyStats(Player player, StatsManager statsManager) {
        double maxHealth = statsManager.getStatValue(player, "maxHealth");
        AttributeInstance healthAttr = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        if (healthAttr != null) {
            healthAttr.setBaseValue(maxHealth);
            if (player.getHealth() > maxHealth) {
                player.setHealth(maxHealth);
            }
            logger.info("Applied maxHealth (" + maxHealth + ") for player " + player.getName());
        }
        // Other attribute updates (e.g., mana, damage) would be applied in a similar fashion.
    }
} 