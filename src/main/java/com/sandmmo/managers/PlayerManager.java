package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private final SandMMO plugin;
    private final Map<UUID, String> playerClasses = new HashMap<>();

    public PlayerManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void setPlayerClass(Player player, String className) {
        playerClasses.put(player.getUniqueId(), className);
    }

    public String getPlayerClass(Player player) {
        return playerClasses.get(player.getUniqueId());
    }

    public boolean hasSelectedClass(Player player) {
        return playerClasses.containsKey(player.getUniqueId());
    }
}