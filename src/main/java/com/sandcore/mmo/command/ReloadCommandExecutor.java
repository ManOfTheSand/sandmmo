package com.sandcore.mmo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sandcore.mmo.gui.ReloadGUI;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ReloadCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;

    public ReloadCommandExecutor(JavaPlugin plugin) {
         this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (sender instanceof Player) {
             Player player = (Player) sender;
             // Reload the plugin configuration
             plugin.reloadConfig();
             FileConfiguration config = plugin.getConfig();
             new ReloadGUI(config).open(player);
         } else {
             sender.sendMessage("This command is only usable by players.");
         }
         return true;
    }
} 