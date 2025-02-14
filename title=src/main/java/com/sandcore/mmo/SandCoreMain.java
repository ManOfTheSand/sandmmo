package com.sandcore.mmo;

import com.sandcore.mmo.stats.StatsCommands;
import com.sandcore.mmo.ReloadCommand;
import com.sandcore.mmo.stats.StatsManager;
import com.sandcore.mmo.stats.ClassesConfig;
import com.sandcore.mmo.util.ServiceRegistry;
import org.bukkit.plugin.java.JavaPlugin;

public class SandCoreMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Create instances of StatsManager and ClassesConfig.
        StatsManager statsManager = new StatsManager();
        ClassesConfig classesConfig = new ClassesConfig(getDataFolder());
        
        // Register the /stats command to open the Stats GUI.
        // Ensure your plugin.yml defines the "stats" command.
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