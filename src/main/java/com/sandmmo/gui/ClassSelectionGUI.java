package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.classes.PlayerClass;
import com.sandmmo.player.PlayerData;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
                PlayerClass playerClass = plugin.getClassManager().getClass(className);
                if (playerClass != null) {
                    PlayerData playerData = plugin.getPlayerDataManager().getPlayerData((Player) event.getWhoClicked());
                    playerData.setPlayerClass(playerClass);
                    event.getWhoClicked().sendMessage(mm.deserialize("<green>Selected class: " + playerClass.getDisplayName()));
                } else {
                    event.getWhoClicked().sendMessage(mm.deserialize("<red>Invalid class selected!"));
                }
                event.getWhoClicked().closeInventory();
            }
        }
    }
}