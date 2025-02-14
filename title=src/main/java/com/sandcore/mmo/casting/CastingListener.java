package com.sandcore.mmo.casting;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CastingListener implements Listener {

    private final CastingManager castingManager;

    public CastingListener(CastingManager castingManager) {
        this.castingManager = castingManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        // For demonstration, we use a simple logic:
        // If the player left-clicks, we test for the "fireball" skill.
        String testSkillId = "fireball";
        if ((action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) &&
            !castingManager.isSkillUnlocked(player, testSkillId)) {
            player.sendMessage(ChatColor.RED + "You have not unlocked the skill: " + testSkillId);
            event.setCancelled(true);
        }
    }
} 