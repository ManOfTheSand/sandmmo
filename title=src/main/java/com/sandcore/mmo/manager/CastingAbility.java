package com.sandcore.mmo.manager;

import java.util.Map;

/**
 * Represents a casting ability mapping for a player class.
 * This includes the skill to be cast, its cooldown, and additional parameters.
 */
public class CastingAbility {
    private String skill;
    private int cooldown; // in seconds
    private Map<String, Object> parameters; // additional parameters for the ability

    public CastingAbility(String skill, int cooldown, Map<String, Object> parameters) {
        this.skill = skill;
        this.cooldown = cooldown;
        this.parameters = parameters;
    }

    public String getSkill() {
        return skill;
    }

    public int getCooldown() {
        return cooldown;
    }

    public Map<String, Object> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "CastingAbility{" +
                "skill='" + skill + '\'' +
                ", cooldown=" + cooldown +
                ", parameters=" + parameters +
                '}';
    }
} 