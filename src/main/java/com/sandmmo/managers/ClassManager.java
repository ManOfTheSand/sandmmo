package com.sandmmo.managers;

import com.sandmmo.config.ClassesConfig;
import com.sandmmo.config.ClassesConfig.MMOClass;
import org.bukkit.entity.Player;

public class ClassManager {
    private final ClassesConfig classesConfig;
    private final PlayerDataManager playerDataManager;

    public ClassManager(ClassesConfig classesConfig, PlayerDataManager playerDataManager) {
        this.classesConfig = classesConfig;
        this.playerDataManager = playerDataManager;
    }

    public boolean setClass(Player player, String className) {
        MMOClass mmoClass = classesConfig.getClasses().get(className);
        if (mmoClass == null) return false;

        PlayerDataManager.PlayerData data = playerDataManager.getData(player);
        data.setCurrentClass(className);
        applyClassAttributes(player, mmoClass);
        return true;
    }

    private void applyClassAttributes(Player player, MMOClass mmoClass) {
        // Implement attribute application using Eco's systems
        double health = mmoClass.baseHealth() + (mmoClass.healthPerLevel() * getPlayerLevel(player));
        double damage = mmoClass.baseDamage() + (mmoClass.damagePerLevel() * getPlayerLevel(player));

        // Example using Eco's attribute system
        Eco.get().getAttributeRegistry().getByID("generic.max_health")
                .ifPresent(attr -> attr.setValue(player, health));
    }

    private int getPlayerLevel(Player player) {
        return playerDataManager.getData(player).getLevel();
    }
}