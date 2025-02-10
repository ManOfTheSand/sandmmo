package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import com.sandmmo.data.ClassData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassManager {
    private final SandMMO plugin;
    private final Map<String, ClassData> classes;
    private final PlayerDataManager playerDataManager;
    private final MessagesManager messagesManager;

    public ClassManager(SandMMO plugin) {
        this.plugin = plugin;
        this.classes = new HashMap<>();
        this.playerDataManager = plugin.getPlayerDataManager();
        this.messagesManager = plugin.getMessagesManager();
        loadClasses();
    }

    public void loadClasses() {
        classes.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("classes");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                String displayName = section.getString(key + ".display-name");
                List<String> description = section.getStringList(key + ".description");
                String icon = section.getString(key + ".icon");
                classes.put(key, new ClassData(displayName, description, icon));
            }
        }
    }

    public void selectClass(Player player, String className) {
        if (classes.containsKey(className)) {
            playerDataManager.setPlayerClass(player, className);
            messagesManager.sendMessage(player, "class-selected", className);
            player.closeInventory();
        }
    }

    public Map<String, ClassData> getAllClasses() {
        return classes;
    }
}