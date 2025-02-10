package com.sandmmo.player;

import com.sandmmo.SandMMO;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {
    private final Map<UUID, com.sandmmo.player.PlayerData> playerDataMap = new HashMap<>();
    private final SandMMO plugin; // Add plugin reference

    // Update constructor to accept plugin
    public PlayerDataManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    public com.sandmmo.player.PlayerData getPlayerData(Player player) {
        return playerDataMap.computeIfAbsent(player.getUniqueId(), k -> new com.sandmmo.player.PlayerData(player));
    }

    public void saveAllData() {
        // Implement data saving logic here
    }
}