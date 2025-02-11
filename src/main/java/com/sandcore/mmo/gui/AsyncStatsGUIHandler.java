package com.sandcore.mmo.gui;

import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class AsyncStatsGUIHandler implements Listener {

    private final JavaPlugin plugin;
    private YamlConfiguration config;
    // Track inventories we create for item protection.
    private final Set<Inventory> statsInventories = new HashSet<>();

    public AsyncStatsGUIHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        reloadConfiguration();
    }

    // Asynchronously load the stats GUI configuration from "statsgui.yml"
    public void reloadConfiguration() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    // Attempt to load statsgui.yml from the plugin's data folder.
                    File configFile = new File(plugin.getDataFolder(), "statsgui.yml");
                    final YamlConfiguration loadedConfig;
                    if (configFile.exists()) {
                        loadedConfig = YamlConfiguration.loadConfiguration(configFile);
                    } else {
                        // Fallback: load from the jar resources
                        try (InputStream input = getClass().getResourceAsStream("/statsgui.yml")) {
                            if (input == null) {
                                plugin.getLogger().warning("Stats GUI config (statsgui.yml) not found in data folder or resources!");
                                return;
                            }
                            loadedConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(input));
                        }
                    }
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            config = loadedConfig;
                            plugin.getLogger().info("Stats GUI configuration reloaded successfully.");
                        }
                    }.runTask(plugin);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Error reloading statsgui.yml", e);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    // Build and open the stats GUI for a given player.
    public void openGUI(final Player player) {
        if (config == null) {
            player.sendMessage(ChatColor.RED + "Stats GUI is not loaded. Please try again later.");
            return;
        }

        // Retrieve the StatsManager via ServiceRegistry.
        final StatsManager statsManager = ServiceRegistry.getStatsManager();
        if (statsManager == null) {
            player.sendMessage(ChatColor.RED + "Stats system is not available.");
            return;
        }

        // Build the GUI based on the YAML configuration.
        int size = config.getInt("stats_gui.size", 27);
        String title = ChatColor.translateAlternateColorCodes('&', config.getString("stats_gui.title", "Player Stats"));
        final Inventory gui = Bukkit.createInventory(null, size, title);

        // Process each attribute defined under "stats_gui.attributes"
        ConfigurationSection attributesSec = config.getConfigurationSection("stats_gui.attributes");
        if (attributesSec != null) {
            for (String attributeKey : attributesSec.getKeys(false)) {
                ConfigurationSection attrSec = attributesSec.getConfigurationSection(attributeKey);
                if (attrSec == null) continue;
                int slot = attrSec.getInt("slot", -1);
                if (slot < 0 || slot >= size) continue;

                Material material = Material.matchMaterial(attrSec.getString("material", "STONE"));
                if (material == null)
                    material = Material.STONE;

                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();

                // Replace placeholders in display name and lore using the player's stats.
                String displayName = ChatColor.translateAlternateColorCodes('&', attrSec.getString("displayName", attributeKey));
                displayName = replacePlaceholders(displayName, statsManager, player, attributeKey);
                meta.setDisplayName(displayName);

                List<String> loreList = attrSec.getStringList("lore");
                if (loreList != null && !loreList.isEmpty()) {
                    List<String> newLore = loreList.stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', replacePlaceholders(line, statsManager, player, attributeKey)))
                        .collect(Collectors.toList());
                    meta.setLore(newLore);
                }
                item.setItemMeta(meta);
                gui.setItem(slot, item);
            }
        }

        // Process the available stat points display.
        ConfigurationSection availSec = config.getConfigurationSection("stats_gui.availablePoints");
        if (availSec != null) {
            int slot = availSec.getInt("slot", -1);
            if (slot >= 0 && slot < size) {
                Material material = Material.matchMaterial(availSec.getString("material", "EMERALD"));
                if (material == null)
                    material = Material.EMERALD;
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                String dispName = ChatColor.translateAlternateColorCodes('&', availSec.getString("displayName", "&eAvailable Stat Points: %points%"));
                int points = statsManager.getAvailablePoints(player);
                dispName = dispName.replace("%points%", String.valueOf(points));
                meta.setDisplayName(dispName);

                List<String> loreList = availSec.getStringList("lore");
                if (loreList != null && !loreList.isEmpty()) {
                    List<String> newLore = loreList.stream()
                        .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                        .collect(Collectors.toList());
                    meta.setLore(newLore);
                }
                item.setItemMeta(meta);
                gui.setItem(slot, item);
            }
        }

        // Add to our set for GUI protection.
        statsInventories.add(gui);

        // Open the GUI on the main thread with auditory feedback.
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(gui);
                player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            }
        }.runTask(plugin);
    }

    // Replace placeholders in text with dynamic stats values.
    // %value% = current computed stat value.
    // %base% = the base stat value.
    // %per_level% = per-level increment.
    private String replacePlaceholders(String text, StatsManager statsManager, Player player, String attributeKey) {
        double value = statsManager.getStatValue(player, attributeKey);
        double base = statsManager.getBaseStat(player, attributeKey);
        double perLevel = statsManager.getPerLevelIncrement(player, attributeKey);

        text = text.replace("%value%", String.valueOf(value));
        text = text.replace("%base%", String.valueOf(base));
        text = text.replace("%per_level%", String.valueOf(perLevel));
        return text;
    }

    // Lock the GUI—the click events are cancelled.
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (statsInventories.contains(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    // Lock drag events to prevent item modifications.
    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (statsInventories.contains(event.getInventory())) {
            event.setCancelled(true);
        }
    }
} 