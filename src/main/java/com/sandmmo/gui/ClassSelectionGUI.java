package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.classes.PlayerClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClassSelectionGUI {
    public static final MiniMessage mm = MiniMessage.miniMessage();
    private final SandMMO plugin;

    public ClassSelectionGUI(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 9,
                mm.deserialize("<gold>Choose Your Class"));

        for (String className : plugin.getClassManager().getAvailableClasses()) {
            PlayerClass pc = plugin.getClassManager().getClass(className);
            ItemStack item = createClassItem(pc);
            gui.addItem(item);
        }

        player.openInventory(gui);
    }

    private ItemStack createClassItem(PlayerClass pc) {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(mm.deserialize(pc.getDisplayName()));
        // Add lore with class stats
        item.setItemMeta(meta);
        return item;
    }
}