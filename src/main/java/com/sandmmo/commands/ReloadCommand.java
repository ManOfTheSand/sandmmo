package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ReloadCommand implements CommandExecutor {
    private final SandMMO plugin;

    public ReloadCommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage(plugin.getMessagesManager().getMessage("reload-success"));
            return true;
        }
        return false;
    }
}