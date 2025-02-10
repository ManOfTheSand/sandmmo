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

public class ClassSelectionGUI implements Listener {
    private final SandMMO plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ClassSelectionGUI(SandMMO plugin) {
        this.plugin = plugin;
    }

    public void open(Player player) {
        FileConfiguration guiConfig = plugin.getGuiConfig().getConfig();
        String title = guiConfig.getString("classes.title", "<#00BFFF>Class Selection");
        Inventory gui = Bukkit.createInventory(null,
                guiConfig.getInt("classes.size", 27),
                mm.deserialize(title)
        );

        plugin.getClassManager().getClasses().forEach((id, clazz) -> {
            String path = "classes.items." + id + ".";
            ItemStack item = new ItemStack(Material.matchMaterial(
                    guiConfig.getString(path + "material", "BOOK")
            ));

            ItemMeta meta = item.getItemMeta();
            meta.displayName(mm.deserialize(
                    guiConfig.getString(path + "name", clazz.getDisplayName())
            ));

            List<Component> lore = guiConfig.getStringList(path + "lore").stream()
                    .map(line -> mm.deserialize(line))
                    .collect(Collectors.toList());
            meta.lore(lore);

            item.setItemMeta(meta);
            gui.addItem(item);
        });

        player.openInventory(gui);
    }