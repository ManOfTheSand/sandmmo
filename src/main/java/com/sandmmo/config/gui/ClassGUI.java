package com.sandmmo.config.gui;

import com.sandmmo.config.SandMMO;
import com.sandmmo.config.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassGUI {
    private final SandMMO plugin;
    private final ClassManager classManager;

    public ClassGUI(SandMMO plugin) {
        this.plugin = plugin;
        this.classManager = plugin.getClassManager();
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27,
                ChatColor.DARK_PURPLE + "Class Selection");

        for (Map.Entry<String, Map<String, Object>> entry :
                classManager.getAllClasses().entrySet()) {

            ItemStack item = createClassItem(entry.getKey(), entry.getValue());
            gui.addItem(item);
        }

        player.openInventory(gui);
    }

    private ItemStack createClassItem(String className, Map<String, Object> classData) {
        Material material = Material.valueOf(
                classData.getOrDefault("icon", "BARRIER").toString());
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&',
                classData.get("display_name").toString()));

        List<String> lore = new ArrayList<>();
        for (String line : (List<String>) classData.get("description")) {
            lore.add(ChatColor.GRAY + line);
        }
        lore.add("");
        lore.add(ChatColor.GOLD + "Base Stats:");
        ((Map<String, Double>) classData.get("base_stats")).forEach((stat, value) ->
                lore.add(ChatColor.GRAY + "• " + stat + ": " + ChatColor.WHITE + value));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}