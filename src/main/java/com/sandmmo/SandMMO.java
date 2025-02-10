package com.sandmmo;

import org.bukkit.plugin.java.JavaPlugin;
import com.willfp.eco.core.EcoPlugin; // For optional Eco interactions
import com.sandmmo.gui.ClassGUI;    // Make sure this class exists
import java.io.File;

public class SandMMO extends JavaPlugin {

    private ClassGUI classGUI;

    @Override
    public void onEnable() {
        getLogger().info("Initializing SandMMO");

        // Ensure the plugin folder exists
        if (!getDataFolder().exists()) {
            getDataFolder().mkdirs();
        }

        // Check for lang.yml; if not found, save the default resource.
        File langFile = new File(getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            saveResource("lang.yml", false);
            getLogger().info("Default lang.yml saved.");
        } else {
            getLogger().info("lang.yml found.");
        }

        // Optionally, retrieve the Eco plugin instance if needed.
        EcoPlugin eco = (EcoPlugin) getServer().getPluginManager().getPlugin("eco");
        if (eco == null) {
            getLogger().warning("Eco plugin not found! Economy features will be disabled.");
        } else {
            getLogger().info("Eco plugin detected.");
        }

        // Initialize GUI for classes
        // If your ClassGUI requires a reference to the main plugin instance, pass "this".
        this.classGUI = new ClassGUI(this);

        // Additional initialization tasks here
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling SandMMO");
        // Cleanup if needed
    }

    // Added method for use in ClassCommand.
    public ClassGUI getClassGUI() {
        return this.classGUI;
    }
}