package com.sandcore.mmo.casting;

import com.sandcore.mmo.manager.ClassManager;
import com.sandcore.mmo.util.ServiceRegistry;
import io.lumine.mythic.api.skills.Skill;
import io.lumine.mythic.bukkit.MythicBukkit;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CastingManager {
    private final JavaPlugin plugin;
    private final Logger logger;
    private final Map<UUID, CastingState> castingPlayers = new HashMap<>();
    private final Map<String, Map<String, String>> classCombos = new HashMap<>();
    private int timeoutSeconds = 5;

    public CastingManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        loadConfiguration();
    }

    private class CastingState {
        StringBuilder combo = new StringBuilder();
        BukkitTask timeoutTask;
        String playerClass;
    }

    public void loadConfiguration() {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                File configFile = new File(plugin.getDataFolder(), "config.yml");
                YamlConfiguration config = YamlConfiguration.loadConfiguration(configFile);
                
                // Load casting timeout
                timeoutSeconds = config.getInt("casting.timeout", 5);
                
                // Load class combos
                classCombos.clear();
                if (config.contains("casting.classes")) {
                    for (String className : config.getConfigurationSection("casting.classes").getKeys(false)) {
                        Map<String, String> combos = new HashMap<>();
                        for (String combo : config.getConfigurationSection("casting.classes." + className).getKeys(false)) {
                            combos.put(combo.toUpperCase(), config.getString("casting.classes." + className + "." + combo));
                        }
                        classCombos.put(className.toLowerCase(), combos);
                    }
                }
                logger.info("Loaded casting configurations for " + classCombos.size() + " classes");
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Failed to load casting configuration", e);
            }
        });
    }

    public void toggleCastingMode(Player player) {
        UUID uuid = player.getUniqueId();
        if (castingPlayers.containsKey(uuid)) {
            cancelCasting(player);
            player.sendMessage("§cCasting mode disabled");
        } else {
            ClassManager classManager = ServiceRegistry.getClassManager();
            String playerClass = classManager.getPlayerClass(player) != null ? 
                classManager.getPlayerClass(player).getId().toLowerCase() : null;
            
            if (playerClass == null || !classCombos.containsKey(playerClass)) {
                player.sendMessage("§cNo casting abilities available for your class");
                return;
            }

            CastingState state = new CastingState();
            state.playerClass = playerClass;
            castingPlayers.put(uuid, state);
            player.sendMessage("§aCasting mode enabled - Perform a 3-click combo!");
        }
    }

    private void cancelCasting(Player player) {
        CastingState state = castingPlayers.remove(player.getUniqueId());
        if (state != null && state.timeoutTask != null) {
            state.timeoutTask.cancel();
        }
        // Update GUI through your existing GUI system
    }

    public void handleClick(Player player, ClickType clickType) {
        CastingState state = castingPlayers.get(player.getUniqueId());
        if (state == null) return;

        // Start timeout on first click
        if (state.combo.length() == 0) {
            state.timeoutTask = Bukkit.getScheduler().runTaskLater(plugin, () -> {
                cancelCasting(player);
                player.sendMessage("§cCast timed out!");
            }, timeoutSeconds * 20L);
        }

        // Record click
        state.combo.append(clickType == ClickType.LEFT ? "L" : "R");
        
        if (state.combo.length() == 3) {
            completeCast(player, state);
        }
    }

    private void completeCast(Player player, CastingState state) {
        try {
            String combo = state.combo.toString();
            Map<String, String> classSkills = classCombos.get(state.playerClass);
            String skillName = classSkills.get(combo);

            if (skillName != null) {
                Skill skill = MythicBukkit.inst().getSkillManager().getSkill(skillName).orElse(null);
                if (skill != null) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        skill.execute(skill.getDefaultCaster(), player.getLocation());
                        player.sendMessage("§aCasted: §e" + skillName);
                    });
                } else {
                    player.sendMessage("§cInvalid skill configuration");
                }
            } else {
                player.sendMessage("§cInvalid combo for your class");
            }
        } finally {
            cancelCasting(player);
        }
    }

    public enum ClickType {
        LEFT, RIGHT
    }
} 