package com.sandcore.mmo.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.event.inventory.InventoryType;
import net.kyori.adventure.text.Component;

public class StatsGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Cancel all interactions regardless of which inventory was clicked
        event.setCancelled(true);
        if (!(event.getWhoClicked() instanceof Player player)) return;
        if (!event.getView().title().equals(new StatsGUI(player, 0).getTitleComponent())) return;
        
        StatsManager statsManager = ServiceRegistry.getStatsManager();
        boolean allocated = false;
        int slot = event.getRawSlot();
        // For example: assume slots 10, 11, and 12 are for "strength", "dexterity", and "intellect"
        switch(slot) {
            case 10:
                allocated = statsManager.allocateStatPoint(player, "strength");
                break;
            case 11:
                allocated = statsManager.allocateStatPoint(player, "dexterity");
                break;
            case 12:
                allocated = statsManager.allocateStatPoint(player, "intellect");
                break;
            // Add other cases for additional attributes if desired.
        }
        if (allocated) {
            player.sendMessage(ChatColor.GREEN + "Stat point allocated!");
            player.playSound(player.getLocation(), "entity.player.levelup", 1F, 1F);
            // Reopen the GUI after a short delay (assume level is obtainable; here we use 10 as a placeholder)
            Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("SandMMO"), () -> {
                new com.sandcore.mmo.gui.StatsGUI(player, 10).open();
            }, 2L);
        } else {
            player.sendMessage(ChatColor.RED + "No free stat points available!");
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!event.getView().title().contains(Component.text("Player Stats"))) return;
        event.setCancelled(true);
    }
} 