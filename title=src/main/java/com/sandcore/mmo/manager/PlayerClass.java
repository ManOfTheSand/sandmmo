package com.sandcore.mmo.manager;

import org.bukkit.Material;
import java.util.Map;

/**
 * Represents a player class (e.g., Mage, Warrior, Rogue).
 * Contains display details, casting abilities configuration, and requirements.
 */
public class PlayerClass {
    private String id;
    private String displayName;
    private String description;
    private Material icon;
    private Map<String, CastingAbility> castingAbilities; // Key is click combo string (e.g., "LEFT_LEFT_RIGHT")
    private Requirements requirements;

    public PlayerClass(String id, String displayName, String description, Material icon,
                       Map<String, CastingAbility> castingAbilities, Requirements requirements) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.icon = icon;
        this.castingAbilities = castingAbilities;
        this.requirements = requirements;
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

    public Material getIcon() {
        return icon;
    }

    public Map<String, CastingAbility> getCastingAbilities() {
        return castingAbilities;
    }

    public Requirements getRequirements() {
        return requirements;
    }

    @Override
    public String toString() {
        return "PlayerClass{" +
                "id='" + id + '\'' +
                ", displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                ", icon=" + icon +
                ", castingAbilities=" + castingAbilities +
                ", requirements=" + requirements +
                '}';
    }
} 