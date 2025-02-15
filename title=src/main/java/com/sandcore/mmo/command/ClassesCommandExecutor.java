package com.sandcore.mmo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sandcore.mmo.manager.ClassesManager;
import com.sandcore.mmo.manager.PlayerClassStorage;
import com.sandcore.mmo.gui.ClassSelectionGUI;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.ChatColor;

/**
 * Command executor for the /classes command.
 * Opens the class selection GUI for the player.
 */
public class ClassesCommandExecutor implements CommandExecutor {
    private JavaPlugin plugin;
    private ClassesManager classesManager;
    private PlayerClassStorage storage;

    public ClassesCommandExecutor(JavaPlugin plugin, ClassesManager classesManager, PlayerClassStorage storage) {
        this.plugin = plugin;
        this.classesManager = classesManager;
        this.storage = storage;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Ensure only players can execute this command.
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Only players can select a class.");
            return true;
        }
        Player player = (Player) sender;
        // Check permission.
        if (!player.hasPermission("sandmmo.classes.use")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }
        // Create and show the Class Selection GUI.
        ClassSelectionGUI gui = new ClassSelectionGUI(classesManager, storage);
        plugin.getServer().getPluginManager().registerEvents(gui, plugin);
        gui.open(player);
        return true;
    }
} 