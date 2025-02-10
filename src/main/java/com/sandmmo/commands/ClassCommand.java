package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import com.sandmmo.managers.ClassManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClassCommand implements CommandExecutor {
    private final SandMMO plugin;
    private final MiniMessage miniMessage = MiniMessage.miniMessage();

    public ClassCommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if (args.length == 0) {
            Component help = miniMessage.deserialize(
                    "<aqua>Class Command Help:\n" +
                            "<gray>/class list <gray>- Show available classes\n" +
                            "<gray>/class select <class> <gray>- Choose a class"
            );
            player.sendMessage(help);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                listClasses(player);
                break;
            case "select":
                if (args.length < 2) {
                    player.sendMessage(miniMessage.deserialize("<red>Please specify a class!"));
                    return true;
                }
                selectClass(player, args[1]);
                break;
            default:
                player.sendMessage(miniMessage.deserialize("<red>Invalid subcommand!"));
        }

        return true;
    }

    private void listClasses(Player player) {
        // Implement class listing logic
        player.sendMessage(miniMessage.deserialize("<yellow>Available classes: Warrior, Mage"));
    }

    private void selectClass(Player player, String className) {
        // Implement class selection logic
        player.sendMessage(miniMessage.deserialize("<green>Selected class: " + className));
    }
}