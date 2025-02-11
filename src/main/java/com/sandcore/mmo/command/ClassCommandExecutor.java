package com.sandcore.mmo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import com.sandcore.mmo.gui.ClassSelectionGUI;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class ClassCommandExecutor implements CommandExecutor {
    private final JavaPlugin plugin;

    public ClassCommandExecutor(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
         if (sender instanceof Player) {
             Player player = (Player) sender;
             FileConfiguration config = plugin.getConfig();
             new ClassSelectionGUI(config).open(player);
         } else {
             sender.sendMessage("This command is only usable by players.");
         }
         return true;
    }
} 