package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.willfp.eco.core.gui.EcoGui;
import com.willfp.eco.core.gui.EcoGuiItem;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.entity.Player;

public class MMOGUI {

    private final SandMMO plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public MMOGUI(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        // Load GUI settings from config (with defaults)
        String titleStr = plugin.getConfig().getString("menu.title", "<gold>MMO GUI");
        int rows = plugin.getConfig().getInt("menu.rows", 1); // number of rows (each row has 9 slots)
        String itemTextStr = plugin.getConfig().getString("menu.item", "<blue>Click Me!");

        Component title = miniMessage.deserialize(titleStr);
        Component itemText = miniMessage.deserialize(itemTextStr);

        // Build the Eco GUI using Eco's built-in system
        EcoGui gui = EcoGui.builder()
                .plugin(plugin)
                .title(title)
                .rows(rows)
                .build();

        // Create an item with Eco's GUI item builder
        ItemStack icon = new ItemStack(Material.DIAMOND);
        EcoGuiItem guiItem = EcoGuiItem.builder()
                .icon(icon)
                .displayText(itemText)
                .clickAction(event -> {
                    event.getWhoClicked().sendMessage(miniMessage.deserialize("<green>You clicked the diamond!"));
                })
                .build();

        // Calculate a center slot (for example)
        int totalSlots = rows * 9;
        int centerSlot = totalSlots / 2;
        gui.setItem(centerSlot, guiItem);

        gui.open(player);
    }
}