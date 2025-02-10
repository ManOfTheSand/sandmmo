package com.sandmmo.managers;

import com.sandmmo.SandMMO;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MobManager {
    private final SandMMO plugin;
    private final MythicBukkit mythicMobs;

    public MobManager(SandMMO plugin) {
        this.plugin = plugin;
        this.mythicMobs = MythicBukkit.inst();
    }

    public void spawnMob(Player player, String mobName) {
        Location location = player.getLocation();
        // Use the spawnMob method that accepts (String mobName, Location location)
        mythicMobs.getMobManager().spawnMob(mobName, location);
    }
}