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
    private final List<String> loreTemplate;

    public AdvancedStatsGUIHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        File statsFile = new File(plugin.getDataFolder(), "stats.yml");
        if (!statsFile.exists()) {
            plugin.saveResource("stats.yml", false);
        }
        this.statsManager = new AdvancedStatsManager(plugin.getDataFolder());
        FileConfiguration config = YamlConfiguration.loadConfiguration(statsFile);
        this.title = config.getString("ui.title", "Player Stats");
        this.loreTemplate = config.getStringList("ui.lore");
    }

    // Creates an inventory GUI showing the player's computed stats.
    public Inventory createStatsGUI(Player player, int level) {
        Inventory inv = Bukkit.createInventory(null, 9, Component.text(title));
        List<String> replacedLore = new ArrayList<>();
        for (String line : loreTemplate) {
            // Replace tokens of the format {StatName} with the calculated values.
            for (String statKey : statsManager.getAllFormulas().keySet()) {
                double value = statsManager.calculateStat(statKey, level);
                line = line.replace("{" + statKey + "}", String.format("%.1f", value));
            }
            replacedLore.add(line);
        }
        // Create an item (using PAPER) to present the stats.
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(title);
        meta.setLore(replacedLore);
        item.setItemMeta(meta);
        inv.setItem(4, item);
        return inv;
    }
} 