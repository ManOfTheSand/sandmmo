package com.sandcore.mmo;

import com.sandcore.mmo.command.ClassCommandExecutor;
import com.sandcore.mmo.manager.ClassManager;
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
        // Ensure the data folder exists.
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Create instances of StatsManager and ClassesConfig.
        StatsManager statsManager = new StatsManager();
        ClassesConfig classesConfig = new ClassesConfig(getDataFolder());

        // Create and register the ClassManager (for the classes system).
        ClassManager classManager = new ClassManager(getDataFolder());
        // Register the /class command for choosing classes.
        getCommand("class").setExecutor(new ClassCommandExecutor(classManager));
        ServiceRegistry.registerClassManager(classManager);

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

        // Initialize and register the casting system.
        CastingManager castingManager = new CastingManager(classManager);
        // Register CastingListener (which checks class-based skill unlocks during casting).
        getServer().getPluginManager().registerEvents(new CastingListener(castingManager), this);

        // Register the Stats GUI protection listener (if applicable).
        getServer().getPluginManager().registerEvents(new com.sandcore.mmo.stats.StatsGUIListener(), this);

        // Register the StatsManager in the ServiceRegistry.
        ServiceRegistry.registerStatsManager(statsManager);

        getLogger().info("SandMMO Enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SandMMO Disabled!");
    }
} 