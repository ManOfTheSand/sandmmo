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

/**
 * Main plugin class for SandCore.
 * 
 * Responsibilities:
 * - Ensure the data folder exists and default config files (classes.yml and gui.yml) are generated.
 * - Load the classes configuration using ClassesConfigLoader and ClassesManager.
 * - Register the command for class selection.
 * - Initialize the casting system and register related event listeners.
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
         ClassesManager classesManager = new ClassesManager();
         classesManager.loadClasses(this);
         
         // Initialize the casting system.
         CastingManager castingManager = new CastingManager(this);
         ServiceRegistry.registerCastingManager(castingManager);
         getServer().getPluginManager().registerEvents(new CastingListener(castingManager), this);
         
         // Register the /class command for class selection.
         if (getCommand("class") != null) {
             getCommand("class").setExecutor(new ClassCommandExecutor(classesManager));
         } else {
             getLogger().severe("Command /class not defined in plugin.yml");
         }
         
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