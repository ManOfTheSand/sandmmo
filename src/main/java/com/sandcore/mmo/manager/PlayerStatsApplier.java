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
     * Applies the player's effective stats.
     * Retrieves current attribute values from StatsManager, applies them in-game, and refreshes the stats GUI.
     */
    public static void applyStats(Player player) {
        try {
            StatsManager statsManager = ServiceRegistry.getStatsManager();
            if (statsManager == null) {
                logger.warning("StatsManager is unavailable for player " + player.getName());
                return;
            }
            
            // Calculate effective values for each attribute.
            double effectiveMaxHealth = statsManager.recalcEffectiveAttribute(player, "maxHealth");
            double effectiveMaxMana = statsManager.recalcEffectiveAttribute(player, "maxMana");
            double effectiveHealthRegen = statsManager.recalcEffectiveAttribute(player, "healthRegen");
            double effectiveManaRegen = statsManager.recalcEffectiveAttribute(player, "manaRegen");
            double effectiveStrength = statsManager.recalcEffectiveAttribute(player, "strength");
            double effectiveDexterity = statsManager.recalcEffectiveAttribute(player, "dexterity");
            double effectiveIntellect = statsManager.recalcEffectiveAttribute(player, "intellect");
            double effectiveDefense = statsManager.recalcEffectiveAttribute(player, "defense");
            double effectiveMagicDefense = statsManager.recalcEffectiveAttribute(player, "magicDefense");
            
            // Apply in-game: update maximum health.
            if (effectiveMaxHealth < 1.0) {
                effectiveMaxHealth = 20.0;
            }
            player.setMaxHealth(effectiveMaxHealth);
            
            // Example: update player's attack damage based on strength.
            double baseAttackDamage = 1.0;
            double computedAttackDamage = baseAttackDamage + (effectiveStrength * 0.5);
            AttributeInstance attackAttribute = player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE);
            if (attackAttribute != null) {
                attackAttribute.setBaseValue(computedAttackDamage);
            }
            
            logger.info("Applied stats for " + player.getName() +
                    ": maxHealth=" + effectiveMaxHealth +
                    ", maxMana=" + effectiveMaxMana +
                    ", healthRegen=" + effectiveHealthRegen +
                    ", manaRegen=" + effectiveManaRegen +
                    ", strength=" + effectiveStrength +
                    ", dexterity=" + effectiveDexterity +
                    ", intellect=" + effectiveIntellect +
                    ", defense=" + effectiveDefense +
                    ", magicDefense=" + effectiveMagicDefense +
                    ", attackDamage=" + computedAttackDamage);
            
            // Refresh the stats GUI on the main thread.
            new BukkitRunnable() {
                @Override
                public void run() {
                    SandCoreMain main = SandCoreMain.getInstance();
                    if (main != null && main.getStatsGUIHandler() != null) {
                        main.getStatsGUIHandler().openGUI(player);
                    }
                }
            }.runTask(SandCoreMain.getInstance());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error applying stats for " + player.getName(), e);
        }
    }
} 