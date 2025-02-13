package com.sandcore.mmo.command;

import com.sandcore.mmo.SandCoreMain;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import com.sandcore.mmo.util.ServiceRegistry;
import java.util.function.Consumer;
import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.manager.CurrencyManager;
import com.sandcore.mmo.manager.XPManager;
import com.sandcore.mmo.manager.CastingManager;

public class ReloadCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;

    public ReloadCommandExecutor(JavaPlugin plugin) {
         this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (sender instanceof Player) {
             Player player = (Player) sender;
             try {
                 // Reload all configurations
                 plugin.reloadConfig();
                 plugin.saveConfig();
                 
                 // Reload all managers
                 reloadManager(ServiceRegistry.getClassManager(), ClassManager::loadClasses);
                 reloadManager(ServiceRegistry.getStatsManager(), StatsManager::reloadStats);
                 reloadManager(ServiceRegistry.getCurrencyManager(), CurrencyManager::reload);
                 reloadManager(ServiceRegistry.getXPManager(), XPManager::reload);
                 
                 // Reload the statsgui.yml used by your AsyncStatsGUIHandler.
                 SandCoreMain main = SandCoreMain.getInstance();
                 if (main.getStatsGUIHandler() != null) {
                     main.getStatsGUIHandler().reloadConfiguration();
                 }
                 
                 // Reload the casting configuration so that updated skills are registered
                 reloadManager(ServiceRegistry.getCastingManager(), CastingManager::loadConfiguration);
                 
                 // Reload the classes configuration
                 if (ServiceRegistry.getClassManager() != null) {
                     ServiceRegistry.getClassManager().reloadClasses();
                 }
                 
                 player.sendMessage("§aConfigurations reloaded successfully!");
             } catch (Exception e) {
                 player.sendMessage("§cReload failed: " + e.getMessage());
             }
         } else {
             sender.sendMessage("§aConfigurations reloaded successfully!");
         }
         return true;
    }

    private <T> void reloadManager(T manager, Consumer<T> reloadMethod) {
        if (manager != null) {
            reloadMethod.accept(manager);
        }
    }
} 