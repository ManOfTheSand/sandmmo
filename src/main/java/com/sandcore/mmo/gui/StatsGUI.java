package com.sandcore.mmo.gui;

import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.manager.StatsManager.PlayerStatAllocation;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class StatsGUI implements Listener {
    private final YamlConfiguration config;
    private final Inventory gui;
    private final String guiKey = "stats_gui";
    private final org.bukkit.plugin.java.JavaPlugin plugin;

    public StatsGUI(org.bukkit.plugin.java.JavaPlugin plugin) {
        this.plugin = plugin;
        // Attempt to load statsgui.yml from the plugin data folder; fallback to resource.
        File file = new File(plugin.getDataFolder(), "statsgui.yml");
        if (file.exists()) {
            config = YamlConfiguration.loadConfiguration(file);
        } else {
            YamlConfiguration temp;
            try (InputStream is = plugin.getResource("statsgui.yml")) {
                temp = YamlConfiguration.loadConfiguration(new InputStreamReader(is));
            } catch (Exception e) {
                plugin.getLogger().severe("Failed to load statsgui.yml: " + e.getMessage());
                temp = new YamlConfiguration();
            }
            config = temp;
        }
        // Build a fresh GUI from configuration.
        int size = config.getInt(guiKey + ".size", 27);
        String title = ChatColor.translateAlternateColorCodes('&', config.getString(guiKey + ".title", "Player Stats"));
        gui = Bukkit.createInventory(null, size, title);
        buildGUI();
    }

    private void buildGUI() {
        int size = gui.getSize();
        // Load attribute items.
        ConfigurationSection attributesSec = config.getConfigurationSection(guiKey + ".attributes");
        if (attributesSec != null) {
            for (String key : attributesSec.getKeys(false)) {
                ConfigurationSection attr = attributesSec.getConfigurationSection(key);
                if (attr == null) continue;
                int slot = attr.getInt("slot", -1);
                if (slot < 0 || slot >= size) continue;
                String matStr = attr.getString("material", "STONE");
                Material material = Material.matchMaterial(matStr);
                if (material == null) material = Material.STONE;
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                String displayName = ChatColor.translateAlternateColorCodes('&', attr.getString("displayName", key));
                meta.setDisplayName(displayName);
                List<String> lore = attr.getStringList("lore");
                if (lore != null && !lore.isEmpty()) {
                    lore = lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line))
                            .collect(Collectors.toList());
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);
                gui.setItem(slot, item);
            }
        }
        // Available stat points item.
        ConfigurationSection availSec = config.getConfigurationSection(guiKey + ".availablePoints");
        if (availSec != null) {
            int slot = availSec.getInt("slot", -1);
            if (slot >= 0 && slot < size) {
                String matStr = availSec.getString("material", "EMERALD");
                Material material = Material.matchMaterial(matStr);
                if (material == null) material = Material.EMERALD;
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                String displayName = ChatColor.translateAlternateColorCodes('&', availSec.getString("displayName", "Available Stat Points: %points%"));
                meta.setDisplayName(displayName);
                List<String> lore = availSec.getStringList("lore");
                if (lore != null && !lore.isEmpty()) {
                    lore = lore.stream().map(line -> ChatColor.translateAlternateColorCodes('&', line))
                            .collect(Collectors.toList());
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);
                gui.setItem(slot, item);
            }
        }
    }

    /**
     * Updates the GUI with dynamic stat values for the given player.
     */
    public void updateGUI(Player player) {
        StatsManager statsManager = ServiceRegistry.getStatsManager();
        if (statsManager == null) return;
        int size = gui.getSize();
        ConfigurationSection attributesSec = config.getConfigurationSection(guiKey + ".attributes");
        if (attributesSec != null) {
            for (String key : attributesSec.getKeys(false)) {
                ConfigurationSection attr = attributesSec.getConfigurationSection(key);
                if (attr == null) continue;
                int slot = attr.getInt("slot", -1);
                if (slot < 0 || slot >= size) continue;
                ItemStack item = gui.getItem(slot);
                if (item == null) continue;
                ItemMeta meta = item.getItemMeta();
                if (meta == null) continue;
                String displayName = ChatColor.translateAlternateColorCodes('&', attr.getString("displayName", key));
                double statValue = statsManager.recalcEffectiveAttribute(player, key);
                displayName = displayName.replace("%value%", String.valueOf(statValue));
                meta.setDisplayName(displayName);
                List<String> lore = attr.getStringList("lore");
                if (lore != null && !lore.isEmpty()) {
                    lore = lore.stream()
                            .map(line -> ChatColor.translateAlternateColorCodes('&', line).replace("%value%", String.valueOf(statValue)))
                            .collect(Collectors.toList());
                    meta.setLore(lore);
                }
                item.setItemMeta(meta);
                gui.setItem(slot, item);
            }
        }
        // Update available stat points.
        ConfigurationSection availSec = config.getConfigurationSection(guiKey + ".availablePoints");
        if (availSec != null) {
            int slot = availSec.getInt("slot", -1);
            if (slot >= 0 && slot < size) {
                ItemStack item = gui.getItem(slot);
                if (item != null) {
                    ItemMeta meta = item.getItemMeta();
                    if (meta != null) {
                        String displayName = ChatColor.translateAlternateColorCodes('&', availSec.getString("displayName", "Available Stat Points: %points%"));
                        PlayerStatAllocation alloc = statsManager.getAllocation(player);
                        displayName = displayName.replace("%points%", String.valueOf(alloc.freeStatPoints));
                        meta.setDisplayName(displayName);
                        item.setItemMeta(meta);
                        gui.setItem(slot, item);
                    }
                }
            }
        }
    }

    /**
     * Opens the stats GUI for the given player.
     */
    public void open(Player player) {
        updateGUI(player);
        player.openInventory(gui);
        player.playSound(player.getLocation(), org.bukkit.Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
    }
} 