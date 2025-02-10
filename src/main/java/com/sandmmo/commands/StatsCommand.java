package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import com.sandmmo.player.PlayerData;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class StatsCommand implements CommandExecutor {
    private final SandMMO plugin;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public StatsCommand(SandMMO plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(mm.deserialize("<red>This command is for players only!"));
            return true;
        }

        PlayerData data = plugin.getPlayerDataManager().getPlayerData(player);
        Component stats = mm.deserialize(
                "<aqua>Your Stats:\n" +
                        "<gray>Level: <white>" + data.getLevel() + "\n" +
                        "<gray>XP: <white>" + String.format("%.1f", data.getExperience()) + "/" +
                        String.format("%.1f", data.getRequiredExperience()) + "\n" +
                        "<gray>Class: " + (data.getPlayerClass() != null ?
                        data.getPlayerClass().getDisplayName() : "<gray>None")
        );

        player.sendMessage(stats);
        return true;
    }
}