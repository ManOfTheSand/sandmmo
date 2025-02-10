package com.sandmmo.listeners;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // Send a welcome message when a player joins.
        event.getPlayer().sendMessage(ChatColor.AQUA + "Welcome to SandMMO!");

        // Example: Load or initialize the player's MMO data using eco's persistent data storage.
        // (You could integrate eco's API here as needed.)
    }
}
