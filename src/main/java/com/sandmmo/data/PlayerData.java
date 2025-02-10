package com.sandmmo.player;

import com.sandmmo.classes.PlayerClass;
import org.bukkit.entity.Player;

public class PlayerData {
    private final Player player;
    private PlayerClass playerClass;
    private int level;
    private double experience;

    public PlayerData(Player player) {
        this.player = player;
        this.level = 1;
        this.experience = 0;
    }

    // Add experience/level methods
    public void addExperience(double amount) {
        experience += amount;
        checkLevelUp();
    }

    private void checkLevelUp() {
        double required = getRequiredExperience();
        if(experience >= required) {
            level++;
            experience -= required;
            // Add level up effects
        }
    }

    public double getRequiredExperience() {
        return 100 * Math.pow(1.1, level-1);
    }

    // Getters and setters
    public PlayerClass getPlayerClass() { return playerClass; }
    public void setPlayerClass(PlayerClass playerClass) { this.playerClass = playerClass; }
    public int getLevel() { return level; }
    public double getExperience() { return experience; }
}