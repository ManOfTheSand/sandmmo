package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.managers.ClassManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
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
        FileConfiguration config = plugin.getClassesConfig();

        String materialName = config.getString(className + ".icon", "IRON_SWORD");
        Material material = Material.valueOf(materialName.toUpperCase());

        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + displayName);

        List<String> lore = new ArrayList<>();

        List<String> description = config.getStringList(className + ".description");
        for (String line : description) {
            lore.add(ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', line));
        }

        lore.add("");
        lore.add(ChatColor.YELLOW + "Base Stats:");

        ConfigurationSection statsSection = config.getConfigurationSection(className + ".stats");
        if (statsSection != null) {
            for (String stat : statsSection.getKeys(false)) {
                double value = statsSection.getDouble(stat);
                lore.add(ChatColor.GRAY + "• " + stat + ": " + ChatColor.WHITE + value);
            }
        }

        lore.add("");
        lore.add(ChatColor.GREEN + "Click to select this class!");

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
}