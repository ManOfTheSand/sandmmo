package com.sandcore.mmo.stats;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

/**
 * Custom InventoryHolder used to mark the stats GUI.
 * Inventories created with this holder are recognized and protected by our event listeners.
 */
public class StatsGUIHolder implements InventoryHolder {
    @Override
    public Inventory getInventory() {
        return null; // Not used.
    }
} 