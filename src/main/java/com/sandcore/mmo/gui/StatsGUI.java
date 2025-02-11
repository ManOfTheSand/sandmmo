package com.sandcore.mmo.gui;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.util.ServiceRegistry;
import net.kyori.adventure.text.Component;
import com.willfp.eco.core.gui.GUI;
import com.willfp.eco.core.gui.menu.Menu;
import com.willfp.eco.core.gui.slot.Slot;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StatsGUI {
    private final Menu menu;
    private final FileConfiguration config;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();
    private final Player player;
    private final int playerLevel;
    private final StatsManager statsManager;

    public StatsGUI(Player player, int playerLevel) {
        this.player = player;
        this.playerLevel = playerLevel;
        // Load the external configuration from mmoMinecraft/stats.yml
        InputStream input = getClass().getClassLoader().getResourceAsStream("stats.yml");
        if (input == null) {
            throw new RuntimeException("Failed to load stats.yml");
        }
        this.config = YamlConfiguration.loadConfiguration(new InputStreamReader(input, StandardCharsets.UTF_8));
        // Build the inventory using settings from the YAML config.
        String titleRaw = config.getString("gui.title", "Player Stats");
        // Use MiniMessage to parse the title into an Adventure Component.
        net.kyori.adventure.text.Component titleComponent = miniMessage.deserialize(titleRaw);
        int size = config.getInt("gui.size", 27);
        this.menu = GUI.create(size)
            .setTitle(titleComponent)
            .setCancelClick(true) // This prevents item movement
            .build();
        this.statsManager = ServiceRegistry.getStatsManager();
        updateInventory();
    }

    // Update the inventory items with current stat values
    public void updateInventory() {
        if (config.isConfigurationSection("gui.items")) {
            for (String key : config.getConfigurationSection("gui.items").getKeys(false)) {
                String path = "gui.items." + key;
                int slot = config.getInt(path + ".slot", -1);
                String materialStr = config.getString(path + ".material", "STONE");
                Material material;
                try {
                    material = Material.valueOf(materialStr.toUpperCase());
                } catch (Exception ex) {
                    material = Material.STONE;
                }
                String displayName = config.getString(path + ".name", key);
                // Replace configuration placeholders with the player's current stats.
                displayName = displayName.replace("%maxHealth%", String.valueOf(statsManager.getTotalMaxHealth(player, playerLevel)));
                displayName = displayName.replace("%maxMana%", String.valueOf(statsManager.getTotalMaxMana(player, playerLevel)));
                displayName = displayName.replace("%healthRegen%", String.valueOf(statsManager.getTotalHealthRegen(player, playerLevel)));
                displayName = displayName.replace("%manaRegen%", String.valueOf(statsManager.getTotalManaRegen(player, playerLevel)));
                displayName = displayName.replace("%strength%", String.valueOf(statsManager.getTotalStrength(player, playerLevel)));
                displayName = displayName.replace("%dexterity%", String.valueOf(statsManager.getTotalDexterity(player, playerLevel)));
                displayName = displayName.replace("%intellect%", String.valueOf(statsManager.getTotalIntellect(player, playerLevel)));
                int freePoints = statsManager.getPlayerStats(player.getUniqueId()).freeStatPoints;
                displayName = displayName.replace("%freePoints%", String.valueOf(freePoints));
                
                ItemStack item = new ItemStack(material);
                ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(legacySerializer.serialize(miniMessage.deserialize(displayName)));
                    // Optionally load lore from the config if defined.
                    if (config.isList(path + ".lore")) {
                        meta.setLore(config.getStringList(path + ".lore"));
                    }
                    item.setItemMeta(meta);
                }
                if (slot >= 0 && slot < menu.getSize()) {
                    menu.addItem(slot, item);
                }
            }
        }
    }

    public void open() {
        updateInventory();
        menu.open(player);
    }

    public Component getTitleComponent() {
        return miniMessage.deserialize(config.getString("gui.title", "Player Stats"));
    }
} 