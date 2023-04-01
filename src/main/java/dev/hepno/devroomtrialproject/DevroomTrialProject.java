package dev.hepno.devroomtrialproject;

import dev.hepno.devroomtrialproject.command.BanCommand;
import dev.hepno.devroomtrialproject.command.UnbanCommand;
import dev.hepno.devroomtrialproject.event.BanListener;
import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class DevroomTrialProject extends JavaPlugin {

    private static DevroomTrialProject instance;

    @Override
    public void onEnable() {
        // Register methods
        registerCommands();
        registerEvents();
        instance = this;

        // Setup config
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);

        // Setup database
        DatabaseManager databaseManager = new DatabaseManager();
        try {
            databaseManager.connect();
            databaseManager.createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static DevroomTrialProject getInstance() { return instance; }

    public void registerCommands() {
        new BanCommand("ban", "ban.use", new String[]{"tempban"}, "Command to ban a player from the server");
        new UnbanCommand("unban", "unban.use", new String[]{}, "Command to unban a player from the server");
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new BanListener(), this);
    }
}
