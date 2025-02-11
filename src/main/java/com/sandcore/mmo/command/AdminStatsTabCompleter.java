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
 * Provides tab completion for the "admin stats" command arguments.
 * Expected arguments:
 *   args[0]: "set" or "add"
 *   args[1]: target player name
 *   args[2]: attribute (suggest "maxHealth", "maxMana", "healthRegen", "manaRegen", "strength", "dexterity", "intellect")
 *   args[3]: (value) - no suggestions, so returns an empty list.
 */
public class AdminStatsTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            // Suggest "set" and "add"
            return filterByStart(args[0], Arrays.asList("set", "add"));
        } else if (args.length == 2) {
            // Suggest online player names
            List<String> playerNames = new ArrayList<>();
            for (Player p : Bukkit.getOnlinePlayers()) {
                playerNames.add(p.getName());
            }
            return filterByStart(args[1], playerNames);
        } else if (args.length == 3) {
            // Suggest attribute names
            List<String> attributes = Arrays.asList("maxHealth", "maxMana", "healthRegen", "manaRegen", "strength", "dexterity", "intellect", "criticalChance", "criticalDamage");
            return filterByStart(args[2], attributes);
        } else {
            // For the numerical value argument, no suggestions.
            return Collections.emptyList();
        }
    }

    private List<String> filterByStart(String input, List<String> options) {
        List<String> result = new ArrayList<>();
        for(String option : options) {
            if(option.toLowerCase().startsWith(input.toLowerCase())){
                result.add(option);
            }
        }
        return result;
    }
} 