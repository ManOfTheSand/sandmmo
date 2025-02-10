package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.managers.ClassManager;
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

        // Get all classes and create items for each
        Map<String, String> classes = classManager.getAllClasses();
        for (Map.Entry<String, String> entry : classes.entrySet()) {
            String className = entry.getKey();
            String displayName = entry.getValue();

            ItemStack item = createClassItem(className, displayName);
            gui.addItem(item);
        }

        player.openInventory(gui);
    }

    private ItemStack createClassItem(String className, String displayName) {
        // Get class configuration
        String materialName = plugin.getConfig().getString("classes." + className + ".icon", "IRON_SWORD");
        Material material = Material.valueOf(materialName.toUpperCase());

        // Create item
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        // Set display name
        meta.setDisplayName(ChatColor.GOLD + displayName);

        // Create lore
        List<String> lore = new ArrayList<>();

        // Add description if exists
        List<String> description = plugin.getConfig().getStringList("classes." + className + ".description");
        for (String line : description) {
            lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', line));
        }

        // Add empty line
        lore.add("");

        // Add stats
        lore.add(ChatColor.YELLOW + "Base Stats:");
        Map<String, Double> stats = classManager.getClassStats(className, 1);
        for (Map.Entry<String, Double> stat : stats.entrySet()) {
            if (!stat.getKey().equals("displayName") && !stat.getKey().equals("icon")) {
                lore.add(ChatColor.GRAY + "• " + stat.getKey() + ": " + ChatColor.WHITE + stat.getValue());
            }
        }

        // Add instructions
        lore.add("");
        lore.add(ChatColor.GREEN + "Click to select this class!");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}