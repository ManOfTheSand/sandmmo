package com.sandmmo.listeners;

import com.sandmmo.SandMMO;
import com.sandmmo.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClassListener implements Listener {
    private final SandMMO plugin;
    private final PlayerDataManager playerDataManager;

    public ClassListener(SandMMO plugin) {
        this.plugin = plugin;
        this.playerDataManager = plugin.getPlayerDataManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if(playerDataManager.getPlayerClass(player).isEmpty()) {
            plugin.getClassGUI().open(player);
        }
    }
}