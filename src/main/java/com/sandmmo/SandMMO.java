package com.sandmmo;

import com.willfp.eco.core.EcoPlugin;
import com.sandmmo.commands.MMOInfoCommand;
import org.bukkit.command.PluginCommand;

public class SandMMO extends EcoPlugin {

    // Do not use @Override – EcoPlugin does not declare onEcoEnable()
    public void onEcoEnable() {
        getLogger().info("SandMMO enabled!");

        PluginCommand command = getCommand("mmoinfo");
        if (command != null) {
            command.setExecutor(new MMOInfoCommand());
        } else {
            getLogger().warning("mmoinfo command not registered in plugin.yml");
        }
    }
}