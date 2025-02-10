package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.HashMap;
import java.util.Map;

public class ClassManager {
    private final SandMMO plugin;

    public ClassManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void selectClass(Player player, String className) {
        Map<String, String> classes = getAllClasses();
        if (!classes.containsKey(className.toLowerCase())) {
            player.sendMessage("§cInvalid class!");
            return;
        }

        Map<String, Double> stats = getClassStats(className, 1);
        plugin.getSkillManager().applyClassStats(player, className);
        plugin.getPlayerManager().setPlayerClass(player.getUniqueId(), className);

        player.sendMessage("§aYou have selected the " + className + " class!");
        player.sendMessage("§7Stats applied: " + stats.toString());
    }

    public String getClassNameFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey classKey = new NamespacedKey(plugin, "class");

        if (pdc.has(classKey, PersistentDataType.STRING)) {
            return pdc.get(classKey, PersistentDataType.STRING);
        }
        return null;
    }

    public ItemStack createClassItem(String className, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "class");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, className);
        meta.setDisplayName("§e" + className + " Class");
        item.setItemMeta(meta);
        return item;
    }

    public String getSkillFromItem(ItemStack item) {
        if (item == null || !item.hasItemMeta()) return null;

        ItemMeta meta = item.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey skillKey = new NamespacedKey(plugin, "skill");

        if (container.has(skillKey, PersistentDataType.STRING)) {
            return container.get(skillKey, PersistentDataType.STRING);
        }
        return null;
    }

    public Map<String, Double> getClassStats(String className, int level) {
        Map<String, Double> stats = new HashMap<>();
        FileConfiguration config = plugin.getClassesConfig();

        if (config.contains(className + ".stats")) {
            ConfigurationSection statsSection = config.getConfigurationSection(className + ".stats");
            for (String stat : statsSection.getKeys(false)) {
                stats.put(stat, statsSection.getDouble(stat));
            }
        }
        return stats;
    }

    public Map<String, String> getAllClasses() {
        Map<String, String> classes = new HashMap<>();
        FileConfiguration config = plugin.getClassesConfig();

        if (config != null) {
            for (String key : config.getKeys(false)) {
                String displayName = config.getString(key + ".displayName", key);
                classes.put(key.toLowerCase(), displayName);
            }
        }
        return classes;
    }
}