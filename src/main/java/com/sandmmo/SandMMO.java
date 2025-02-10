package com.sandmmo;

import com.sandmmo.commands.ClassCommand;
import com.sandmmo.managers.ClassManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SandMMO extends JavaPlugin {
    private static SandMMO instance;
    private ClassManager classManager;

    @Override
    public void onEnable() {
        instance = this;

        // Save the default configuration file (config.yml in src/main/resources)
        saveDefaultConfig();

        // Initialize your class manager (ensure this class exists in your project)
        this.classManager = new ClassManager(this);

        // Register the command 'class'
        if (getCommand("class") != null) {
            getCommand("class").setExecutor(new ClassCommand(this));
        } else {
            getLogger().severe("Command 'class' not found in plugin.yml");
        }

        // Now that loadClasses is implemented in ClassManager, this call will work:
        classManager.loadClasses();

        getLogger().info("§6§l[SandMMO] §r§7Plugin enabled!");
        getLogger().info("§eVersion: §b" + getDescription().getVersion());
    }

    @Override
    public void onDisable() {
        getLogger().info("§6§l[SandMMO] §r§7Plugin disabled!");
    }

    public static SandMMO getInstance() {
        return instance;
    }

    public ClassManager getClassManager() {
        return classManager;
    }
}