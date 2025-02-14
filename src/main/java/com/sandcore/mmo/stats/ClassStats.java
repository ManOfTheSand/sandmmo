package com.sandcore.mmo.stats;

/**
 * Represents bonus stat values granted by a player's chosen class.
 */
public class ClassStats {
    private final double bonusMaxHealth;
    private final double bonusMaxMana;
    private final double bonusHealthRegen;
    private final double bonusManaRegen;
    private final double bonusStrength;
    private final double bonusDexterity;
    private final double bonusDefense;
    private final double bonusMagicDamage;

    public ClassStats() {
        this(0, 0, 0, 0, 0, 0, 0, 0);
    }

    public ClassStats(double bonusMaxHealth, double bonusMaxMana, double bonusHealthRegen, double bonusManaRegen,
                      double bonusStrength, double bonusDexterity, double bonusDefense, double bonusMagicDamage) {
        this.bonusMaxHealth = bonusMaxHealth;
        this.bonusMaxMana = bonusMaxMana;
        this.bonusHealthRegen = bonusHealthRegen;
        this.bonusManaRegen = bonusManaRegen;
        this.bonusStrength = bonusStrength;
        this.bonusDexterity = bonusDexterity;
        this.bonusDefense = bonusDefense;
        this.bonusMagicDamage = bonusMagicDamage;
    }

    /**
     * Returns the bonus for the given stat key.
     *
     * @param statKey the stat identifier (e.g., "maxHealth", "strength")
     * @return bonus value or 0 if not present.
     */
    public double getBonus(String statKey) {
        switch (statKey) {
            case "maxHealth": return bonusMaxHealth;
            case "maxMana": return bonusMaxMana;
            case "healthRegen": return bonusHealthRegen;
            case "manaRegen": return bonusManaRegen;
            case "strength": return bonusStrength;
            case "dexterity": return bonusDexterity;
            case "defense": return bonusDefense;
            case "magicDamage": return bonusMagicDamage;
            default: return 0;
        }
    }
} 