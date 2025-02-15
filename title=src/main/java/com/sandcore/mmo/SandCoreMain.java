package com.sandcore.mmo;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;
import java.io.File;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.entity.Player;
import com.sandcore.mmo.casting.CastingManager;
import com.sandcore.mmo.casting.CastingListener;
import com.sandcore.mmo.manager.ClassesConfigLoader;
import com.sandcore.mmo.manager.ClassesManager;
import com.sandcore.mmo.command.ClassCommandExecutor;
import com.sandcore.mmo.command.MainCommandExecutor;
import com.sandcore.mmo.command.MainTabCompleter;
import com.sandcore.mmo.command.ReloadCommand;

/**
 * Main plugin class for SandCore.
 * 
 * Responsibilities:
 * - Ensure the data folder exists and default configuration files (classes.yml and gui.yml) are generated.
 * - Load the classes configuration using ClassesConfigLoader and ClassesManager.
 * - Register the /class and /sandmmo commands.
 * - Initialize the universal /reload command.
 * - Initialize the casting system and its event listener.
 * - Register a simple welcome listener.
 */
public class SandCoreMain extends JavaPlugin {

    private static SandCoreMain instance;

    public static SandCoreMain getInstance() {
         return instance;
    }
    
    @Override
    public void onEnable() {
         instance = this;
         ServiceRegistry.registerPlugin(this);
         
         // Ensure data folder exists and save default configuration files.
         if (!getDataFolder().exists()) {
             getDataFolder().mkdirs();
         }
         saveDefaultConfig();
         String[] configFiles = { "classes.yml", "gui.yml" };
         for (String fileName : configFiles) {
             File configFile = new File(getDataFolder(), fileName);
             if (!configFile.exists()) {
                 saveResource(fileName, false);
             }
         }
         
         // Generate or load classes.yml using our config loader.
         new ClassesConfigLoader(this);
         
         // Initialize and load the classes configuration.
         ServiceRegistry.registerClassManager(new com.sandcore.mmo.manager.ClassManager(this));
         // Load classes configuration (used by reload command).
         com.sandcore.mmo.manager.ClassesConfig classesConfig = new com.sandcore.mmo.manager.ClassesConfig(getDataFolder());
         
         // Register command executors.
         if (getCommand("class") != null) {
             getCommand("class").setExecutor(new ClassCommandExecutor());
         }
         if (getCommand("sandmmo") != null) {
             getCommand("sandmmo").setExecutor(new MainCommandExecutor(this));
             getCommand("sandmmo").setTabCompleter(new MainTabCompleter());
         } else {
             getLogger().severe("Command /sandmmo not defined in plugin.yml");
         }
         
         // Register the universal /reload command.
         if (getCommand("reload") != null) {
             getCommand("reload").setExecutor(new ReloadCommand(this, classesConfig));
         } else {
             getLogger().severe("Command /reload not defined in plugin.yml");
         }
         
         // Initialize the casting system.
         CastingManager castingManager = new CastingManager(this);
         getServer().getPluginManager().registerEvents(new CastingListener(castingManager), this);
         ServiceRegistry.registerCastingManager(castingManager);
         
         // Register a simple welcome event listener.
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
} 