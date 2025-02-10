package com.sandmmo.config.managers;

import com.sandmmo.config.SandMMO;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.UUID;

public class SkillManager {
    private final SandMMO plugin;
    private final MythicBukkit mythicBukkit;

    public SkillManager(SandMMO plugin) {
        this.plugin = plugin;
        this.mythicBukkit = MythicBukkit.inst();
    }

    public void applyClassStats(Player player, String className) {
        Map<String, Double> stats = plugin.getClassManager().getClassStats(className, 1);
        UUID uuid = player.getUniqueId();

        // Store stats in player data
        Map<String, Object> playerData = plugin.getPlayerManager().getPlayerData(uuid);
        if (playerData != null) {
            playerData.put("classStats", stats);
            plugin.getPlayerManager().updatePlayerData(uuid, playerData);
        }
    }

    public void triggerSkill(Player player, ItemStack item) {
        String skillName = plugin.getClassManager().getSkillFromItem(item);
        if (skillName != null) {
            // Apply skill with MythicMobs
            mythicBukkit.getAPIHelper().castSkill(player, skillName);

            // Optional: Add cooldown tracking
            plugin.getPlayerManager().setSkillCooldown(
                    player.getUniqueId(),
                    skillName,
                    System.currentTimeMillis()
            );
        }
    }
}