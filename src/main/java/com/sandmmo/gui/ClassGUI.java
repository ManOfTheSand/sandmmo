package com.sandmmo.gui;

import com.sandmmo.config.ClassesConfig;
import com.sandmmo.managers.ClassManager;
import org.bukkit.entity.Player;

public class ClassGUI {
    private final ClassesConfig classesConfig;
    private final ClassManager classManager;

    public ClassGUI(ClassesConfig classesConfig, ClassManager classManager) {
        this.classesConfig = classesConfig;
        this.classManager = classManager;
    }

    // Added stub method to open the GUI for a player.
    public void open(Player player) {
        // TODO: Implement your GUI logic using the Adventure API and MiniMessage.
        // For now, simply send the player a message.
        player.sendMessage("Opening Class GUI…");
    }
}