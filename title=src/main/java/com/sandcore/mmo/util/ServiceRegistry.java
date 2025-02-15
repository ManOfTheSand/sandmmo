package com.sandcore.mmo.util;

import org.bukkit.plugin.java.JavaPlugin;
import com.sandcore.mmo.casting.CastingManager;

public final class ServiceRegistry {

    private static volatile JavaPlugin plugin;
    private static volatile CastingManager castingManager;

    private ServiceRegistry() {} // Prevent instantiation.

    public static void registerPlugin(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void registerCastingManager(CastingManager manager) {
        castingManager = manager;
    }

    public static CastingManager getCastingManager() {
        return castingManager;
    }
}