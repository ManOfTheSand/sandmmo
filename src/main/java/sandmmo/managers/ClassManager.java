package sandmmo.managers;

import com.sandmmo.SandMMO;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import java.util.Map;

public class ClassManager {
    private final SandMMO plugin;

    public ClassManager(SandMMO plugin) {
        this.plugin = plugin;
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
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        NamespacedKey skillKey = new NamespacedKey(plugin, "skill");

        return pdc.get(skillKey, PersistentDataType.STRING);
    }

    public ItemStack createSkillItem(String skillName, Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();

        NamespacedKey key = new NamespacedKey(plugin, "skill");
        meta.getPersistentDataContainer().set(key, PersistentDataType.STRING, skillName);

        meta.setDisplayName("§b" + skillName + " Skill");
        item.setItemMeta(meta);

        return item;
    }

    public Map<String, String> getAllClasses() {
        Map<String, String> classes = new HashMap<>();

        // Get all classes from config
        if (plugin.getConfig().contains("classes")) {
            for (String className : plugin.getConfig().getConfigurationSection("classes").getKeys(false)) {
                // Get display name from config or generate one
                String displayName = plugin.getConfig().getString("classes." + className + ".displayName");
                if (displayName == null) {
                    displayName = className.substring(0, 1).toUpperCase() +
                            className.substring(1).toLowerCase();
                }
                classes.put(className, displayName);
            }
        }
        return classes;
    }
}