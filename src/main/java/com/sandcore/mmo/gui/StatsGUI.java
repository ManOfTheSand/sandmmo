package com.sandcore.mmo.gui;

import com.willfp.eco.core.gui.menu.Menu;
import com.willfp.eco.core.gui.slot.FillerSlot;
import org.bukkit.entity.Player;
import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.util.ServiceRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StatsGUI {
    private final Menu menu;
    private final FileConfiguration config;
    private final StatsManager statsManager;

    public StatsGUI(Player player, int playerLevel) {
        this.statsManager = ServiceRegistry.getStatsManager();
        
        // Load config
        InputStream input = getClass().getClassLoader().getResourceAsStream("stats.yml");
        this.config = YamlConfiguration.loadConfiguration(
                new InputStreamReader(input, StandardCharsets.UTF_8));
        
        // Create menu using Eco's builder pattern (pattern similar to Auxilor's EcoSkills)
        var builder = Menu.builder(27)
                .setTitle(config.getString("gui.title", "Player Stats"))
                .setPreventClick(true)
                .setPreventItemMove(true);

        // Add items from config
        for (String key : config.getConfigurationSection("gui.items").getKeys(false)) {
            String path = "gui.items." + key;
            int slot = config.getInt(path + ".slot");
            
            // Convert flat slot index into row & column (Eco uses 1-indexed rows and columns)
            int row = slot / 9 + 1;
            int column = slot % 9 + 1;
            
            builder.setSlot(row, column, new FillerSlot(createItem(player, playerLevel, path)));
        }

        this.menu = builder.build();
    }

    private ItemStack createItem(Player player, int level, String path) {
        Material material = Material.valueOf(config.getString(path + ".material", "STONE"));
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        String displayName = config.getString(path + ".name");
        displayName = replacePlaceholders(displayName, player, level);
        
        if (meta != null) {
            meta.setDisplayName(displayName);
            item.setItemMeta(meta);
        }
        return item;
    }

    private String replacePlaceholders(String text, Player player, int level) {
        return text.replace("%maxHealth%", String.valueOf(statsManager.getTotalMaxHealth(player, level)))
                .replace("%maxMana%", String.valueOf(statsManager.getTotalMaxMana(player, level)))
                .replace("%freePoints%", String.valueOf(statsManager.getPlayerStats(player.getUniqueId()).freeStatPoints));
    }

    public void open(Player player) {
        menu.open(player);
    }
} 