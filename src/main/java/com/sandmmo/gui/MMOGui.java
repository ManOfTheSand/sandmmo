package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class MMOGUI {
    private final SandMMO plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MMOGUI(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        // Read GUI settings from config; use defaults if not set
        String titleStr = plugin.getConfig().getString("menu.title", "<gold>MMO GUI");
        int size = plugin.getConfig().getInt("menu.size", 9);
        String itemNameStr = plugin.getConfig().getString("menu.item", "<blue>Click Me!");

        // Parse texts via MiniMessage to obtain Components
        Component title = miniMessage.deserialize(titleStr);
        Component itemName = miniMessage.deserialize(itemNameStr);

        // Create inventory using Paper's API that supports Components as titles
        Inventory inv = Bukkit.createInventory(null, size, title);

        // Create a sample item (DIAMOND) with colored display name
        ItemStack item = new ItemStack(Material.DIAMOND);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(itemName);
        item.setItemMeta(meta);

        // Place the item in the middle
        inv.setItem(size / 2, item);

        // Open the GUI for the player
        player.openInventory(inv);
    }
}