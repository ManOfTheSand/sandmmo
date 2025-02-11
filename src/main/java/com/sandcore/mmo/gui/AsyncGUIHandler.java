package com.sandcore.mmo.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class AsyncGUIHandler implements Listener {
    private final JavaPlugin plugin;
    private final Map<String, Inventory> guiCache = new HashMap<>();
    private YamlConfiguration config;

    public AsyncGUIHandler(JavaPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        reloadConfiguration();
    }

    public void reloadConfiguration() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try (InputStream input = getClass().getResourceAsStream("/gui.yml")) {
                    if (input == null) {
                        plugin.getLogger().warning("GUI configuration file not found in resources!");
                        return;
                    }
                    
                    YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(input));
                    
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            config = newConfig;
                            rebuildGUICache();
                            plugin.getLogger().info("Successfully reloaded GUI configuration");
                        }
                    }.runTask(plugin);
                } catch (Exception e) {
                    plugin.getLogger().log(Level.SEVERE, "Failed to load GUI configuration", e);
                }
            }
        }.runTaskAsynchronously(plugin);
    }

    private void rebuildGUICache() {
        guiCache.clear();
        for (String guiKey : config.getKeys(false)) {
            int size = config.getInt(guiKey + ".size", 9);
            String title = config.getString(guiKey + ".title", "GUI");
            
            Inventory inv = Bukkit.createInventory(null, size, title);
            
            for (String itemKey : config.getConfigurationSection(guiKey + ".items").getKeys(false)) {
                String path = guiKey + ".items." + itemKey;
                int slot = config.getInt(path + ".slot");
                ItemStack item = buildItem(config.getString(path + ".material", "STONE"),
                                          config.getString(path + ".name"),
                                          config.getStringList(path + ".lore"));
                
                if (slot >= 0 && slot < size) {
                    inv.setItem(slot, item);
                }
            }
            guiCache.put(guiKey, inv);
        }
    }

    private ItemStack buildItem(String material, String name, List<String> lore) {
        Material mat = Material.matchMaterial(material);
        if (mat == null) mat = Material.STONE;
        
        ItemStack item = new ItemStack(mat);
        ItemMeta meta = item.getItemMeta();
        
        if (name != null) meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        if (lore != null) {
            List<String> coloredLore = lore.stream()
                .map(line -> ChatColor.translateAlternateColorCodes('&', line))
                .collect(Collectors.toList());
            meta.setLore(coloredLore);
        }
        
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (guiCache.containsValue(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        if (guiCache.containsValue(event.getInventory())) {
            event.setCancelled(true);
        }
    }

    public void openGUI(Player player, String guiKey) {
        Inventory gui = guiCache.get(guiKey);
        if (gui != null) {
            player.openInventory(gui);
        } else {
            player.sendMessage(ChatColor.RED + "Requested GUI is not available");
        }
    }

    public void updateGUIAsync(String guiKey) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (config != null) {
                    rebuildGUICache();
                }
            }
        }.runTaskAsynchronously(plugin);
    }
} 