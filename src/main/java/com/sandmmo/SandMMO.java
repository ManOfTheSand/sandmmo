package com.sandmmo;

import com.willfp.eco.core.EcoPlugin;
import com.sandmmo.commands.MMOInfoCommand;
import com.sandmmo.commands.MMOGUICommand;
import org.bukkit.command.PluginCommand;

public class SandMMO extends EcoPlugin {

    // Do not annotate with @Override – EcoPlugin does not declare onEcoEnable()
    public void onEcoEnable() {
        getLogger().info("SandMMO enabled!");

        PluginCommand infoCommand = getCommand("mmoinfo");
        if (infoCommand != null) {
            infoCommand.setExecutor(new MMOInfoCommand());
        } else {
            getLogger().warning("mmoinfo command not registered in plugin.yml");
        }

        PluginCommand guiCommand = getCommand("mmogui");
        if (guiCommand != null) {
            guiCommand.setExecutor(new MMOGUICommand(this));
        } else {
            getLogger().warning("mmogui command not registered in plugin.yml");
        }
    }
}