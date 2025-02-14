package com.sandcore.mmo.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Consolidated command handler for SandMMO.
 *
 * Usage:
 *   /sandmmo           - Opens the stats GUI.
 *   /sandmmo stats     - Opens the stats GUI.
 *   /sandmmo reload    - Reloads the GUI configuration (from gui.yml)
 *                        and refreshes the stats GUI for all players.
 */
public class StatsCommands implements CommandExecutor {

    private final JavaPlugin plugin;
    private final StatsManager statsManager;
    private final ClassesConfig classesConfig;
    private StatsGUIConfig guiConfig;

    public StatsCommands(JavaPlugin plugin, StatsManager statsManager, ClassesConfig classesConfig) {
        this.plugin = plugin;
        this.statsManager = statsManager;
        this.classesConfig = classesConfig;
        // Load GUI configuration from gui.yml
        this.guiConfig = new StatsGUIConfig(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || args[0].equalsIgnoreCase("stats")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
                return true;
            }
            Player player = (Player) sender;
            Bukkit.getScheduler().runTask(plugin, () -> {
                StatsGUI gui = new StatsGUI(guiConfig, statsManager, classesConfig);
                player.openInventory(gui.build(player));
            });
            sender.sendMessage(ChatColor.GREEN + "Opening your Stats GUI...");
        } else {
            sender.sendMessage(ChatColor.RED + "Usage: /" + label);
        }
        return true;
    }
} 