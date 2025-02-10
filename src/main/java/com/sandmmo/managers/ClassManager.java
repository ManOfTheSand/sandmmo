package com.sandmmo.managers;

import com.sandmmo.config.ClassesConfig;
import com.sandmmo.config.ClassesConfig.MMOClass;
import com.willfp.eco.core.Eco;
import org.bukkit.entity.Player;

public class ClassManager {
    private final ClassesConfig config;
    private final PlayerDataManager dataManager;

    public ClassManager(ClassesConfig config, PlayerDataManager dataManager) {
        this.config = config;
        this.dataManager = dataManager;
    }

    public void setClass(Player player, String className) {
        MMOClass mmoClass = config.getClasses().get(className);
        if (mmoClass == null) return;

        PlayerDataManager.PlayerData data = dataManager.getData(player);
        data.setCurrentClass(className);
        applyAttributes(player, mmoClass);
    }

    private void applyAttributes(Player player, MMOClass mmoClass) {
        Eco.get().getAttributeRegistry().getByID("generic.max_health")
                .ifPresent(attr -> attr.setValue(player,
                        mmoClass.baseHealth() + (mmoClass.healthPerLevel() * level)
                ));
    }
}