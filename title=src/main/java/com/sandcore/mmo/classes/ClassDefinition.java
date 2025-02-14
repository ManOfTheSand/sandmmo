package com.sandcore.mmo.classes;

import java.util.Map;

/**
 * Represents a player class definition loaded from classes.yml.
 */
public class ClassDefinition {

    private final String id;
    private final String displayName;
    private final String lore;
    private final Map<String, Double> startingStats;
    // Mapping from a key-combo (e.g., "LLL") to a skill id.
    private final Map<String, String> keyCombos;
    private final SoundSettings castingSound;
    private final SoundSettings comboClickSound;
    private final SoundSettings comboFailSound;

    public ClassDefinition(String id, String displayName, String lore, Map<String, Double> startingStats,
                           Map<String, String> keyCombos, SoundSettings castingSound,
                           SoundSettings comboClickSound, SoundSettings comboFailSound) {
        this.id = id;
        this.displayName = displayName;
        this.lore = lore;
        this.startingStats = startingStats;
        this.keyCombos = keyCombos;
        this.castingSound = castingSound;
        this.comboClickSound = comboClickSound;
        this.comboFailSound = comboFailSound;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getLore() {
        return lore;
    }

    public Map<String, Double> getStartingStats() {
        return startingStats;
    }

    public Map<String, String> getKeyCombos() {
        return keyCombos;
    }

    public SoundSettings getCastingSound() {
        return castingSound;
    }

    public SoundSettings getComboClickSound() {
        return comboClickSound;
    }

    public SoundSettings getComboFailSound() {
        return comboFailSound;
    }
} 