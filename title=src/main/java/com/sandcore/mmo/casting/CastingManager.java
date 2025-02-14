package com.sandcore.mmo.casting;

import com.sandcore.mmo.manager.PlayerClassDataManager;
import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.classes.ClassDefinition;
import org.bukkit.entity.Player;

public class CastingManager {

    private final ClassManager classManager;

    public CastingManager(ClassManager classManager) {
        this.classManager = classManager;
    }

    /**
     * Checks if the skill with the given id is unlocked for the player's class.
     * This demo method checks if the given skillId exists in the class's keyCombos.
     */
    public boolean isSkillUnlocked(Player player, String skillId) {
        String classId = PlayerClassDataManager.getPlayerClass(player);
        if (classId == null) return false;
        ClassDefinition def = classManager.getClassDefinition(classId);
        if (def == null) return false;
        return def.getKeyCombos().values().stream().anyMatch(s -> s.equalsIgnoreCase(skillId));
    }

    // Additional casting combo handling methods can be added here.
} 