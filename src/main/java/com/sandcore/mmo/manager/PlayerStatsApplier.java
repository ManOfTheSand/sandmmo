package com.sandcore.mmo.manager;

import com.sandcore.mmo.SandCoreMain;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.logging.Level;
import java.util.logging.Logger;

public class PlayerStatsApplier {

    private static final Logger logger = Logger.getLogger(PlayerStatsApplier.class.getName());

    /**
     * Retrieves the player's current stats from the StatsManager, calculates effective in-game values
     * based on base stats, per-level increments, and allocated stat points, applies these values to the
     * player's in-game attributes via the Spigot API, and then refreshes the stats GUI to reflect the changes.
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
     *   <li>... any other custom attributes</li>
     * </ul>
     *
     * @param player the player for which to recalculate and apply stats.
     */
    public static void applyStats(Player player) {
        try {
            StatsManager statsManager = ServiceRegistry.getStatsManager();
            if (statsManager == null) {
                logger.warning("StatsManager is not available when applying stats for player " + player.getName());
                return;
            }

            // Retrieve current stat values from StatsManager.
            double maxHealth = statsManager.getStatValue(player, "maxHealth");
            double maxMana = statsManager.getStatValue(player, "maxMana");
            double healthRegen = statsManager.getStatValue(player, "healthRegen");
            double manaRegen = statsManager.getStatValue(player, "manaRegen");
            double strength = statsManager.getStatValue(player, "strength");
            double criticalChance = statsManager.getStatValue(player, "criticalChance");
            double criticalDamage = statsManager.getStatValue(player, "criticalDamage");

            // --- Dynamic Calculation & Application ---
            // 1. Apply Maximum Health.
            if (maxHealth < 1.0) {
                maxHealth = 20.0; // Fallback default to ensure no API error.
            }
            player.setMaxHealth(maxHealth);

            // 2. Update Attack Damage based on Strength.
            // For instance, the effective attack damage might be:
            // baseAttackDamage + (strength * 0.5)
            double baseAttackDamage = 1.0; // Example base damage value.
            double computedAttackDamage = baseAttackDamage + (strength * 0.5);
            AttributeInstance attackAttribute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (attackAttribute != null) {
                attackAttribute.setBaseValue(computedAttackDamage);
            }

            // 3. (Optional) Other attributes can be updated here.
            // For example, you might update movement speed using a "dexterity" stat:
            // double dexterity = statsManager.getStatValue(player, "dexterity");
            // double baseSpeed = 0.1;
            // double computedSpeed = baseSpeed + (dexterity * 0.01);
            // AttributeInstance speedAttribute = player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED);
            // if (speedAttribute != null) {
            //     speedAttribute.setBaseValue(computedSpeed);
            // }

            // Log the applied stats for debugging.
            logger.info("Applied stats for player " + player.getName() +
                    ": maxHealth=" + maxHealth +
                    ", maxMana=" + maxMana +
                    ", healthRegen=" + healthRegen +
                    ", manaRegen=" + manaRegen +
                    ", strength=" + strength +
                    ", attackDamage=" + computedAttackDamage +
                    ", criticalChance=" + criticalChance +
                    ", criticalDamage=" + criticalDamage);

            // --- GUI Refresh Integration ---
            // After applying in-game stats, refresh the player's stats GUI.
            // We call the openGUI(Player) method on the AsyncStatsGUIHandler instance from the main plugin.
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        SandCoreMain main = SandCoreMain.getInstance();
                        if (main != null && main.getStatsGUIHandler() != null) {
                            main.getStatsGUIHandler().openGUI(player);
                        }
                    } catch (Exception ex) {
                        logger.log(Level.SEVERE, "Error refreshing stats GUI for player " + player.getName(), ex);
                    }
                }
            }.runTask(SandCoreMain.getInstance());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error applying stats for player " + player.getName(), e);
        }
    }
} 