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
             plugin.reloadConfig();
             plugin.saveConfig();
             
             // Reload all YAML files
             ServiceRegistry.getClassManager().loadClasses();
             if (ServiceRegistry.getStatsManager() != null) {
                 ServiceRegistry.getStatsManager().reloadStats();
             }
             
             new ReloadGUI(plugin.getConfig()).open(player);
         } else {
             sender.sendMessage("This command is only usable by players.");
         }
         return true;
    }
} 