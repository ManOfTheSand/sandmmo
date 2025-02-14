package com.sandcore.mmo;

import com.sandcore.mmo.command.ClassCommandExecutor;
import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.manager.PlayerClassDataManager;
import com.sandcore.mmo.stats.StatsCommands;
import com.sandcore.mmo.ReloadCommand;
import com.sandcore.mmo.stats.StatsManager;
import com.sandcore.mmo.stats.ClassesConfig;
import com.sandcore.mmo.util.ServiceRegistry;
import com.sandcore.mmo.casting.CastingListener;
import com.sandcore.mmo.casting.CastingManager;
import org.bukkit.plugin.java.JavaPlugin;

public class SandCoreMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Ensure data folder exists.
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Create instances of StatsManager and ClassesConfig.
        StatsManager statsManager = new StatsManager();
        ClassesConfig classesConfig = new ClassesConfig(getDataFolder());

        // Create and register ClassManager (for class system).
        ClassManager classManager = new ClassManager(getDataFolder());
        // Register the /class command for choosing classes.
        getCommand("class").setExecutor(new com.sandcore.mmo.command.ClassCommandExecutor(classManager));
        // Register the ClassManager into your ServiceRegistry if needed.
        ServiceRegistry.registerClassManager(classManager);

        // Register the /stats command to open the Stats GUI.
        if (getCommand("stats") != null) {
            getCommand("stats").setExecutor(new StatsCommands(this, statsManager, classesConfig));
        } else {
            getLogger().severe("Command /stats not defined in plugin.yml");
        }
        
        // Register the universal /reload command.
        // Ensure your plugin.yml defines the "reload" command.
        if (getCommand("reload") != null) {
            getCommand("reload").setExecutor(new ReloadCommand(this, statsManager, classesConfig));
        } else {
            getLogger().severe("Command /reload not defined in plugin.yml");
        }
        
        // Create CastingManager with ClassManager integration.
        CastingManager castingManager = new CastingManager(classManager);
        // Register CastingListener to check skill casting against player's class unlocks.
        getServer().getPluginManager().registerEvents(new CastingListener(castingManager), this);
        
        // Register the Stats GUI listener.
        getServer().getPluginManager().registerEvents(new com.sandcore.mmo.stats.StatsGUIListener(), this);
        
        // Register the StatsManager in the ServiceRegistry.
        ServiceRegistry.registerStatsManager(statsManager);
        // Also ensure that ServiceRegistry.getClassManager() returns a valid ClassManager instance.
        
        getLogger().info("SandMMO Enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SandMMO Disabled!");
    }
} 