package com.sandmmo.gui;

import com.sandmmo.SandMMO;
import com.sandmmo.classes.PlayerClass;
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

import java.util.List;
import java.util.stream.Collectors;

public class ClassSelectionGUI implements Listener {
    private final SandMMO plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ClassSelectionGUI(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        String title = plugin.getGuiConfig().get().getString("gui.titles.class-select");
        int size = plugin.getGuiConfig().get().getInt("gui.sizes.class-select", 27);

        Inventory gui = Bukkit.createInventory(null, size, mm.deserialize(title));

        plugin.getClassManager().getClasses().forEach((id, clazz) -> {
            ItemStack item = new ItemStack(Material.matchMaterial(
                    plugin.getGuiConfig().get().getString("gui.items." + id + ".material", "BOOK")
            ));

            ItemMeta meta = item.getItemMeta();
            meta.displayName(mm.deserialize(clazz.getColor() + clazz.getDisplayName()));

            List<String> lore = plugin.getGuiConfig().get().getStringList("gui.items." + id + ".lore");
            meta.setLore(lore.stream().map(line -> mm.serialize(mm.deserialize(line))).collect(Collectors.toList()));

            item.setItemMeta(meta);
            gui.addItem(item);
        });

        player.openInventory(gui);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        String targetTitle = plugin.getGuiConfig().get().getString("gui.titles.class-select");
        if (event.getView().title().equals(mm.deserialize(targetTitle))) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getWhoClicked() instanceof Player player) {
                PlayerClass selectedClass = plugin.getClassManager().getClasses().values().stream()
                        .filter(clazz -> clazz.getDisplayName().equals(event.getCurrentItem().getItemMeta().getDisplayName()))
                        .findFirst()
                        .orElse(null);

                if (selectedClass != null) {
                    plugin.getPlayerDataManager().getPlayerData(player).setPlayerClass(selectedClass);
                    player.sendMessage(mm.deserialize(plugin.getMessagesConfig().get().getString("messages.class-selected")
                            .replace("{class}", selectedClass.getDisplayName())));
                } else {
                    player.sendMessage(mm.deserialize(plugin.getMessagesConfig().get().getString("messages.invalid-class")));
                }
                player.closeInventory();
            }
        }
    }
}