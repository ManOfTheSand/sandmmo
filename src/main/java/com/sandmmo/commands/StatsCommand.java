package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class StatsCommand implements CommandExecutor {
    private final SandMMO plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public StatsCommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize(
                    plugin.getMessagesConfig().get().getString("errors.player-only")
            ));
            return true;
        }
        // Add stats display logic here
        player.sendMessage(mm.deserialize("<green>Stats command works!"));
        return true;
    }
}