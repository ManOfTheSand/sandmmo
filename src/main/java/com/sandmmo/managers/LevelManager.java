package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.entity.Player;

public class LevelManager {
    private final SandMMO plugin;
    private final PlayerDataManager playerDataManager;
    private final StatsManager statsManager;
    private final MessagesManager messagesManager;

    public LevelManager(SandMMO plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.statsManager = plugin.getStatsManager();
        this.messagesManager = plugin.getMessagesManager();
    }

    public void addExperience(Player player, int amount) {
        int currentExp = playerDataManager.getPlayerExperience(player);
        int newExp = currentExp + amount;
        int requiredExp = getRequiredExperience(player);

        if (newExp >= requiredExp) {
            levelUp(player);
            newExp -= requiredExp;
        }

        playerDataManager.setPlayerExperience(player, newExp);
    }

    public void levelUp(Player player) {
        int currentLevel = playerDataManager.getPlayerLevel(player);
        playerDataManager.setPlayerLevel(player, currentLevel + 1);
        statsManager.updatePlayerStats(player);
        messagesManager.sendMessage(player, "level-up", String.valueOf(currentLevel + 1));
    }

    private int getRequiredExperience(Player player) {
        int level = playerDataManager.getPlayerLevel(player);
        return 100 * level; // Adjust the formula as needed
    }
}