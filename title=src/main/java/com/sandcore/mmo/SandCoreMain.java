package com.sandcore.mmo;

import com.sandcore.mmo.stats.StatsCommands;
import com.sandcore.mmo.stats.ReloadCommand;
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
        getCommand("stats").setExecutor(new StatsCommands(this, statsManager, classesConfig));
        
        // Register the universal /reload command.
        getCommand("reload").setExecutor(new ReloadCommand(this, statsManager, classesConfig));
        
        // Register any necessary event listeners.
        getServer().getPluginManager().registerEvents(new com.sandcore.mmo.stats.StatsGUIListener(), this);
        
        // Optionally, register these managers into your ServiceRegistry so they are accessible elsewhere.
        ServiceRegistry.registerStatsManager(statsManager);
        // Also ensure ServiceRegistry.getClassManager() returns a valid ClassManager instance.
        
        getLogger().info("SandMMO Enabled!");
    }
    
    @Override
    public void onDisable() {
        getLogger().info("SandMMO Disabled!");
    }
} 