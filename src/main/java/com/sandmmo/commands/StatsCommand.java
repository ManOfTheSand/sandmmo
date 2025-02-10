package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import com.sandmmo.gui.StatsGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    private final SandMMO plugin;

    public StatsCommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            new StatsGUI(plugin).open(player);
            return true;
        }
        return false;
    }
}