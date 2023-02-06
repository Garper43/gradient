package org.garper.gradient;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("gradient").setExecutor(new GradientCommand());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
