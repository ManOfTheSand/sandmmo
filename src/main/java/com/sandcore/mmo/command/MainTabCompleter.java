package com.sandcore.mmo.command;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MainTabCompleter provides tab completion for the /sandmmo command.
 * It suggests subcommands such as "reload" and "admin".
 * For admin commands, it delegates further completion to AdminStatsTabCompleter.
 */
public class MainTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        // Format for /sandmmo: 
        // /sandmmo <subcommand> ...
        if (args.length == 1) {
            List<String> subCommands = Arrays.asList("reload", "admin");
            return filterByStart(args[0], subCommands);
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("admin")) {
                // second arg for admin: suggest "stats"
                List<String> adminSub = Arrays.asList("stats");
                return filterByStart(args[1], adminSub);
            }
        } else if (args.length >= 3) {
            if (args[0].equalsIgnoreCase("admin") && args[1].equalsIgnoreCase("stats")) {
                // Delegate remaining tab completion to AdminStatsTabCompleter.
                // We shift the args: remove the first two elements.
                String[] adminArgs = Arrays.copyOfRange(args, 2, args.length);
                AdminStatsTabCompleter adminTab = new AdminStatsTabCompleter();
                return adminTab.onTabComplete(sender, command, alias, adminArgs);
            }
        }
        return Collections.emptyList();
    }

    private List<String> filterByStart(String input, List<String> options) {
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option.toLowerCase().startsWith(input.toLowerCase())) {
                result.add(option);
            }
        }
        return result;
    }
} 