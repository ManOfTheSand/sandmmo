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

        plugin.getClassSelectionGUI().open(player);
        return true;
    }
}