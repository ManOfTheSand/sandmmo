package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.managers.MessagesManager;
import com.sandmmo.managers.PlayerDataManager;
import com.sandmmo.managers.SkillManager;
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

public class SkillsGUI {
    private final SandMMO plugin;
    private final PlayerDataManager dataManager;
    private final MessagesManager messages;
    private final SkillManager skillManager;

    public SkillsGUI(SandMMO plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getPlayerDataManager();
        this.messages = plugin.getMessagesManager();
        this.skillManager = plugin.getSkillManager();
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, messages.getMessage("skills-gui.title"));

        // Add skill items
        // TODO: Implement skill items based on player's class and level

        // Add info item
        gui.setItem(22, createPlayerInfoItem(player));

        player.openInventory(gui);
    }

    private ItemStack createPlayerInfoItem(Player player) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(messages.getMessage("skills-gui.your-stats"));

        List<String> lore = new ArrayList<>();
        lore.add(messages.getMessage("skills-gui.class") + ": " + dataManager.getPlayerClass(player));
        lore.add(messages.getMessage("skills-gui.level") + ": " + dataManager.getPlayerLevel(player));
        lore.add(messages.getMessage("skills-gui.skill-points") + ": " + skillManager.getSkillPoints(player));
        // Add more stats as needed

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true); // Prevent item taking

        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            String displayName = ChatColor.stripColor(item.getItemMeta().getDisplayName());
            // TODO: Handle skill item clicks based on display name
        }
    }
}