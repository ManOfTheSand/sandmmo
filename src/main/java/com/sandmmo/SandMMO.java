package com.sandmmo;

import com.sandmmo.commands.ClassCommand;
import com.sandmmo.gui.ClassGUI;
import com.sandmmo.gui.SkillsGUI;
import com.sandmmo.listeners.ClassListener;
import com.sandmmo.managers.*;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SandMMO extends JavaPlugin implements Listener {
    private static SandMMO instance;
    private ClassManager classManager;
    private PlayerDataManager playerDataManager;
    private SkillManager skillManager;
    private MessagesManager messagesManager;
    private ClassGUI classGUI;
    private SkillsGUI skillsGUI;
    private LevelManager levelManager;
    private StatsManager statsManager;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        // Initialize managers
        this.classManager = new ClassManager(this);
        this.playerDataManager = new PlayerDataManager(this);
        this.skillManager = new SkillManager(this);
        this.messagesManager = new MessagesManager(this);
        this.levelManager = new LevelManager(this);
        this.statsManager = new StatsManager(this);
        this.classGUI = new ClassGUI(this);
        this.skillsGUI = new SkillsGUI(this);

        // Register command
        PluginCommand classCommand = getCommand("class");
        if (classCommand != null) {
            classCommand.setExecutor(new ClassCommand(this));
        } else {
            getLogger().severe("Failed to register /class command!");
        }

        // Register reload command
        PluginCommand reloadCommand = getCommand("sandmmo");
        if (reloadCommand != null) {
            reloadCommand.setExecutor((sender, command, label, args) -> {
                if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
                    reload();
                    sender.sendMessage(messagesManager.getMessage("reload-success"));
                    return true;
                }
                return false;
            });
        } else {
            getLogger().severe("Failed to register /sandmmo command!");
        }

        // Register listeners
        getServer().getPluginManager().registerEvents(new ClassListener(this), this);
        getServer().getPluginManager().registerEvents(this, this);

        getLogger().info("§6§l[SandMMO] §aPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("§6§l[SandMMO] §cPlugin disabled!");
    }

    public void reload() {
        reloadConfig();
        classManager.loadClasses();
        messagesManager.reload();
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        classGUI.handleClick(event);
        skillsGUI.handleClick(event);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        playerDataManager.loadPlayerData(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerDataManager.savePlayerData(event.getPlayer());
    }

    public static SandMMO getInstance() {
        return instance;
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }

    public MessagesManager getMessagesManager() {
        return messagesManager;
    }

    public ClassGUI getClassGUI() {
        return classGUI;
    }

    public SkillsGUI getSkillsGUI() {
        return skillsGUI;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }
}