package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.managers.MessagesManager;
import com.sandmmo.managers.PlayerDataManager;
import com.sandmmo.managers.StatsManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class StatsGUI {
    private final SandMMO plugin;
    private final PlayerDataManager dataManager;
    private final MessagesManager messages;
    private final StatsManager statsManager;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public StatsGUI(SandMMO plugin) {
        this.plugin = plugin;
        this.dataManager = plugin.getPlayerDataManager();
        this.messages = plugin.getMessagesManager();
        this.statsManager = plugin.getStatsManager();
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, miniMessage.deserialize(messages.getMessage("stats-gui.title")));

        // Add stats item
        gui.setItem(13, createPlayerStatsItem(player));

        player.openInventory(gui);
    }

    private ItemStack createPlayerStatsItem(Player player) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();

        meta.displayName(miniMessage.deserialize(messages.getMessage("stats-gui.your-stats")));

        List<String> lore = new ArrayList<>();
        lore.add(miniMessage.serialize(miniMessage.deserialize(messages.getMessage("stats-gui.class") + ": " + dataManager.getPlayerClass(player))));
        lore.add(miniMessage.serialize(miniMessage.deserialize(messages.getMessage("stats-gui.level") + ": " + dataManager.getPlayerLevel(player))));
        lore.add(miniMessage.serialize(miniMessage.deserialize(messages.getMessage("stats-gui.health") + ": " + statsManager.getPlayerHealth(player) + "/" + statsManager.getPlayerMaxHealth(player))));
        lore.add(miniMessage.serialize(miniMessage.deserialize(messages.getMessage("stats-gui.attack-damage") + ": " + statsManager.getPlayerAttackDamage(player))));
        lore.add(miniMessage.serialize(miniMessage.deserialize(messages.getMessage("stats-gui.skill-points") + ": " + plugin.getSkillManager().getSkillPoints(player))));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public void handleClick(InventoryClickEvent event) {
        event.setCancelled(true); // Prevent item taking
    }
}