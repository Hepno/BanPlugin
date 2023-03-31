package dev.hepno.devroomtrialproject;

import dev.hepno.devroomtrialproject.command.BanCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class DevroomTrialProject extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        registerCommands();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static DevroomTrialProject getInstance() {
        return getPlugin(DevroomTrialProject.class);
    }

    public void registerCommands() {
        new BanCommand("ban", "ban.use", new String[]{}, "Command to ban a player from the server");
    }
}
