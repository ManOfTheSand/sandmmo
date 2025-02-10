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

public class ClassSelectionGUI implements Listener {
    private final SandMMO plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ClassSelectionGUI(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        Inventory gui = Bukkit.createInventory(null, 27, mm.deserialize("<aqua>Select Your Class"));

        // Populate the GUI with class selection items
        String[] classes = plugin.getClassManager().getAvailableClasses();
        for (int i = 0; i < classes.length; i++) {
            String className = classes[i];
            PlayerClass playerClass = plugin.getClassManager().getClass(className);
            if (playerClass != null) {
                ItemStack item = new ItemStack(Material.BOOK);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(mm.deserialize("<yellow>" + playerClass.getDisplayName()));
                item.setItemMeta(meta);
                gui.setItem(i, item);
            }
        }

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().title().equals(mm.deserialize("<aqua>Select Your Class"))) {
            event.setCancelled(true);
            if (event.getCurrentItem() != null) {
                String className = event.getCurrentItem().getItemMeta().getDisplayName();
                // Handle class selection logic here
                event.getWhoClicked().sendMessage(mm.deserialize("<green>Selected class: " + className));
                event.getWhoClicked().closeInventory();
            }
        }
    }
}