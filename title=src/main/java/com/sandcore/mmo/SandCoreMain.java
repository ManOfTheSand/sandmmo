package com.sandcore.mmo;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;
import com.sandcore.mmo.command.ClassCommandExecutor;
import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.stats.StatsCommands;
import com.sandcore.mmo.ReloadCommand;
import com.sandcore.mmo.stats.StatsManager;
import com.sandcore.mmo.stats.ClassesConfig;
import com.sandcore.mmo.util.ServiceRegistry;
import com.sandcore.mmo.casting.CastingManager;
import com.sandcore.mmo.casting.CastingListener;
import com.sandcore.mmo.stats.StatsGUIListener;
import org.bukkit.entity.Player;
import java.io.File;
import com.sandcore.mmo.manager.ClassesConfigLoader;
import com.sandcore.mmo.manager.ClassesManager;

public class SandCoreMain extends JavaPlugin {

    private static SandCoreMain instance;

    public static SandCoreMain getInstance() {
         return instance;
    }
    
    @Override
    public void onEnable() {
         instance = this;
         ServiceRegistry.registerPlugin(this);
         
         // Ensure the data folder exists.
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
         
         // Initialize and register managers.
         ClassManager classManager = new ClassManager(getDataFolder());
         ServiceRegistry.registerClassManager(classManager);
         StatsManager statsManager = new StatsManager();
         ClassesConfig classesConfig = new ClassesConfig(getDataFolder());
         
         // Register the /class command for choosing classes.
         if (getCommand("class") != null) {
             getCommand("class").setExecutor(new ClassCommandExecutor(classManager));
         }
         
         // Register the /stats command to open the Stats GUI.
         if (getCommand("stats") != null) {
             getCommand("stats").setExecutor(new StatsCommands(this, statsManager, classesConfig));
         } else {
             getLogger().severe("Command /stats not defined in plugin.yml");
         }
         
         // Register the universal /reload command.
         if (getCommand("reload") != null) {
             getCommand("reload").setExecutor(new ReloadCommand(this, statsManager, classesConfig));
         } else {
             getLogger().severe("Command /reload not defined in plugin.yml");
         }
         
         // Example event: welcome message on join.
         getServer().getPluginManager().registerEvents(new Listener() {
             @EventHandler
             public void onPlayerJoin(PlayerJoinEvent event) {
                 Player player = event.getPlayer();
                 player.sendMessage(Component.text("Welcome to MMO!"));
             }
         }, this);
         
         // Initialize and register the casting system.
         CastingManager castingManager = new CastingManager(classManager);
         getServer().getPluginManager().registerEvents(new CastingListener(castingManager), this);
         ServiceRegistry.registerCastingManager(castingManager);
         
         // Register the Stats GUI protection listener.
         getServer().getPluginManager().registerEvents(new StatsGUIListener(), this);
         
         // Register the StatsManager in the ServiceRegistry.
         ServiceRegistry.registerStatsManager(statsManager);
         
         // Generate or load classes.yml if not present
         ClassesConfigLoader classesConfigLoader = new ClassesConfigLoader(this);
         
         // Optionally, if you have a ClassesManager that loads the config:
         ClassesManager classesManager = new ClassesManager();
         classesManager.loadClasses(this);  // Ensure that loadClasses() reads the file in the data folder
         
         getLogger().info("classes.yml configuration has been loaded.");
         
         getLogger().info("SandMMO Enabled!");
    }

    @Override
    public void onDisable() {
         getLogger().info("SandMMO Disabled!");
    }
} 