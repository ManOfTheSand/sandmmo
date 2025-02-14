package com.sandcore.mmo.classes;

import java.util.List;
import java.util.Map;

/**
 * Represents a player class definition loaded from classes.yml.
 */
public class ClassDefinition {

    private final String id;
    private final String displayName;
    private final String description;
    private final Map<String, Double> startingStats;
    private final Map<String, Double> perLevelStats;
    private final List<SkillUnlock> skills;

    public ClassDefinition(String id, String displayName, String description,
                           Map<String, Double> startingStats,
                           Map<String, Double> perLevelStats,
                           List<SkillUnlock> skills) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.startingStats = startingStats;
        this.perLevelStats = perLevelStats;
        this.skills = skills;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Double> getStartingStats() {
        return startingStats;
    }

    public Map<String, Double> getPerLevelStats() {
        return perLevelStats;
    }

    public List<SkillUnlock> getSkills() {
        return skills;
    }
} 