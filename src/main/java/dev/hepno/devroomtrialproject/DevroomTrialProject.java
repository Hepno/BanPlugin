package dev.hepno.devroomtrialproject;

import org.bukkit.plugin.java.JavaPlugin;

public final class DevroomTrialProject extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static DevroomTrialProject getInstance() {
        return getPlugin(DevroomTrialProject.class);
    }
}
