package com.sandcore.mmo.stats;

import com.sandcore.mmo.util.ServiceRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Builds the Stats GUI inventory using a single item.
 * This item is placed in the center (e.g. slot 13 for a 27-slot inventory)
 * and its display name and lore include placeholders for stats:
 *   %maxHealth%, %maxMana%, %healthRegen%, %manaRegen%, %strength%, %dexterity%, %defense%, %magicDamage%.
 *
 * Hex color support is provided using the MiniMessage API.
 */
public class StatsGUI {

    private final StatsGUIConfig guiConfig;
    private final StatsManager statsManager;
    private final ClassesConfig classesConfig;
    private final int size;
    private final String title;
    private final Material background;
    // Define the stat keys to display.
    private final String[] statKeys = {"maxHealth", "maxMana", "healthRegen", "manaRegen", "strength", "dexterity", "defense", "magicDamage"};

    private final MiniMessage miniMessage = MiniMessage.miniMessage();
    private final LegacyComponentSerializer legacySerializer = LegacyComponentSerializer.legacyAmpersand();

    public StatsGUI(StatsGUIConfig config, StatsManager statsManager, ClassesConfig classesConfig) {
        this.guiConfig = config;
        this.statsManager = statsManager;
        this.classesConfig = classesConfig;
        // Use MiniMessage to parse hex color text.
        this.title = legacySerializer.serialize(miniMessage.deserialize(guiConfig.getTitle()));
        this.size = guiConfig.getSize();
        this.background = guiConfig.getBackgroundMaterial();
    }

    /**
     * Builds the Stats GUI inventory.
     * A single main item is placed in the configuration-specified slot and its lore 
     * contains placeholders for stats, which are replaced by computed stat values.
     *
     * @param player the player for whom to build the GUI
     * @return the constructed Inventory
     */
    public Inventory build(Player player) {
        Inventory inv = Bukkit.createInventory(new StatsGUIHolder(), size, Component.text(title));

        // Fill every slot with the background item.
        ItemStack bgItem = new ItemStack(background);
        ItemMeta bgMeta = bgItem.getItemMeta();
        bgMeta.setDisplayName(" ");
        bgItem.setItemMeta(bgMeta);
        for (int i = 0; i < size; i++) {
            inv.setItem(i, bgItem);
        }

        // Compute base stat values.
        double finalMaxHealth = statsManager.getStatValue(player, "maxHealth");
        double finalMaxMana = statsManager.getStatValue(player, "maxMana");
        double finalHealthRegen = statsManager.getStatValue(player, "healthRegen");
        double finalManaRegen = statsManager.getStatValue(player, "manaRegen");
        double finalStrength = statsManager.getStatValue(player, "strength");
        double finalDexterity = statsManager.getStatValue(player, "dexterity");
        double finalDefense = statsManager.getStatValue(player, "defense");
        double finalMagicDamage = statsManager.getStatValue(player, "magicDamage");

        // Retrieve bonus values from the player's chosen class.
        String playerClass = ServiceRegistry.getClassManager().getPlayerClassId(player);
        if (playerClass != null) {
            ClassStats classStats = classesConfig.getClassStats(playerClass);
            finalMaxHealth += classStats.getBonus("maxHealth");
            finalMaxMana += classStats.getBonus("maxMana");
            finalHealthRegen += classStats.getBonus("healthRegen");
            finalManaRegen += classStats.getBonus("manaRegen");
            finalStrength += classStats.getBonus("strength");
            finalDexterity += classStats.getBonus("dexterity");
            finalDefense += classStats.getBonus("defense");
            finalMagicDamage += classStats.getBonus("magicDamage");
        }

        // Get the main item configuration.
        ConfigurationSection mainSec = guiConfig.getMainItemSection();
        if (mainSec == null) return inv;
        int slot = mainSec.getInt("slot", size / 2);
        String matStr = mainSec.getString("material", "PAPER");
        Material mat = Material.getMaterial(matStr.toUpperCase());
        if (mat == null) mat = Material.PAPER;

        ItemStack mainItem = new ItemStack(mat);
        ItemMeta meta = mainItem.getItemMeta();
        String displayName = mainSec.getString("display_name", "&6Your Stats Overview");
        displayName = legacySerializer.serialize(miniMessage.deserialize(displayName));
        meta.setDisplayName(displayName);

        // Process lore lines and replace placeholders.
        List<String> lore = mainSec.getStringList("lore");
        if (lore != null && !lore.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                String line = lore.get(i);
                // Replace placeholders first.
                line = line.replace("%maxHealth%", String.valueOf(finalMaxHealth))
                           .replace("%maxMana%", String.valueOf(finalMaxMana))
                           .replace("%healthRegen%", String.valueOf(finalHealthRegen))
                           .replace("%manaRegen%", String.valueOf(finalManaRegen))
                           .replace("%strength%", String.valueOf(finalStrength))
                           .replace("%dexterity%", String.valueOf(finalDexterity))
                           .replace("%defense%", String.valueOf(finalDefense))
                           .replace("%magicDamage%", String.valueOf(finalMagicDamage));
                // Process hex colors via MiniMessage.
                line = legacySerializer.serialize(miniMessage.deserialize(line));
                lore.set(i, line);
            }
            meta.setLore(lore);
        }
        mainItem.setItemMeta(meta);
        inv.setItem(slot, mainItem);
        return inv;
    }
} 