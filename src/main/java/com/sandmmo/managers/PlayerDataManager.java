package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.entity.Player;
import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager {
    private final HashMap<UUID, PlayerData> dataMap = new HashMap<>();
    private final SandMMO plugin;

    public PlayerDataManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    public PlayerData getData(Player player) {
        return dataMap.computeIfAbsent(player.getUniqueId(), k -> new PlayerData());
    }

    public static class PlayerData {
        private String currentClass;
        private int level;

        public void setCurrentClass(String className) {
            this.currentClass = className;
        }

        public int getLevel() {
            return this.level;
        }
    }
}