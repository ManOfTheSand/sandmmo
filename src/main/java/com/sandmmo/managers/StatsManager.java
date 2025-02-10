package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

public class StatsManager {
    private final SandMMO plugin;
    private final PlayerDataManager playerDataManager;

    public StatsManager(SandMMO plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    public void updatePlayerStats(Player player) {
        String className = playerDataManager.getPlayerClass(player);
        int level = playerDataManager.getPlayerLevel(player);

        // Update player stats based on class and level
        double maxHealth = 20 + level * 2; // Adjust the formula as needed
        double attackDamage = 1 + level * 0.5; // Adjust the formula as needed

        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(maxHealth);
        player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(attackDamage);
    }

    public double getPlayerHealth(Player player) {
        return player.getHealth();
    }

    public void setPlayerHealth(Player player, double health) {
        player.setHealth(health);
    }

    public double getPlayerMaxHealth(Player player) {
        return player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    public double getPlayerAttackDamage(Player player) {
        return player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).getBaseValue();
    }
}