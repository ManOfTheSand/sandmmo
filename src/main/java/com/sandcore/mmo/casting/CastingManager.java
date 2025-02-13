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
    // Casting sound when entering casting mode.
    private String enterSoundName = "BLOCK_NOTE_BLOCK_PLING";
    private float enterSoundVolume = 1.0F;
    private float enterSoundPitch = 1.0F;

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
                
                // Load enter sound configuration
                if (config.isConfigurationSection("casting.enter_sound")) {
                    enterSoundName = config.getString("casting.enter_sound.name", "BLOCK_NOTE_BLOCK_PLING");
                    enterSoundVolume = (float) config.getDouble("casting.enter_sound.volume", 1.0);
                    enterSoundPitch = (float) config.getDouble("casting.enter_sound.pitch", 1.0);
                }
                
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
                // Debug: list loaded combos for each class
                for (Map.Entry<String, Map<String, String>> entry : classCombos.entrySet()) {
                    logger.fine("loadConfiguration: For class '" + entry.getKey() + "' loaded combos: " + entry.getValue().toString());
                }
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
            logger.fine("toggleCastingMode: Enabled casting mode for player " + player.getName() + " (class: " + state.playerClass + ")");

            // Play the enter casting sound
            try {
                player.playSound(player.getLocation(), org.bukkit.Sound.valueOf(enterSoundName), enterSoundVolume, enterSoundPitch);
                logger.fine("toggleCastingMode: Played enter casting sound (" + enterSoundName + ") for player " + player.getName());
            } catch (Exception ex) {
                player.sendMessage("§cError playing casting sound.");
                logger.warning("toggleCastingMode: Error playing casting sound for player " + player.getName() + ": " + ex.getMessage());
            }
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
            logger.fine("completeCast: Player " + player.getName() + " performed combo: " + combo);

            Map<String, String> classSkills = classCombos.get(state.playerClass);
            String skillName = classSkills.get(combo);

            if (skillName != null) {
                Skill skill = MythicBukkit.inst().getSkillManager().getSkill(skillName).orElse(null);
                if (skill != null) {
                    Bukkit.getScheduler().runTask(plugin, () -> {
                        MythicBukkit.inst().getAPIHelper().castSkill(player, skillName);
                        player.sendMessage("§aCasted: §e" + skillName);
                        logger.fine("completeCast: Successfully cast skill: " + skillName + " for player " + player.getName());
                    });
                } else {
                    player.sendMessage("§cInvalid skill configuration");
                    logger.warning("completeCast: Skill " + skillName + " not found for player " + player.getName());
                }
            } else {
                player.sendMessage("§cInvalid combo for your class");
                logger.warning("completeCast: Combo " + combo + " not valid for class " + state.playerClass + " (player " + player.getName() + ")");
            }
        } finally {
            cancelCasting(player);
            logger.fine("completeCast: Casting mode cancelled for player " + player.getName());
        }
    }

    public enum ClickType {
        LEFT, RIGHT
    }
} 