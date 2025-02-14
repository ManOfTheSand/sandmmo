package com.sandcore.mmo.stats;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AdvancedStatsGUIHandler {

    private final JavaPlugin plugin;
    private final AdvancedStatsManager statsManager;
    private final String title;
    private final int size;
    private final FileConfiguration guiConfig;

    public AdvancedStatsGUIHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        File guiFile = new File(plugin.getDataFolder(), "statsgui.yml");
        if (!guiFile.exists()) {
            plugin.saveResource("statsgui.yml", false);
        }
        this.guiConfig = YamlConfiguration.loadConfiguration(guiFile);
        String basePath = "stats_gui";
        this.title = guiConfig.getString(basePath + ".title", "Player Stats");
        this.size = guiConfig.getInt(basePath + ".size", 27);
        this.statsManager = new AdvancedStatsManager(plugin.getDataFolder());
    }

    // Creates an inventory GUI showing the player's computed stats.
    public Inventory createStatsGUI(Player player, int level) {
        // Create inventory with custom holder so the GUI is locked.
        Inventory inv = Bukkit.createInventory(new AdvancedStatsGUIHolder(), size, Component.text(title));
        String basePath = "stats_gui";
        // Populate attribute items
        if (guiConfig.isConfigurationSection(basePath + ".attributes")) {
            for (String statKey : guiConfig.getConfigurationSection(basePath + ".attributes").getKeys(false)) {
                String path = basePath + ".attributes." + statKey;
                int slot = guiConfig.getInt(path + ".slot", -1);
                String materialStr = guiConfig.getString(path + ".material", "STONE");
                Material mat = Material.matchMaterial(materialStr);
                if (mat == null) { mat = Material.STONE; }
                String displayName = guiConfig.getString(path + ".displayName", statKey);
                java.util.List<String> loreList = guiConfig.getStringList(path + ".lore");

                // Replace tokens using the advanced stat formula
                double value = statsManager.calculateStat(statKey, level);
                AdvancedStatsManager.StatFormula formula = statsManager.getAllFormulas().get(statKey);
                double baseVal = formula != null ? formula.getBase() : 0;
                double perLevel = formula != null ? formula.getGrowth() : 0;
                displayName = displayName.replace("%value%", String.format("%.1f", value))
                                         .replace("%base%", String.format("%.1f", baseVal))
                                         .replace("%per_level%", String.format("%.1f", perLevel));

                java.util.List<String> replacedLore = new java.util.ArrayList<>();
                for(String line : loreList) {
                    replacedLore.add(line.replace("%value%", String.format("%.1f", value))
                                         .replace("%base%", String.format("%.1f", baseVal))
                                         .replace("%per_level%", String.format("%.1f", perLevel)));
                }

                ItemStack item = new ItemStack(mat);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName(displayName);
                meta.setLore(replacedLore);
                item.setItemMeta(meta);
                if (slot >= 0 && slot < size) {
                    inv.setItem(slot, item);
                }
            }
        }
        // Populate available stat points item
        if (guiConfig.isConfigurationSection(basePath + ".availablePoints")) {
            String apath = basePath + ".availablePoints";
            int slot = guiConfig.getInt(apath + ".slot", -1);
            String materialStr = guiConfig.getString(apath + ".material", "EMERALD");
            Material mat = Material.matchMaterial(materialStr);
            if (mat == null) { mat = Material.EMERALD; }
            String displayName = guiConfig.getString(apath + ".displayName", "Available Stat Points: %points%");
            java.util.List<String> loreList = guiConfig.getStringList(apath + ".lore");
            int freePoints = ServiceRegistry.getStatsManager().getAllocation(player).freeStatPoints;
            displayName = displayName.replace("%points%", String.valueOf(freePoints));
            java.util.List<String> replacedLore = new java.util.ArrayList<>();
            for(String line : loreList) {
                replacedLore.add(line.replace("%points%", String.valueOf(freePoints)));
            }
            ItemStack item = new ItemStack(mat);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(displayName);
            meta.setLore(replacedLore);
            item.setItemMeta(meta);
            if (slot >= 0 && slot < size) {
                inv.setItem(slot, item);
            }
        }
        return inv;
    }
} 