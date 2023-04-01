package dev.hepno.devroomtrialproject.event;

import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Duration;
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
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // Get the units needed to create the message
            Duration difference = Duration.between(now.toLocalDateTime(), expiresAt.toLocalDateTime());
            long seconds = difference.getSeconds();
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;
            long weeks = days / 7;
            long months = days / 30;

            // Remove units used (eg, if there are 2 days, remove 48 hours)
            seconds -= minutes * 60;
            minutes -= hours * 60;
            hours -= days * 24;
            days -= weeks * 7;
            weeks -= months * 4;

            // Create a string to display the time remaining (a bit messy, but it works)
            String message = "In ";
            if (months > 0) {
                message += months + " months, ";
            }

            if (weeks > 0) {
                message += weeks + " weeks, ";
            }

            if (days > 0) {
                message += days + " days, ";
            }

            if (hours > 0) {
                message += hours + " hours, ";
            }

            if (minutes > 0) {
                message += minutes + " minutes, ";
            }

            if (seconds > 0) {
                message += seconds + " seconds, ";
            }

            if (expiresAt == null) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                event.setKickMessage("1");
            } else if (expiresAt.after(new Timestamp(System.currentTimeMillis()))) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_BANNED);
                event.setKickMessage(ChatColor.RED + "You have been banned from this server for " +
                databaseManager.getReason(uuid) + ". You will be unbanned " + message
                );
            } else {
                // The server does not check if the ban has expired periodically, so we need to remove it from the database if it has
                databaseManager.deleteBan(uuid);
            }
        }
    }

}
