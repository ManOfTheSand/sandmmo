package com.sandmmo.classes;

public class PlayerClass {
    private final String displayName;
    private final int baseHealth;
    private final int baseMana;
    private final double strengthMultiplier;

    public PlayerClass(String displayName, int baseHealth, int baseMana, double strengthMultiplier) {
        this.displayName = displayName;
        this.baseHealth = baseHealth;
        this.baseMana = baseMana;
        this.strengthMultiplier = strengthMultiplier;
    }

    // Getters
    public String getDisplayName() { return displayName; }
    public int getBaseHealth() { return baseHealth; }
    public int getBaseMana() { return baseMana; }
    public double getStrengthMultiplier() { return strengthMultiplier; }
}