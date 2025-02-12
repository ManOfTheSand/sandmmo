package com.sandcore.mmo.command;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.manager.ClassManager.PlayerClass;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClassTabCompleter implements TabCompleter {

    /**
     * Provides tab completion for the class command by suggesting available class IDs.
     */
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ClassManager classManager = ServiceRegistry.getClassManager();
        if (classManager == null) {
            return new ArrayList<>();
        }
        Map<String, PlayerClass> availableClasses = classManager.getAvailableClasses();
        List<String> suggestions = new ArrayList<>();
        if (args.length == 1) {
            String input = args[0].toLowerCase();
            for (String classId : availableClasses.keySet()) {
                if (classId.startsWith(input)) {
                    suggestions.add(classId);
                }
            }
        }
        return suggestions;
    }
} 