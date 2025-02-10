package com.sandmmo.listeners;

import com.sandmmo.SandMMO;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {
    private final SandMMO plugin;

    public PlayerJoinListener(SandMMO plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Initialize player data on join
        plugin.getPlayerDataManager().getPlayerData(event.getPlayer());
    }
}