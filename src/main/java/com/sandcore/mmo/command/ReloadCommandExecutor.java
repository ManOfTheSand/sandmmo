package com.sandcore.mmo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sandcore.mmo.gui.ReloadGUI;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import com.sandcore.mmo.util.ServiceRegistry;

public class ReloadCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;

    public ReloadCommandExecutor(JavaPlugin plugin) {
         this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (sender instanceof Player) {
             Player player = (Player) sender;
             // Reload the plugin configuration from disk.
             plugin.reloadConfig();
             FileConfiguration config = plugin.getConfig();
 
             // Reload additional configuration files.
             if (ServiceRegistry.getClassManager() != null) {
                 ServiceRegistry.getClassManager().loadClasses();
             }
             // Add similar calls for other managers if needed.
 
             // Open a custom reload GUI to indicate a successful reload.
             new ReloadGUI(config).open(player);
         } else {
             sender.sendMessage("This command is only usable by players.");
         }
         return true;
    }
} 