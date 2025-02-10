package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.entity.Player;

import java.util.Map;

public class SkillManager {
    private final SandMMO plugin;

    public SkillManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void applyClassStats(Player player) {
        String className = plugin.getPlayerManager().getPlayerClass(player);
        if (className == null) {
            return;
        }

        int level = plugin.getPlayerManager().getPlayerLevel(player);
        Map<String, Double> stats = plugin.getClassManager().getClassStats(className, level);
        if (stats == null) {
            return;
        }

        // TODO: Apply the stats to the player here
        player.sendMessage("Applied class stats for level " + level);
    }
}