package com.sandcore.mmo.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.bukkit.entity.Player;

public class PlayerClassDataManager {

    private static final Map<UUID, String> playerClassMap = new HashMap<>();

    public static void setPlayerClass(Player player, String classId) {
        playerClassMap.put(player.getUniqueId(), classId.toLowerCase());
    }

    public static String getPlayerClass(Player player) {
        return playerClassMap.get(player.getUniqueId());
    }
} 