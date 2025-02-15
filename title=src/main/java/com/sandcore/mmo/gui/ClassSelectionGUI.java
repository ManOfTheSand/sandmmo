package com.sandcore.mmo.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import com.sandcore.mmo.manager.ClassesManager;
import com.sandcore.mmo.manager.PlayerClass;
import com.sandcore.mmo.manager.PlayerClassStorage;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * GUI for class selection. Reads configuration from ClassesManager and displays items accordingly.
 */
public class ClassSelectionGUI implements InventoryHolder, Listener {
    private Inventory inventory;
    private ClassesManager classesManager;
    private PlayerClassStorage storage;

    /**
     * Constructor for ClassSelectionGUI.
     * @param classesManager Reference to the ClassesManager.
     * @param storage Reference to the PlayerClassStorage.
     */
    public ClassSelectionGUI(ClassesManager classesManager, PlayerClassStorage storage) {
        this.classesManager = classesManager;
        this.storage = storage;
        // Create an inventory (size can be adjusted; here we use 9 slots for simplicity).
        this.inventory = Bukkit.createInventory(this, 9, ChatColor.translateAlternateColorCodes('&', "&6Select Your Class"));
        populateInventory();
    }

    /**
     * Populates the inventory with items for each class.
     */
    private void populateInventory() {
        Map<String, PlayerClass> classes = classesManager.getAllClasses();
        int slot = 0;
        for (PlayerClass pc : classes.values()) {
            ItemStack item = new ItemStack(pc.getIcon());
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                // Translate hex color codes in the display name.
                meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', pc.getDisplayName()));
                List<String> lore = new ArrayList<>();
                lore.add(ChatColor.translateAlternateColorCodes('&', pc.getDescription()));
                // Optionally append a summary of casting abilities.
                if (pc.getCastingAbilities() != null && !pc.getCastingAbilities().isEmpty()) {
                    lore.add(ChatColor.GRAY + "Casting Abilities:");
                    for (Map.Entry<String, ?> entry : pc.getCastingAbilities().entrySet()) {
                        lore.add(ChatColor.GRAY + entry.getKey() + ": " + entry.getValue().toString());
                    }
                }
                meta.setLore(lore);
                item.setItemMeta(meta);
            }
            inventory.setItem(slot, item);
            slot++;
        }
    }

    /**
     * Opens the class selection GUI for the specified player.
     * @param player The player.
     */
    public void open(Player player) {
        player.openInventory(inventory);
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Listener for inventory click events to handle class selection.
     * Prevents item movement and processes the player's selection.
     */
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().equals(this.inventory)) {
            event.setCancelled(true); // Lock the inventory.
            if (event.getCurrentItem() == null) return;
            Player player = (Player) event.getWhoClicked();
            String clickedName = "";
            if (event.getCurrentItem().hasItemMeta() && event.getCurrentItem().getItemMeta().hasDisplayName()) {
                clickedName = ChatColor.stripColor(event.getCurrentItem().getItemMeta().getDisplayName());
            }
            // Find a matching class by comparing display names.
            PlayerClass selectedClass = null;
            for (PlayerClass pc : classesManager.getAllClasses().values()) {
                String stripped = ChatColor.stripColor(pc.getDisplayName());
                if (stripped.equalsIgnoreCase(clickedName)) {
                    selectedClass = pc;
                    break;
                }
            }
            if (selectedClass != null) {
                // Save the player's chosen class.
                storage.savePlayerClass(player, selectedClass.getId());
                player.sendMessage(ChatColor.GREEN + "You have selected the " +
                        ChatColor.translateAlternateColorCodes('&', selectedClass.getDisplayName()) + " class!");
                // Integration point: here call your PlayerStatsApplier to apply bonus attributes.
                player.closeInventory();
            } else {
                player.sendMessage(ChatColor.RED + "Class selection failed. Please try again.");
            }
        }
    }
} 