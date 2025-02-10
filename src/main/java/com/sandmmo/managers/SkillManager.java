package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import com.sandmmo.gui.SkillsGUI;
import org.bukkit.entity.Player;

public class SkillManager {
    private final SandMMO plugin;
    private final PlayerDataManager playerDataManager;
    private final SkillsGUI skillsGUI;

    public SkillManager(SandMMO plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
        this.skillsGUI = plugin.getSkillsGUI();
    }

    public void openSkillsGUI(Player player) {
        skillsGUI.open(player);
    }

    public void addSkillPoints(Player player, int amount) {
        // TODO: Implement adding skill points to the player
    }

    public void removeSkillPoints(Player player, int amount) {
        // TODO: Implement removing skill points from the player
    }

    public int getSkillPoints(Player player) {
        // TODO: Implement retrieving the player's skill points
        return 0;
    }
}