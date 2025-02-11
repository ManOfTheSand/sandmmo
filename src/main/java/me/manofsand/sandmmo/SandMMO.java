package me.manofsand.sandmmo;

import org.bukkit.plugin.java.JavaPlugin;
// Removed all unnecessary imports that referenced the removed classes.

public class SandMMO extends JavaPlugin {

    @Override
    public void onEnable() {
        // Initialize the main functionality.
        getLogger().info("SandMMO enabled!");
        // Removed initialization that registered commands, listeners, etc.
    }

    @Override
    public void onDisable() {
        getLogger().info("SandMMO disabled!");
    }
} 