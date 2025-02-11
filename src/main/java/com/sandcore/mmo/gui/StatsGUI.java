package com.sandcore.mmo.gui;

import com.willfp.eco.core.gui.menu.MenuBuilder;
import com.willfp.eco.core.gui.slot.impl.SlotFiller;
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
    private final MenuBuilder menu;
    private final FileConfiguration config;
    private final StatsManager statsManager;

    public StatsGUI(Player player, int playerLevel) {
        this.statsManager = ServiceRegistry.getStatsManager();
        
        // Load config
        InputStream input = getClass().getClassLoader().getResourceAsStream("stats.yml");
        this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(input, StandardCharsets.UTF_8));
        
        // Create Eco menu
        this.menu = MenuBuilder.builder(config.getInt("gui.size", 27))
                .setTitle(config.getString("gui.title", "Player Stats"))
                .setPreventClicks(true)
                .setPreventDrags(true);

        // Add slots during build phase
        for (String key : config.getConfigurationSection("gui.items").getKeys(false)) {
            String path = "gui.items." + key;
            int slot = config.getInt(path + ".slot");
            menu.setSlot(slot, new SlotFiller(createItem(player, playerLevel, path)));
        }
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
        menu.build().open(player);
    }
} 