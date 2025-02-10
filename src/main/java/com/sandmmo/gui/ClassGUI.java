package com.sandmmo.gui;

import com.sandmmo.SandMMO;
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

    public ClassGUI(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        int size = getInventorySize(plugin.getClassManager().getAllClasses().size());
        Inventory gui = Bukkit.createInventory(null, size, ChatColor.DARK_PURPLE + "Class Selection");

        for (Map.Entry<String, Map<String, Object>> entry : plugin.getClassManager().getAllClasses().entrySet()) {
            String className = entry.getKey();
            Map<String, Object> classData = entry.getValue();
            String displayName = (String) classData.get("display-name");
            String description = (String) classData.get("description");
            String icon = (String) classData.get("icon");

            ItemStack item = createClassItem(className, displayName, description, icon);
            gui.addItem(item);
        }

        player.openInventory(gui);
    }

    private ItemStack createClassItem(String className, String displayName, String description, String icon) {
        Material material = Material.valueOf(icon.toUpperCase());
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GOLD + displayName);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + description);
        lore.add("");
        lore.add(ChatColor.GREEN + "Click to select this class!");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private int getInventorySize(int numClasses) {
        int size = (int) Math.ceil(numClasses / 9.0) * 9;
        return Math.min(size, 54);
    }
}