package dev.hepno.devroomtrialproject;

import dev.hepno.devroomtrialproject.command.BanCommand;
import dev.hepno.devroomtrialproject.command.BanHistory;
import dev.hepno.devroomtrialproject.command.UnbanCommand;
import dev.hepno.devroomtrialproject.event.BanListener;
import dev.hepno.devroomtrialproject.event.GUIListener;
import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class DevroomTrialProject extends JavaPlugin {

    private static DevroomTrialProject instance;

    //DONE: Create new database for ban history. Same as the current one, but with a new table that uses ban_id as a primary key
    //todo 2: Create the Ban History GUI. It should list all info from the ban history table.
    //DONE: Create a command to view the ban history of a player. It should open the Ban History GUI (for that player)
    //todo 4: Add a page system to the Ban History GUI. It should have a max of 45 entries per page, with no limit on the amount of pages.

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
            databaseManager.createHistoryTable();
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
        new BanCommand("ban", "ban.use", new String[]{"tempban"}, "Command to ban a player from the server", this);
        new UnbanCommand("unban", "unban.use", new String[]{}, "Command to unban a player from the server");
        new BanHistory("banhistory", "banhistory.use", new String[]{}, "Command to view the ban history of a player", this);
    }

    public void registerEvents() {
        getServer().getPluginManager().registerEvents(new BanListener(), this);
        getServer().getPluginManager().registerEvents(new GUIListener(this), this);
    }

}
