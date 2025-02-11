package com.sandcore.mmo.command;

import com.sandcore.mmo.SandCoreMain;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

/**
 * MainCommandExecutor delegates subcommands for /sandmmo.
 * 
 * Supported subcommands:
 * - /sandmmo reload  : handled by ReloadCommandExecutor.
 * - /sandmmo admin stats <set|add> <player> <attribute> <value> :
 *       handled by AdminStatsCommandExecutor.
 */
public class MainCommandExecutor implements CommandExecutor {

    private final ReloadCommandExecutor reloadCommandExecutor;
    private final AdminStatsCommandExecutor adminStatsCommandExecutor;

    public MainCommandExecutor(SandCoreMain plugin) {
        // Initialize subcommand executors.
        this.reloadCommandExecutor = new ReloadCommandExecutor(plugin);
        this.adminStatsCommandExecutor = new AdminStatsCommandExecutor();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1) {
            sender.sendMessage(ChatColor.GREEN + "Usage: /sandmmo <reload|admin stats ...>");
            return true;
        }
        
        // Check for subcommand "reload"
        if (args[0].equalsIgnoreCase("reload")) {
            return reloadCommandExecutor.onCommand(sender, command, label, args);
        }
        
        // Check for "admin" subcommand with "stats"
        if (args[0].equalsIgnoreCase("admin")) {
            if (args.length >= 2 && args[1].equalsIgnoreCase("stats")) {
                // Remove the first two arguments ("admin", "stats") and delegate the rest.
                String[] newArgs = Arrays.copyOfRange(args, 2, args.length);
                return adminStatsCommandExecutor.onCommand(sender, command, label, newArgs);
            } else {
                sender.sendMessage(ChatColor.RED + "Invalid admin subcommand. Usage: /sandmmo admin stats <set|add> <player> <attribute> <value>");
                return true;
            }
        }
        
        sender.sendMessage(ChatColor.RED + "Unknown subcommand for /sandmmo.");
        return true;
    }
} 