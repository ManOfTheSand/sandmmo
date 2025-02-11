package com.sandcore.mmo.manager;

import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerStatsApplier {

    private static final Logger logger = Logger.getLogger(PlayerStatsApplier.class.getName());

    /**
     * Recalculates and applies the player's stats.
     * <p>
     * Retrieves stat values from StatsManager using dynamic formulas (including base values and per-level increments)
     * and applies them to the player's in-game attributes via the Spigot API.
     * For example, it updates the maximum health and attack damage.
     * </p>
     *
     * Expected stat keys:
     * <ul>
     *   <li>"maxHealth"</li>
     *   <li>"maxMana"</li>
     *   <li>"healthRegen"</li>
     *   <li>"manaRegen"</li>
     *   <li>"strength"</li>
     *   <li>"criticalChance"</li>
     *   <li>"criticalDamage"</li>
     *   <li>... any custom attributes as needed </li>
     * </ul>
     *
     * @param player the player for which to apply the stats.
     */
    public static void applyStats(Player player) {
        try {
            StatsManager statsManager = ServiceRegistry.getStatsManager();
            if (statsManager == null) {
                logger.warning("StatsManager is not available when applying stats for player " + player.getName());
                return;
            }
            
            // Retrieve current stat values, calculated via StatsManager.
            double maxHealth = statsManager.getStatValue(player, "maxHealth");
            double maxMana = statsManager.getStatValue(player, "maxMana");
            double healthRegen = statsManager.getStatValue(player, "healthRegen");
            double manaRegen = statsManager.getStatValue(player, "manaRegen");
            double strength = statsManager.getStatValue(player, "strength");
            double criticalChance = statsManager.getStatValue(player, "criticalChance");
            double criticalDamage = statsManager.getStatValue(player, "criticalDamage");
            
            // --- Dynamic Calculations & Application ---
            
            // 1. Apply Maximum Health.
            // Ensure maxHealth is within a valid range.
            if (maxHealth < 1.0) {
                maxHealth = 20.0; // fallback default
            }
            player.setMaxHealth(maxHealth);
            
            // 2. Update Attack Damage.
            // Example dynamic formula: baseAttackDamage + (strength * factor).
            double baseAttackDamage = 1.0; // Base damage for your calculation.
            double computedAttackDamage = baseAttackDamage + (strength * 0.5);
            AttributeInstance attackAttribute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (attackAttribute != null) {
                attackAttribute.setBaseValue(computedAttackDamage);
            }
            
            // 3. (Optional) You might update movement speed or other native attributes.
            // For example, if you had a "dexterity" stat affecting movement:
            // double dexterity = statsManager.getStatValue(player, "dexterity");
            // double baseSpeed = 0.1D; // default movement speed factor.
            // double computedSpeed = baseSpeed + (dexterity * 0.01);
            // AttributeInstance speedAttribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            // if (speedAttribute != null) {
            //     speedAttribute.setBaseValue(computedSpeed);
            // }
            
            // 4. Log remaining custom stats for debug purposes.
            // Note: mana, health/mana regeneration, critical chance/damage are typically handled
            // by your custom systems (e.g., via scheduled tasks, custom potion effects, or scoreboards).
            logger.info("Applied stats for player " + player.getName() +
                    ": maxHealth=" + maxHealth +
                    ", maxMana=" + maxMana +
                    ", healthRegen=" + healthRegen +
                    ", manaRegen=" + manaRegen +
                    ", strength=" + strength +
                    ", attackDamage=" + computedAttackDamage +
                    ", criticalChance=" + criticalChance +
                    ", criticalDamage=" + criticalDamage);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error applying stats for player " + player.getName(), e);
        }
    }
} 