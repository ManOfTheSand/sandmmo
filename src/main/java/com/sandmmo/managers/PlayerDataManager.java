package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();
    private final SandMMO plugin;

    public PlayerDataManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void handleJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        // Load from database/file
        playerDataMap.put(player.getUniqueId(), new PlayerData());
    }

    public void handleQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        // Save to database/file
        playerDataMap.remove(player.getUniqueId());
    }

    public PlayerData getData(Player player) {
        return playerDataMap.get(player.getUniqueId());
    }

    public static class PlayerData {
        private String currentClass;
        private int level;
        private double experience;

        // Add other player data fields
    }
}