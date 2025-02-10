package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import com.sandmmo.gui.MMOGUI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MMOGUICommand implements CommandExecutor {

    private final SandMMO plugin;

    public MMOGUICommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }
        Player player = (Player) sender;
        new MMOGUI(plugin).open(player);
        return true;
    }
}