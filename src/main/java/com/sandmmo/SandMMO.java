package com.sandmmo;

import com.sandmmo.commands.ClassCommand;
import com.sandmmo.commands.ReloadCommand;
import com.sandmmo.commands.StatsCommand;
import com.sandmmo.config.ClassesConfig;
import com.sandmmo.config.MessagesConfig;
import com.sandmmo.gui.ClassGUI;
import com.sandmmo.managers.LevelManager;
import com.sandmmo.managers.MobManager;
import com.sandmmo.managers.PlayerDataManager;
import com.sandmmo.managers.StatsManager;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SandMMO extends JavaPlugin implements Listener {
    private static SandMMO instance;

    // Configurations
    private ClassesConfig classesConfig;
    private MessagesConfig messagesConfig;

    // Managers
    private PlayerDataManager playerDataManager;
    private LevelManager levelManager;
    private StatsManager statsManager;
    private MobManager mobManager;

    // GUIs
    private ClassGUI classGUI;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        reloadConfig();

        // Load configurations (Eco-based)
        this.classesConfig = new ClassesConfig(this);
        this.messagesConfig = new MessagesConfig(this);

        // Initialize managers
        this.playerDataManager = new PlayerDataManager(this);
        this.levelManager = new LevelManager(this);
        this.statsManager = new StatsManager(this);
        this.mobManager = new MobManager(this);

        // Initialize GUIs (using Eco's GUI system)
        this.classGUI = new ClassGUI(classesConfig);

        // Register commands
        registerCommand("class", new ClassCommand(this));
        registerCommand("stats", new StatsCommand(this));
        registerCommand("sandmmo", new ReloadCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(this, this);
        // (Also register any additional listeners, e.g. new ClassListener if needed)

        getLogger().info("§6§l[SandMMO] §aPlugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("§6§l[SandMMO] §cPlugin disabled!");
    }

    private void registerCommand(String name, Object executor) {
        PluginCommand command = getCommand(name);
        if (command != null) {
            command.setExecutor((CommandExecutor) executor);
        } else {
            getLogger().severe("Failed to register /" + name + " command!");
        }
    }

    public void reload() {
        reloadConfig();
        classesConfig.reload();
        messagesConfig.reload();
        // Reinitialize GUIs if needed
        this.classGUI = new ClassGUI(classesConfig);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        classGUI.handleClick(event);
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

    public ClassesConfig getClassesConfig() {
        return classesConfig;
    }

    public MessagesConfig getMessagesConfig() {
        return messagesConfig;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public StatsManager getStatsManager() {
        return statsManager;
    }

    public MobManager getMobManager() {
        return mobManager;
    }

    public ClassGUI getClassGUI() {
        return classGUI;
    }
}