package com.sandcore.mmo.gui;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.configuration.file.FileConfiguration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Material;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Player;

public abstract class BaseGUI {
    protected Inventory inventory;
    protected final FileConfiguration config;
    protected final String guiKey;
    protected final MiniMessage miniMessage = MiniMessage.miniMessage();
    protected final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();

    public BaseGUI(FileConfiguration config, String guiKey) {
        this.config = config;
        this.guiKey = guiKey;
        loadGUI();
    }

    protected void loadGUI() {
        // Load title and size from configuration using the guiKey (e.g., "gui.class")
        String titleConfig = config.getString(guiKey + ".title", "GUI");
        int size = config.getInt(guiKey + ".size", 9);
        // Deserialize hex color support via MiniMessage then serialize into legacy format.
        String legacyTitle = legacySerializer.serialize(miniMessage.deserialize(titleConfig));
        inventory = Bukkit.createInventory(null, size, legacyTitle);

        // Load configured items (each with slot, material, and name)
        if (config.isConfigurationSection(guiKey + ".items")) {
            for (String key : config.getConfigurationSection(guiKey + ".items").getKeys(false)) {
                String path = guiKey + ".items." + key;
                int slot = config.getInt(path + ".slot", -1);
                String materialStr = config.getString(path + ".material", "STONE");
                String itemName = config.getString(path + ".name", key);
                Material material;
                try {
                    material = Material.valueOf(materialStr.toUpperCase());
                } catch (Exception ex) {
                    material = Material.STONE;
                }
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(legacySerializer.serialize(miniMessage.deserialize(itemName)));
                    item.setItemMeta(meta);
                }
                if (slot >= 0 && slot < inventory.getSize()) {
                    inventory.setItem(slot, item);
                }
            }
        }
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
} 