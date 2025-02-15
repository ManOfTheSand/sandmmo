package com.sandcore.mmo.util;

import org.bukkit.plugin.java.JavaPlugin;
import com.sandcore.mmo.casting.CastingManager;

public final class ServiceRegistry {
    private static volatile JavaPlugin plugin;
    private static volatile CastingManager castingManager;

    // Private constructor to prevent instantiation.
    private ServiceRegistry() {}

    // Register and retrieval methods for ClassManager.

    public static void registerCastingManager(CastingManager manager) {
        castingManager = manager;
    }

    public static CastingManager getCastingManager() {
        return castingManager;
    }
} 
}