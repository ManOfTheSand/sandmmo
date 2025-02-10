package com.sandmmo.listeners;

import com.sandmmo.SandMMO;
import com.sandmmo.gui.ClassGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ClassListener implements Listener {
    private final SandMMO plugin;

    public ClassListener(SandMMO plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getPlayerManager().hasSelectedClass(player)) {
            new ClassGUI(plugin).open(player);
        }
    }
}