package com.sandcore.mmo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.Component;

public class SandCoreCommandExecutor implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Send help message for the MMOCore plugin.
            player.sendMessage(Component.text("MMOCore Plugin Help:\n" +
                    "/class - Open class selection\n" +
                    "/stats - View your stats\n" +
                    "/sandmmo - Reload configurations"));
        } else {
            sender.sendMessage("This command is only usable by players.");
        }
        return true;
    }
} 