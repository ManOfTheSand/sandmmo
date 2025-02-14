package com.sandcore.mmo.stats;

import com.sandcore.mmo.util.ServiceRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Builds the stats GUI inventory.
 * For each stat, it reads its display configuration (slot, icon, display name, lore)
 * from the config.yml and replaces the %value% placeholder with the computed stat value.
 *
 * The final stat value is determined by the base value from StatsManager plus any bonus from the chosen class
 * (retrieved via ClassesConfig).
 */
public class StatsGUI {

    private final StatsGUIConfig guiConfig;
    private final StatsManager statsManager;
    private final ClassesConfig classesConfig;
    private final int size;
    private final String title;
    private final Material background;
    // Stat keys to display.
    private final String[] statKeys = {"maxHealth", "maxMana", "healthRegen", "manaRegen", "strength", "dexterity", "defense", "magicDamage"};

    public StatsGUI(StatsGUIConfig config, StatsManager statsManager, ClassesConfig classesConfig) {
        this.guiConfig = config;
        this.statsManager = statsManager;
        this.classesConfig = classesConfig;
        this.title = ChatColor.translateAlternateColorCodes('&', guiConfig.getTitle());
        this.size = guiConfig.getSize();
        this.background = guiConfig.getBackgroundMaterial();
    }

    /**
     * Builds and returns the Inventory representing the stats GUI.
     *
     * @param player the player for whom to construct the GUI.
     * @return the constructed Inventory.
     */
    public Inventory build(Player player) {
        Inventory inv = Bukkit.createInventory(new StatsGUIHolder(), size, Component.text(title));

        // Fill all slots with the background item.
        ItemStack bgItem = new ItemStack(background);
        ItemMeta bgMeta = bgItem.getItemMeta();
        bgMeta.setDisplayName(" ");
        bgItem.setItemMeta(bgMeta);
        for (int i = 0; i < size; i++) {
            inv.setItem(i, bgItem);
        }

        // Retrieve the player's chosen class from the ClassManager.
        // (Assume ServiceRegistry.getClassManager().getPlayerClassId(player) returns a String such as "warrior" or "mage".)
        String playerClass = ServiceRegistry.getClassManager().getPlayerClassId(player);
        ClassStats classStats = null;
        if (playerClass != null) {
            classStats = classesConfig.getClassStats(playerClass);
        }

        // Process each stat.
        for (String key : statKeys) {
            ConfigurationSection sec = guiConfig.getStatSection(key);
            if (sec == null) continue;
            int slot = sec.getInt("slot", -1);
            if (slot < 0 || slot >= size) continue;

            String matStr = sec.getString("material", "PAPER");
            Material mat = Material.getMaterial(matStr.toUpperCase());
            if (mat == null) {
                mat = Material.PAPER;
            }
            ItemStack statItem = new ItemStack(mat);
            ItemMeta meta = statItem.getItemMeta();

            // Retrieve the base stat value.
            double baseValue = statsManager.getStatValue(player, key);
            // Add bonus from the chosen class (if any).
            if (classStats != null) {
                baseValue += classStats.getBonus(key);
            }

            // Process display name.
            String displayName = sec.getString("display_name", key + ": %value%");
            displayName = ChatColor.translateAlternateColorCodes('&', displayName.replace("%value%", String.valueOf(baseValue)));
            meta.setDisplayName(displayName);

            // Process lore.
            List<String> loreList = sec.getStringList("lore");
            if (loreList != null && !loreList.isEmpty()) {
                for (int i = 0; i < loreList.size(); i++) {
                    String line = loreList.get(i);
                    line = ChatColor.translateAlternateColorCodes('&', line.replace("%value%", String.valueOf(baseValue)));
                    loreList.set(i, line);
                }
                meta.setLore(loreList);
            }

            statItem.setItemMeta(meta);
            inv.setItem(slot, statItem);
        }
        return inv;
    }
} 