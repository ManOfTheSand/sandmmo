package com.sandmmo.gui;

import com.sandmmo.config.ClassesConfig;
import com.sandmmo.managers.ClassManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ClassGUI { // Fixed class name capitalization
    private final Inventory inventory;
    private final ClassesConfig config;
    private final ClassManager classManager;

    public ClassGUI(ClassesConfig config, ClassManager classManager) {
        this.config = config;
        this.classManager = classManager;
        this.inventory = Bukkit.createInventory(null, 27,
                Component.text("Class Selection"));

        initializeItems();
    }

    private void initializeItems() {
        config.getClasses().forEach((id, mmoClass) -> {
            ItemStack item = new ItemStack(Material.PAPER);
            ItemMeta meta = item.getItemMeta();

            meta.displayName(Component.text(mmoClass.displayName()));
            List<Component> lore = new ArrayList<>();
            mmoClass.description().forEach(line ->
                    lore.add(Component.text(line)));

            meta.lore(lore);
            item.setItemMeta(meta);
            inventory.addItem(item);
        });
    }

    public void open(Player player) {
        player.openInventory(inventory);
    }
}