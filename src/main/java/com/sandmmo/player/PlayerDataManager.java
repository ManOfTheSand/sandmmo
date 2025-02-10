package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import com.sandmmo.player.PlayerData;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final SandMMO plugin;
    private final Map<UUID, PlayerData> playerDataMap;

    public PlayerDataManager(SandMMO plugin) {
        this.plugin = plugin;
        this.playerDataMap = new HashMap<>();
    }

    public void loadPlayerData(Player player) {
        UUID playerId = player.getUniqueId();
        PlayerData playerData = new PlayerData(player);
        playerDataMap.put(playerId, playerData);
    }

    public PlayerData getPlayerData(Player player) {
        UUID playerId = player.getUniqueId();
        return playerDataMap.get(playerId);
    }
}