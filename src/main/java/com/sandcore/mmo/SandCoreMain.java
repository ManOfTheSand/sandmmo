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
import com.sandcore.mmo.gui.AsyncGUIHandler;

public class SandCoreMain extends JavaPlugin {

    private static SandCoreMain instance;
    private AsyncGUIHandler guiHandler;

    public static SandCoreMain getInstance() {
         return instance;
    }
    
    @Override
    public void onEnable() {
         instance = this;
         ServiceRegistry.registerPlugin(this);
         
         // Ensure the plugin data folder exists.
         if (!getDataFolder().exists()) {
             getDataFolder().mkdirs();
         }
         
         // Save the default config.yml if it doesn't exist.
         saveDefaultConfig();
         
         // List of YAML files to generate
         String[] configFiles = {
             "classes.yml",
             "stats.yml",
             "gui.yml"
         };

         for (String fileName : configFiles) {
             File configFile = new File(getDataFolder(), fileName);
             if (!configFile.exists()) {
                 saveResource(fileName, false);
             }
         }
         
         // Register ClassManager
         ServiceRegistry.registerClassManager(new ClassManager());
         
         // Register StatsManager
         ServiceRegistry.registerStatsManager(new StatsManager());
         
         // Register the /class command executor.
         if (getCommand("class") != null) {
             getCommand("class").setExecutor(new ClassCommandExecutor(this));
         } else {
             getLogger().severe("Command /class not defined in plugin.yml");
         }
         // Register the /sandmmo command executor (reload config GUI).
         if (getCommand("sandmmo") != null) {
             getCommand("sandmmo").setExecutor(new ReloadCommandExecutor(this));
         } else {
             getLogger().severe("Command /sandmmo not defined in plugin.yml");
         }
         
         // Initialize the AsyncGUIHandler
         guiHandler = new AsyncGUIHandler(this);
         // Register the stats command executor
         getCommand("stats").setExecutor(new StatsCommandExecutor(this, guiHandler));
         
         // Example event: welcome message on join.
         getServer().getPluginManager().registerEvents(new Listener() {
             @EventHandler
             public void onPlayerJoin(PlayerJoinEvent event) {
                 event.getPlayer().sendMessage(Component.text("Welcome to MMO!"));
             }
         }, this);
         
         getLogger().info("SandCoreMain plugin enabled!");
    }

    @Override
    public void onDisable() {
         getLogger().info("SandCoreMain plugin disabled!");
    }
} 