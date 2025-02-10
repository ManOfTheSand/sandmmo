package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import com.sandmmo.gui.ClassGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {
    private final SandMMO plugin;

    public ClassCommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is for players only!");
            return true;
        }

        Player player = (Player) sender;
        if (!player.hasPermission("sandmmo.class")) {
            player.sendMessage("You don't have permission to use this command!");
            return true;
        }

        if (args.length == 0) {
            new ClassGUI(plugin).open(player);
            return true;
        }

        if (args[0].equalsIgnoreCase("select")) {
            if (args.length < 2) {
                player.sendMessage("Usage: /class select <className>");
                player.sendMessage("Available classes: " + String.join(", ", plugin.getClassManager().getAllClasses().keySet()));
                return true;
            }
            plugin.getClassManager().selectClass(player, args[1]);
        } else {
            player.sendMessage("Unknown command. Usage:");
            player.sendMessage("/class - Open the class selection GUI");
            player.sendMessage("/class select <className> - Select a class");
        }
        return true;
    }
}