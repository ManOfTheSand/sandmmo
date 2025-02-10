package com.sandmmo.commands;

import com.sandmmo.SandMMO;
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
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getMessagesConfig().get().getString("errors.player-only"));
            return true;
        }
        plugin.getClassSelectionGUI().open(player);
        return true;
    }
}