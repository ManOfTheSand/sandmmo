package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassManager {
    private final SandMMO plugin;

    public ClassManager(SandMMO plugin) {
        this.plugin = plugin;
    }

    /**
     * Handles the selection of a class by the player.
     * Validates if the class exists, applies class stats, and saves the selection.
     *
     * @param player    The player selecting a class.
     * @param className The name/ID of the class to be selected.
     */
    public void selectClass(Player player, String className) {
        // Ensure the class exists (using a config section or default list)
        Map<String, String> availableClasses = getAllClasses();
        if (!availableClasses.containsKey(className.toLowerCase())) {
            player.sendMessage("§cInvalid class!");
            return;
        }

        // Apply the class stats using SkillManager (adjust if needed)
        plugin.getSkillManager().applyClassStats(player, className);

        // Save player's selected class using PlayerManager - ensure you have this method!
        plugin.getPlayerManager().setPlayerClass(player.getUniqueId(), className);

        // Provide feedback to the player
        player.sendMessage("§aYou have successfully selected the " + className + " class!");
    }

    /**
     * Retrieves the class name stored in an item's persistent data.
     *
     * @param item The item to check.
     * @return The stored class name, or null if none is found.
     */
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

    /**
     * Creates an item that represents a class.
     *
     * @param className The class name.
     * @param material  The material for the item.
     * @return The created ItemStack.
     */
    public ItemStack createClassItem(String className, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "class");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, className);
        meta.setDisplayName("§e" + className + " Class");
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Retrieves the skill name stored in an item's persistent data.
     *
     * @param item The item to check.
     * @return The stored skill name, or null if none is found.
     */
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

    /**
     * Creates an item that represents a skill.
     *
     * @param skillName The skill name.
     * @param material  The material for the item.
     * @return The created ItemStack.
     */
    public ItemStack createSkillItem(String skillName, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        NamespacedKey key = new NamespacedKey(plugin, "skill");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, skillName);
        meta.setDisplayName("§b" + skillName + " Skill");
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Retrieves class stats for a specific class at a given level from the config.
     *
     * @param className The class name.
     * @param level     The level for the stats.
     * @return A map containing stat names and their values.
     */
    public Map<String, Double> getClassStats(String className, int level) {
        return plugin.getConfig()
                .getConfigurationSection("classes." + className)
                .getValues(true)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> Double.parseDouble(e.getValue().toString())
                ));
    }

    /**
     * Retrieves all available classes from the configuration.
     *
     * @return A map where keys are class IDs and values are display names.
     */
    public Map<String, String> getAllClasses() {
        Map<String, String> classes = new HashMap<>();
        if (plugin.getConfig().contains("classes")) {
            plugin.getConfig().getConfigurationSection("classes")
                    .getKeys(false).forEach(key -> {
                        String displayName = plugin.getConfig().getString("classes." + key + ".displayName", key);
                        classes.put(key.toLowerCase(), displayName);
                    });
        }
        return classes;
    }

    // Any other methods that already exist in your ClassManager...
}