package com.sandmmo.gui;

import com.sandmmo.config.ClassesConfig;
import com.willfp.eco.core.gui.menu.Menu;
import com.willfp.eco.core.gui.menu.MenuBuilder;
import com.willfp.eco.core.gui.slot.FillerSlot;
import com.willfp.eco.core.gui.slot.Slot;
import com.willfp.eco.core.items.builder.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ClassGUI {
    private final Menu menu;
    private final ClassesConfig classesConfig;

    public ClassGUI(ClassesConfig config) {
        this.classesConfig = config;

        MenuBuilder builder = Menu.builder(27)
                .setTitle(classesConfig.getFormattedString("guis.class-selection.title"));

        // Fill background
        builder.setFillerSlot(FillerSlot.builder()
                .setItem(new ItemStackBuilder(Material.GRAY_STAINED_GLASS_PANE)
                        .setDisplayName(" ")
                        .build())
                .build());

        // Add class items
        classesConfig.getClasses().forEach((className, data) -> {
            Slot slot = Slot.builder()
                    .setItem(new ItemStackBuilder(Material.valueOf(data.getIcon()))
                            .setDisplayName(data.getDisplayName())
                            .setLore(data.getDescription())
                            .build())
                    .setOnClick((event, menu) -> {
                        Player player = (Player) event.getWhoClicked();
                        // Handle class selection
                        player.closeInventory();
                    })
                    .build();

            builder.addSlot(slot);
        });

        // Add info item
        builder.addSlot(Slot.builder()
                .setItem(new ItemStackBuilder(Material.BOOK)
                        .setDisplayName(classesConfig.getFormattedString("guis.class-selection.items.info.name"))
                        .setLore(classesConfig.getFormattedStrings("guis.class-selection.items.info.lore"))
                        .build())
                .build(), 22);

        this.menu = builder.build();
    }

    public void open(@NotNull Player player) {
        menu.open(player);
    }
}