package com.sandmmo;

import com.sandmmo.commands.ClassCommand;
import com.sandmmo.listeners.ClassListener;
import com.sandmmo.managers.ClassManager;
import com.sandmmo.managers.PlayerManager;
import com.sandmmo.managers.SkillManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

public final class SandMMO extends JavaPlugin {
    private ClassManager classManager;
    private PlayerManager playerManager;
    private SkillManager skillManager;
    private FileConfiguration classesConfig;
    private File classesFile;

    @Override
    public void onEnable() {
        // Initialize managers
        this.classManager = new ClassManager(this);
        this.playerManager = new PlayerManager(this);
        this.skillManager = new SkillManager(this);

        // Load configurations
        loadClassesConfig();

        // Register commands
        getCommand("class").setExecutor(new ClassCommand(this));

        // Register listeners
        getServer().getPluginManager().registerEvents(new ClassListener(this), this);
    }

    public void loadClassesConfig() {
        if (classesFile == null) {
            classesFile = new File(getDataFolder(), "classes.yml");
        }

        if (!classesFile.exists()) {
            saveResource("classes.yml", false);
        }

        classesConfig = YamlConfiguration.loadConfiguration(classesFile);
    }

    public FileConfiguration getClassesConfig() {
        if (classesConfig == null) {
            loadClassesConfig();
        }
        return classesConfig;
    }

    public void saveClassesConfig() {
        if (classesConfig == null || classesFile == null) {
            return;
        }

        try {
            getClassesConfig().save(classesFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save classes.yml!", ex);
        }
    }

    @Override
    public void onDisable() {
        saveClassesConfig();
    }

    public ClassManager getClassManager() {
        return classManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public SkillManager getSkillManager() {
        return skillManager;
    }
}