package com.sandmmo.config;

import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.ExtendableConfig;
import com.willfp.eco.core.config.interfaces.Config;
import com.willfp.eco.core.PluginLike;

public class MessagesConfig extends ExtendableConfig {
    public MessagesConfig(PluginLike plugin) {
        super("messages", true, plugin, MessagesConfig.class, "", ConfigType.YAML, new String[0]);
    }

    public String getClassSelectMessage() {
        return this.getFormattedString("class-select");
    }
}