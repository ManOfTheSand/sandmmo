package com.sandcore.mmo.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Command executor for /reload.
 * Reloads config.yml asynchronously and refreshes the stats GUI for any players who have it open.
 */
public class ReloadStatsCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;
    private final StatsManager statsManager;
    private final ClassesConfig classesConfig;

    public ReloadStatsCommandExecutor(JavaPlugin plugin, StatsManager statsManager, ClassesConfig classesConfig) {
        this.plugin = plugin;
        this.statsManager = statsManager;
        this.classesConfig = classesConfig;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("sandmmo.reload")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to reload configuration.");
            return true;
        }
        sender.sendMessage(ChatColor.YELLOW + "Reloading configuration...");

        // Reload config.yml asynchronously.
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                plugin.reloadConfig();
            } catch (Exception e) {
                e.printStackTrace();
            }
            // Refresh GUI on the main thread.
            Bukkit.getScheduler().runTask(plugin, () -> {
                StatsGUIConfig newConfig = new StatsGUIConfig(plugin);
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (p.getOpenInventory().getTopInventory().getHolder() instanceof StatsGUIHolder) {
                        StatsGUI newGui = new StatsGUI(newConfig, statsManager, classesConfig);
                        p.openInventory(newGui.build(p));
                        p.sendMessage(ChatColor.GREEN + "Your Stats GUI has been refreshed.");
                    }
                }
                sender.sendMessage(ChatColor.GREEN + "Configuration reloaded successfully!");
            });
        });
        return true;
    }
} 