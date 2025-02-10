package com.sandmmo.config;

import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.ExtendableConfig;
import org.bukkit.plugin.java.JavaPlugin;

public class MessagesConfig extends ExtendableConfig {
    public MessagesConfig(JavaPlugin plugin) {
        super("messages", plugin, true, ConfigType.YAML);
    }
}