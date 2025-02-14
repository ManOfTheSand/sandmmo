package com.sandcore.mmo;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import com.sandcore.mmo.stats.StatsGUI;
import com.sandcore.mmo.stats.StatsGUIConfig;
import com.sandcore.mmo.stats.StatsGUIHolder;
import com.sandcore.mmo.stats.StatsManager;
import com.sandcore.mmo.stats.ClassesConfig;

/**
 * Universal reload command for the plugin.
 * 
 * When /reload is executed, this class reloads all YAML configuration files
 * (e.g., config.yml, gui.yml, classes.yml) and refreshes any open GUIs.
 */
public class ReloadCommand implements CommandExecutor {

    private final JavaPlugin plugin;
    private final StatsManager statsManager;
    private final ClassesConfig classesConfig;

    public ReloadCommand(JavaPlugin plugin, StatsManager statsManager, ClassesConfig classesConfig) {
        this.plugin = plugin;
        this.statsManager = statsManager;
        this.classesConfig = classesConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sandmmo.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to reload configurations.");
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + "Reloading all plugin configurations...");
        
        // Reload the main config.yml.
        plugin.reloadConfig();
        // Reload other configuration files as needed.
        // For example, StatsGUIConfig reloads its own file (gui.yml) on creation.
        StatsGUIConfig newGUIConfig = new StatsGUIConfig(plugin);
        ClassesConfig newClassesConfig = new ClassesConfig(plugin.getDataFolder());
        
        // Refresh any open GUIs dependent on these configurations.
        Bukkit.getScheduler().runTask(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (p.getOpenInventory().getTopInventory().getHolder() instanceof StatsGUIHolder) {
                    StatsGUI newGUI = new StatsGUI(newGUIConfig, statsManager, newClassesConfig);
                    p.openInventory(newGUI.build(p));
                    p.sendMessage(ChatColor.GREEN + "Your GUI has been refreshed.");
                }
            }
            sender.sendMessage(ChatColor.GREEN + "All configurations reloaded successfully!");
        });
        return true;
    }
} 