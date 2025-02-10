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
    private final MiniMessage mm = MiniMessage.miniMessage();

    public ClassCommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize("<red>This command is for players only!"));
            return true;
        }

        if (args.length == 0) {
            sendHelpMessage(player);
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "list":
                listClasses(player);
                break;
            case "select":
                handleClassSelection(player, args);
                break;
            default:
                player.sendMessage(mm.deserialize("<red>Invalid subcommand!"));
        }
        return true;
    }

    private void sendHelpMessage(Player player) {
        Component help = mm.deserialize(
                "<aqua>Class Command Help:\n" +
                        "<gray>/class list <gray>- Show available classes\n" +
                        "<gray>/class select <class> <gray>- Choose a class"
        );
        player.sendMessage(help);
    }

    private void listClasses(Player player) {
        ClassManager classManager = plugin.getClassManager();
        Component message = mm.deserialize("<yellow>Available classes: " +
                String.join(", ", classManager.getAvailableClasses()));
        player.sendMessage(message);
    }

    private void handleClassSelection(Player player, String[] args) {
        if (args.length < 2) {
            player.sendMessage(mm.deserialize("<red>Please specify a class!"));
            return;
        }

        ClassManager classManager = plugin.getClassManager();
        String className = args[1];
        plugin.getClassManager().getClass(className);

        player.sendMessage(mm.deserialize("<green>Selected class: " + className));
    }
}