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
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Open the GUI for the player.
            plugin.getClassGUI().open(player);
            return true;
        }
        return false;
    }
}