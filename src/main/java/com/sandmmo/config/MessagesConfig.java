package com.sandmmo.config;

import com.willfp.eco.core.config.ConfigType;
import com.willfp.eco.core.config.ExtendableConfig;
import com.willfp.eco.core.PluginLike;

public class MessagesConfig extends ExtendableConfig {
    public MessagesConfig(PluginLike plugin) {
        super(
                "messages",
                false,
                plugin,
                MessagesConfig.class,
                "messages.yml",
                ConfigType.YAML
        );
    }
}