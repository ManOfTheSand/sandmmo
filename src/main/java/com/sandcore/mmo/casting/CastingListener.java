package com.sandcore.mmo.casting;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

public class CastingListener implements Listener {
    private final CastingManager castingManager;

    public CastingListener(CastingManager castingManager) {
        this.castingManager = castingManager;
    }

    @EventHandler
    public void onSwapHandItems(PlayerSwapHandItemsEvent event) {
        event.setCancelled(true);
        castingManager.toggleCastingMode(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        
        if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
            castingManager.handleClick(player, CastingManager.ClickType.LEFT);
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            castingManager.handleClick(player, CastingManager.ClickType.RIGHT);
        }
    }

    @EventHandler
    public void onComboClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        // Only proceed if player is in casting mode.
        CastingManager castingManager = ServiceRegistry.getCastingManager();
        if (castingManager == null || !castingManager.isCasting(player)) {
            return;
        }
        // Check if action is a valid click type (left or right) for a combo click.
        if (event.getAction() == Action.LEFT_CLICK_AIR ||
            event.getAction() == Action.LEFT_CLICK_BLOCK ||
            event.getAction() == Action.RIGHT_CLICK_AIR ||
            event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            ClassManager.PlayerClass playerClass = ServiceRegistry.getClassManager().getPlayerClass(player);
            if (playerClass != null) {
                String soundName = playerClass.getComboClickSoundName();
                float volume = playerClass.getComboClickSoundVolume();
                float pitch = playerClass.getComboClickSoundPitch();
                try {
                    Sound sound = Sound.valueOf(soundName);
                    player.playSound(player.getLocation(), sound, volume, pitch);
                } catch (IllegalArgumentException ex) {
                    player.sendMessage("Invalid combo click sound: " + soundName);
                }
            }
        }
    }
} 