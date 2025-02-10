package com.sandmmo;

import com.sandmmo.commands.ClassCommand;
import com.sandmmo.listeners.ClassListener;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerManager;
import com.sandmmo.managers.SkillManager;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class SandMMO extends JavaPlugin {
    private static SandMMO instance;
    private ClassManager classManager;
    private PlayerManager playerManager;
    private SkillManager skillManager;

    @Override
    public void onEnable() {
        instance = this;

        saveDefaultConfig();
        reloadConfig();

        this.classManager = new ClassManager(this);
        classManager.loadClasses();

        this.playerManager = new PlayerManager(this);
        this.skillManager = new SkillManager(this);

        PluginCommand classCommand = getCommand("class");
        if (classCommand != null) {
            classCommand.setExecutor(new ClassCommand(this));
        } else {
            getLogger().severe("Command 'class' not found in plugin.yml");
        }

        getServer().getPluginManager().registerEvents(new ClassListener(this), this);

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

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public void reloadClasses() {
        reloadConfig();
        classManager.loadClasses();
    }
}