package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import com.sandmmo.classes.PlayerClass;
import com.sandmmo.gui.ClassSelectionGUI;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.player.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static com.sandmmo.gui.ClassSelectionGUI.mm;

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
        PlayerClass pc = plugin.getClassManager().getClass(className);
        MiniMessage mm;
        if (pc == null) {
            player.sendMessage(mm.deserialize("<red>Invalid class!"));
            return;
        }

        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        data.setPlayerClass(pc);
        player.sendMessage(mm.deserialize("<green>Class set to: " + pc.getDisplayName()));

        // Open GUI if needed
        new ClassSelectionGUI(plugin).open(player);
    }
}