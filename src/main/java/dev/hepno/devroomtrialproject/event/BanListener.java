package dev.hepno.devroomtrialproject.event;

import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.sql.Timestamp;

public class BanListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            databaseManager.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (databaseManager.isBanned(player.getUniqueId())) {
            Timestamp bannedAt = databaseManager.getBannedAt(player.getUniqueId());
            Timestamp expiresAt = databaseManager.getBanExpiresAt(player.getUniqueId());

            if (bannedAt.after(expiresAt)) {
                databaseManager.deleteBan(player.getUniqueId());
            } else {
                player.kickPlayer("You are banned from the server!");
            }
        }
    }

}
