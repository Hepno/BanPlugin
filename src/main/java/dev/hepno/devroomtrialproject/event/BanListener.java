package dev.hepno.devroomtrialproject.event;

import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.UUID;

public class BanListener implements Listener {

    @EventHandler
    public void onJoin(AsyncPlayerPreLoginEvent event) {
        UUID uuid = event.getUniqueId();
        DatabaseManager databaseManager = new DatabaseManager();

        try {
            databaseManager.connect();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (databaseManager.isBanned(uuid)) {
            Timestamp bannedAt = databaseManager.getBannedAt(uuid);
            Timestamp expiresAt = databaseManager.getBanExpiresAt(uuid);

            System.out.println(bannedAt);
            System.out.println(expiresAt);

            if (expiresAt == null) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                event.setKickMessage("1");
            } else if (expiresAt.after(new Timestamp(System.currentTimeMillis()))) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                event.setKickMessage(ChatColor.RED + "You have been banned from this server for " +
                        ChatColor.WHITE + "\"" + databaseManager.getReason(uuid) + "\"" + ChatColor.RED +
                        " until " + ChatColor.WHITE + expiresAt + ChatColor.RED + "!")
                ;
            } else {
                // The server does not check if the ban has expired periodically, so we need to remove it from the database if it has
                databaseManager.deleteBan(uuid);
            }
        }
    }

}
