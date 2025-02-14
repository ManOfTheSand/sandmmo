package com.sandcore.mmo.stats;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Command executor for /stats.
 * When executed, it builds and opens the stats GUI for the player.
 */
public class StatsCommandExecutor implements CommandExecutor {

    private final JavaPlugin plugin;
    private final StatsManager statsManager;
    private final ClassesConfig classesConfig;
    private final StatsGUIConfig guiConfig;

    public StatsCommandExecutor(JavaPlugin plugin, StatsManager statsManager, ClassesConfig classesConfig) {
        this.plugin = plugin;
        this.statsManager = statsManager;
        this.classesConfig = classesConfig;
        this.guiConfig = new StatsGUIConfig(plugin);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }
        Player player = (Player) sender;
        // Schedule GUI creation on the main thread.
        Bukkit.getScheduler().runTask(plugin, () -> {
            StatsGUI gui = new StatsGUI(guiConfig, statsManager, classesConfig);
            player.openInventory(gui.build(player));
        });
        sender.sendMessage(ChatColor.GREEN + "Opening your Stats GUI...");
        return true;
    }
} 