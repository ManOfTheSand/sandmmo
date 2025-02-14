package com.sandcore.mmo.util;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.stats.StatsManager;
import com.sandcore.mmo.casting.CastingManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class ServiceRegistry {

    private static JavaPlugin plugin;
    private static ClassManager classManager;
    private static StatsManager statsManager;
    private static CastingManager castingManager;

    private ServiceRegistry() {} // Prevent instantiation.

    public static void registerPlugin(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void registerClassManager(ClassManager manager) {
        classManager = manager;
    }

    public static ClassManager getClassManager() {
        return classManager;
    }

    public static void registerStatsManager(StatsManager manager) {
        statsManager = manager;
    }

    public static StatsManager getStatsManager() {
        return statsManager;
    }

    public static void registerCastingManager(CastingManager manager) {
        castingManager = manager;
    }

    public static CastingManager getCastingManager() {
        return castingManager;
    }
} 