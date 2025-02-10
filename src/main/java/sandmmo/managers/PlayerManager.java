package sandmmo.managers;

import com.sandmmo.SandMMO;
import com.sandmmo.utils.MessageHelper;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {
    private final SandMMO plugin;
    private final Map<UUID, Map<String, Object>> playerData = new HashMap<>();
    private final File dataFolder;

    public PlayerManager(SandMMO plugin) {
        this.plugin = plugin;
        // Create a directory called "playerdata" within your plugin folder.
        this.dataFolder = new File(plugin.getDataFolder(), "playerdata");
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            MessageHelper.logError("Failed to create player data directory!");
        }
    }

    /**
     * Sets the player's chosen class, initializes their data,
     * saves the data, and applies initial class stats.
     */
    public void setPlayerClass(Player player, String className) {
        Map<String, Object> data = new HashMap<>();
        data.put("class", className);
        data.put("level", 1);
        data.put("experience", 0);
        playerData.put(player.getUniqueId(), data);

        // Save the player's data to file.
        savePlayerData(player.getUniqueId());

        // Apply the initial class stats to the player using SkillManager.
        plugin.getSkillManager().applyClassStats(player, className);
    }

    /**
     * Saves the data for all players.
     */
    public void saveAllData() {
        for (UUID uuid : playerData.keySet()) {
            savePlayerData(uuid);
        }
        MessageHelper.logInfo("Saved data for " + playerData.size() + " players.");
    }

    /**
     * Saves the player data associated with the given UUID to a YAML file.
     */
    public void savePlayerData(UUID uuid) {
        try {
            File file = new File(dataFolder, uuid + ".yml");
            YamlConfiguration config = new YamlConfiguration();

            Map<String, Object> data = playerData.get(uuid);
            if (data != null) {
                for (Map.Entry<String, Object> entry : data.entrySet()) {
                    config.set(entry.getKey(), entry.getValue());
                }
            }
            config.save(file);
        } catch (IOException e) {
            MessageHelper.logError("Failed to save player data for " + uuid + ": " + e.getMessage());
        }
    }

    /**
     * Loads player data from file if it exists.
     */
    public void loadPlayerData(UUID uuid) {
        File file = new File(dataFolder, uuid + ".yml");
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            Map<String, Object> data = new HashMap<>();
            for (String key : config.getKeys(false)) {
                data.put(key, config.get(key));
            }
            playerData.put(uuid, data);
        }
    }

    /**
     * Retrieves the in-memory player data; returns an empty map if none exists.
     */
    public Map<String, Object> getPlayerData(UUID uuid) {
        return playerData.getOrDefault(uuid, new HashMap<>());
    }

    /**
     * Convenience method to save data for a Player using their UUID.
     */
    public void savePlayerData(Player player) {
        savePlayerData(player.getUniqueId());
    }

    public void updatePlayerData(UUID uuid, Map<String, Object> playerData) {
    }

    public void setSkillCooldown(UUID uniqueId, String skillName, long l) {
    }
}