package com.sandmmo.commands;

import com.sandmmo.SandMMO;
import com.sandmmo.gui.ClassGUI;
import com.willfp.eco.core.command.CommandBase;
import org.bukkit.entity.Player;

public class ClassCommand extends CommandBase {
    public ClassCommand(SandMMO plugin) {
        super(plugin, "class");
        this.setExecutor((sender, args) -> {
            if (sender instanceof Player player) {
                ClassGUI classGUI = plugin.getClassGUI();
                classGUI.open(player);
                return true;
            }
            return false;
        });
    }
}