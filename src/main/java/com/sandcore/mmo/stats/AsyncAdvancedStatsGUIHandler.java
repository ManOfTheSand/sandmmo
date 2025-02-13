package com.sandcore.mmo.stats;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class AsyncAdvancedStatsGUIHandler {

    private final JavaPlugin plugin;
    private final AdvancedStatsGUIHandler guiHandler;

    public AsyncAdvancedStatsGUIHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        this.guiHandler = new AdvancedStatsGUIHandler(plugin);
    }

    public void openStatsGUIAsync(final Player player, final int level) {
        // Generate the stats GUI asynchronously.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            Inventory inv = guiHandler.createStatsGUI(player, level);
            // Return to the main thread to open the inventory.
            Bukkit.getScheduler().runTask(plugin, () -> player.openInventory(inv));
        });
    }
} 