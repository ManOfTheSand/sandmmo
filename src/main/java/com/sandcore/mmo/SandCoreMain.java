package com.sandcore.mmo;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;
import com.sandcore.mmo.command.ClassCommandExecutor;
import com.sandcore.mmo.command.StatsCommandExecutor;
import com.sandcore.mmo.command.ReloadCommandExecutor;
import java.io.File;
import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.util.ServiceRegistry;
import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.gui.AsyncStatsGUIHandler;
import org.bukkit.entity.Player;
import com.sandcore.mmo.command.AdminStatsCommandExecutor;

public class SandCoreMain extends JavaPlugin {

    private static SandCoreMain instance;
    private AsyncStatsGUIHandler statsGUIHandler;

    public static SandCoreMain getInstance() {
         return instance;
    }
    
    @Override
    public void onEnable() {
         instance = this;
         ServiceRegistry.registerPlugin(this);
         
         // Ensure data folder exists.
         if (!getDataFolder().exists()) {
             getDataFolder().mkdirs();
         }
         
         // Save default configuration files if they don't exist.
         saveDefaultConfig();
         String[] configFiles = { "classes.yml", "stats.yml", "statsgui.yml", "gui.yml" };
         for (String fileName : configFiles) {
             File configFile = new File(getDataFolder(), fileName);
             if (!configFile.exists()) {
                 saveResource(fileName, false);
             }
         }
         
         // Register managers.
         ServiceRegistry.registerClassManager(new ClassManager());
         ServiceRegistry.registerStatsManager(new StatsManager());
         
         // Register command executors.
         if (getCommand("class") != null) {
             getCommand("class").setExecutor(new ClassCommandExecutor(this));
         }
         if (getCommand("sandmmo") != null) {
             getCommand("sandmmo").setExecutor(new ReloadCommandExecutor(this));
         }
         
         // Register the new admin stats command.
         if (getCommand("adminstats") != null) {
             getCommand("adminstats").setExecutor(new AdminStatsCommandExecutor());
         } else {
             getLogger().severe("Command /adminstats not defined in plugin.yml");
         }
         
         // Initialize the AsyncStatsGUIHandler and register the /stats command.
         statsGUIHandler = new AsyncStatsGUIHandler(this);
         if (getCommand("stats") != null) {
             getCommand("stats").setExecutor(new StatsCommandExecutor(this, statsGUIHandler));
         } else {
             getLogger().severe("Command /stats not defined in plugin.yml");
         }
         
         // Example event: welcome message on join.
         getServer().getPluginManager().registerEvents(new Listener() {
             @EventHandler
             public void onPlayerJoin(PlayerJoinEvent event) {
                 Player player = event.getPlayer();
                 player.sendMessage(Component.text("Welcome to MMO!"));
             }
         }, this);
         
         getLogger().info("SandCoreMain plugin enabled!");
    }

    @Override
    public void onDisable() {
         getLogger().info("SandCoreMain plugin disabled!");
    }

    public AsyncStatsGUIHandler getStatsGUIHandler() {
         return statsGUIHandler;
    }
} 