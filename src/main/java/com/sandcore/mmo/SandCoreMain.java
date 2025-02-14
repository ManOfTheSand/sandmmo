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
import org.bukkit.entity.Player;
import com.sandcore.mmo.command.AdminStatsCommandExecutor;
import com.sandcore.mmo.command.MainCommandExecutor;
import com.sandcore.mmo.command.MainTabCompleter;
import com.sandcore.mmo.casting.CastingManager;
import com.sandcore.mmo.casting.CastingListener;
import com.sandcore.mmo.stats.StatsGUIListener;
import com.sandcore.mmo.stats.StatsCommands;
import com.sandcore.mmo.stats.ReloadCommand;
import com.sandcore.mmo.stats.ClassesConfig;

public class SandCoreMain extends JavaPlugin {

    private static SandCoreMain instance;

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
         ServiceRegistry.registerClassManager(new ClassManager(this));
         StatsManager statsManager = new StatsManager();
         ClassesConfig classesConfig = new ClassesConfig(getDataFolder());
         
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
         
         // Register the new advanced /stats command using our new asynchronous advanced stats GUI system.
         if (getCommand("stats") != null) {
             getCommand("stats").setExecutor(new StatsCommands(this, statsManager, classesConfig));
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
         
         // Add after other manager initializations
         CastingManager castingManager = new CastingManager(this);
         getServer().getPluginManager().registerEvents(new CastingListener(castingManager), this);
         
         // Add to ServiceRegistry if needed
         ServiceRegistry.registerCastingManager(castingManager);
         
         // Register the /resetstats command
         getCommand("resetstats").setExecutor(new com.sandcore.mmo.command.ResetStatsCommandExecutor());
         getCommand("resetstats").setTabCompleter(new com.sandcore.mmo.command.ResetStatsCommandExecutor());
         
         // Register the stats GUI protection listener.
         getServer().getPluginManager().registerEvents(new StatsGUIListener(), this);
         
         // Register the universal /reload command.
         // Ensure your plugin.yml defines the "reload" command.
         getCommand("reload").setExecutor(new ReloadCommand(this, statsManager, classesConfig));
         
         // Optionally, register these managers into your ServiceRegistry so they are accessible elsewhere.
         ServiceRegistry.registerStatsManager(statsManager);
         // Also ensure that ServiceRegistry.getClassManager() returns a valid ClassManager instance.
         
         getLogger().info("SandCoreMain plugin enabled!");
    }

    @Override
    public void onDisable() {
         getLogger().info("SandCoreMain plugin disabled!");
    }
} 