package com.sandcore.mmo.manager;

/**
 * Represents the requirements or unlock criteria for a player class.
 */
public class Requirements {
    private int level;
    private String unlockCriteria;

    public Requirements(int level, String unlockCriteria) {
        this.level = level;
        this.unlockCriteria = unlockCriteria;
    }

    public int getLevel() {
        return level;
    }

    public String getUnlockCriteria() {
        return unlockCriteria;
    }

    @Override
    public String toString() {
        return "Requirements{" +
                "level=" + level +
                ", unlockCriteria='" + unlockCriteria + '\'' +
                '}';
    }
} 