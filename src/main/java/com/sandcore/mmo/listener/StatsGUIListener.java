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
import com.sandcore.mmo.gui.StatsGUI;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.InventoryView;

public class StatsGUIListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true); // Cancel all clicks
        if (!isStatsGUI(event.getView())) return;
        
        // Handle your stat point allocation here
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (isStatsGUI(event.getView())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (isStatsGUI(event.getView())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryCreative(InventoryCreativeEvent event) {
        if (isStatsGUI(event.getView())) {
            event.setCancelled(true);
        }
    }

    private boolean isStatsGUI(InventoryView view) {
        Component guiTitle = new StatsGUI(null, 0).getTitleComponent(); // Null player is safe here
        return view.title().equals(guiTitle);
    }
} 