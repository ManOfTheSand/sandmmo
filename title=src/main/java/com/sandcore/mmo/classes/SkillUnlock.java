package com.sandcore.mmo.classes;

/**
 * Represents a skill unlock entry for a class.
 */
public class SkillUnlock {
    
    private final String id;
    private final int levelRequired;

    public SkillUnlock(String id, int levelRequired) {
        this.id = id;
        this.levelRequired = levelRequired;
    }

    public String getId() {
        return id;
    }

    public int getLevelRequired() {
        return levelRequired;
    }
} 