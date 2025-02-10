package com.sandmmo;

import org.bukkit.plugin.java.JavaPlugin;
import com.willfp.eco.api.EcoAPI;
import com.willfp.eco.api.data.PersistentDataStorage;
import com.sandmmo.commands.MMOInfoCommand;
import com.sandmmo.listeners.PlayerJoinListener;

public class SandMMO extends JavaPlugin {

    private EcoAPI ecoAPI;
    private PersistentDataStorage dataStorage;

    @Override
    public void onEnable() {
        getLogger().info("SandMMO is starting up...");

        // Initialize the eco API instance.
        this.ecoAPI = EcoAPI.getInstance();
        if (ecoAPI == null) {
            getLogger().severe("Failed to load eco API! Disabling plugin.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // Obtain the persistent data storage for saving/loading data.
        this.dataStorage = ecoAPI.getPersistentDataStorage();

        // Register a sample command.
        if (getCommand("mmoinfo") != null) {
            getCommand("mmoinfo").setExecutor(new MMOInfoCommand());
        } else {
            getLogger().warning("Command mmoinfo not found in plugin.yml!");
        }

        // Register an event listener.
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        getLogger().info("SandMMO enabled with eco API version " + ecoAPI.getVersion());
    }

    @Override
    public void onDisable() {
        getLogger().info("SandMMO is shutting down...");
        // Save persistent data via eco.
        if (dataStorage != null) {
            dataStorage.saveAll();
        }
    }
}
