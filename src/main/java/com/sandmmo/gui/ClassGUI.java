package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.data.ClassData;
import com.sandmmo.managers.MessagesManager;
import com.sandmmo.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClassGUI {
    private final SandMMO plugin;
    private final PlayerDataManager dataManager;
    private final MessagesManager messages;

    public ClassGUI(SandMMO plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getPlayerDataManager();
        this.messages = plugin.getMessagesManager();
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, messages.getMessage("gui.title"));

        // Add class items
        plugin.getClassManager().getAllClasses().forEach((className, data) -> {
            gui.addItem(createClassItem(className, data));
        });

        // Add info item
        gui.setItem(22, createPlayerInfoItem(player));

        player.openInventory(gui);
    }

    private ItemStack createClassItem(String className, ClassData data) {
        ItemStack item = new ItemStack(Material.valueOf(data.getIcon()));
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', data.getDisplayName()));

        List<String> lore = new ArrayList<>();
        for(String line : data.getDescription()) {
            lore.add(ChatColor.GRAY + line);
        }
        lore.add("");
        lore.add(messages.getMessage("gui.click-to-select"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private ItemStack createPlayerInfoItem(Player player) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(messages.getMessage("gui.your-stats"));

        List<String> lore = new ArrayList<>();
        lore.add(messages.getMessage("gui.class") + ": " + dataManager.getPlayerClass(player));
        lore.add(messages.getMessage("gui.level") + ": " + dataManager.getPlayerLevel(player));
        // Add more stats here

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true); // Prevent item taking

        if(event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if(item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            plugin.getClassManager().selectClass(player, displayName);
            player.closeInventory();
        }
    }
}