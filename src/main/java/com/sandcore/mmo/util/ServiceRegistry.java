package com.sandcore.mmo.util;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.manager.XPManager;
import com.sandcore.mmo.manager.CurrencyManager;
import com.sandcore.mmo.manager.PartyManager;
import com.sandcore.mmo.manager.StatsManager;
import com.sandcore.mmo.command.AdminStatsCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;
import com.sandcore.mmo.casting.CastingManager;

public final class ServiceRegistry {

    private static volatile ClassManager classManager;
    private static volatile XPManager xpManager;
    private static volatile CurrencyManager currencyManager;
    private static volatile PartyManager partyManager;
    private static volatile StatsManager statsManager;
    private static volatile JavaPlugin plugin;
    private static volatile AdminStatsCommandExecutor adminStatsCommandExecutor;
    private static volatile CastingManager castingManager;

    // Private constructor to prevent instantiation.
    private ServiceRegistry() {}

    // Register and retrieval methods for ClassManager.
    public static void registerClassManager(ClassManager manager) {
        classManager = manager;
    }

    public static ClassManager getClassManager() {
        return classManager;
    }

    // Register and retrieval methods for XPManager.
    public static void registerXPManager(XPManager manager) {
        xpManager = manager;
    }

    public static XPManager getXPManager() {
        return xpManager;
    }

    // Register and retrieval methods for CurrencyManager.
    public static void registerCurrencyManager(CurrencyManager manager) {
        currencyManager = manager;
    }

    public static CurrencyManager getCurrencyManager() {
        return currencyManager;
    }

    // Register and retrieval methods for PartyManager.
    public static void registerPartyManager(PartyManager manager) {
        partyManager = manager;
    }

    public static PartyManager getPartyManager() {
        return partyManager;
    }

    // Register and retrieval methods for StatsManager.
    public static void registerStatsManager(StatsManager manager) {
        statsManager = manager;
    }

    public static StatsManager getStatsManager() {
        return statsManager;
    }

    public static void registerPlugin(JavaPlugin pluginInstance) {
        plugin = pluginInstance;
    }

    public static JavaPlugin getPlugin() {
        return plugin;
    }

    public static void registerAdminStatsCommandExecutor(AdminStatsCommandExecutor executor) {
        adminStatsCommandExecutor = executor;
    }

    public static AdminStatsCommandExecutor getAdminStatsCommandExecutor() {
        return adminStatsCommandExecutor;
    }

    public static void registerCastingManager(CastingManager manager) {
        castingManager = manager;
    }

    public static CastingManager getCastingManager() {
        return castingManager;
    }
} 