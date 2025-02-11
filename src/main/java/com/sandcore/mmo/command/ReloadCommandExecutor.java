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
             
             try {
                 if (ServiceRegistry.getClassManager() != null) {
                     ServiceRegistry.getClassManager().loadClasses();
                 }
                 if (ServiceRegistry.getStatsManager() != null) {
                     ServiceRegistry.getStatsManager().reloadStats();
                 }
                 player.sendMessage("§aConfigurations reloaded successfully!");
             } catch (Exception e) {
                 player.sendMessage("§cReload failed: " + e.getMessage());
             }
             
             new ReloadGUI(plugin.getConfig()).open(player);
         } else {
             sender.sendMessage("§aConfigurations reloaded successfully!");
         }
         return true;
    }
} 