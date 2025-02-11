package com.sandcore.mmo;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import net.kyori.adventure.text.Component;

public class SandCoreMain extends JavaPlugin {

    @Override
    public void onEnable() {
        // Register the /mmocore command executor.
        if (getCommand("mmocore") != null) {
            getCommand("mmocore").setExecutor(new MMOCoreCommandExecutor());
        } else {
            getLogger().severe("Command /mmocore not defined in plugin.yml");
        }

        // Register an event listener that sends a welcome message when a player joins.
        getServer().getPluginManager().registerEvents(new Listener() {
            @EventHandler
            public void onPlayerJoin(PlayerJoinEvent event) {
                event.getPlayer().sendMessage(Component.text("Welcome to MMO!"));
            }
        }, this);

        getLogger().info("SandCoreMain plugin enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("SandCoreMain plugin disabled!");
    }
} 