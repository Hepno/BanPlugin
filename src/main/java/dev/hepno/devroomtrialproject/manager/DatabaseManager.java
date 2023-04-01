package dev.hepno.devroomtrialproject.manager;

import dev.hepno.devroomtrialproject.DevroomTrialProject;

import java.sql.*;
import java.util.UUID;

public class DatabaseManager {

    private final DevroomTrialProject plugin = DevroomTrialProject.getInstance();

    // Database Credentials
    private final String HOST = plugin.getConfig().getString("mysql.host");
    private final String PORT = plugin.getConfig().getString("mysql.port");
    private final String DATABASE = plugin.getConfig().getString("mysql.database");
    private final String USERNAME = plugin.getConfig().getString("mysql.username");
    private final String PASSWORD = plugin.getConfig().getString("mysql.password");

    private Connection connection;

    public void connect() throws SQLException {
        connection = DriverManager.getConnection("jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE, USERNAME, PASSWORD);
    }

    public void disconnect() throws SQLException {
        if (isConnected()) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Connection getConnection() { return connection; }

    public boolean isConnected() { return connection != null; }

    // Getters and Setters
    public void createTable() {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `bans` (`uuid` VARCHAR(36) NOT NULL," +
                    " `isBanned` BOOLEAN NOT NULL, `reason` VARCHAR(255) NOT NULL, `bannedBy` VARCHAR(36) NOT NULL, `bannedAt` DATETIME NOT NULL," +
                    "banExpiresAt DATETIME NOT NULL, PRIMARY KEY (`uuid`))"
            );
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createHistoryTable() {
        try {
            PreparedStatement ps = connection.prepareStatement("CREATE TABLE IF NOT EXISTS `ban_history` (`banId` INT NOT NULL AUTO_INCREMENT," +
                    "`uuid` VARCHAR(36) NOT NULL, `notes` VARCHAR(255) NOT NULL, `reason` VARCHAR(255) NOT NULL, `bannedBy` VARCHAR(36) NOT NULL," +
                    " `bannedAt` DATETIME NOT NULL, `banExpiresAt` DATETIME NOT NULL, PRIMARY KEY (`banId`))"
            );
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void createBan(UUID uuid, boolean isBanned, String reason, UUID bannedBy, Timestamp bannedAt, Timestamp banExpiresAt, String notes) {
        try {
            // Create ban
            PreparedStatement ps = connection.prepareStatement("INSERT INTO `bans` (`uuid`, `isBanned`, `reason`, `bannedBy`," +
                    " `bannedAt`, `banExpiresAt`)" + " VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps.setString(1, uuid.toString());
            ps.setBoolean(2, isBanned);
            ps.setString(3, reason);
            ps.setString(4, bannedBy.toString());
            ps.setTimestamp(5, bannedAt);
            ps.setTimestamp(6, banExpiresAt);
            ps.executeUpdate();

            // Create ban history
            PreparedStatement ps2 = connection.prepareStatement("INSERT INTO `ban_history` (`uuid`, `notes`, `reason`, `bannedBy`," +
                    " `bannedAt`, `banExpiresAt`)" + " VALUES (?, ?, ?, ?, ?, ?)"
            );
            ps2.setString(1, uuid.toString());
            ps2.setString(2, notes);
            ps2.setString(3, reason);
            ps2.setString(4, bannedBy.toString());
            ps2.setTimestamp(5, bannedAt);
            ps2.setTimestamp(6, banExpiresAt);
            ps2.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBan(UUID uuid, boolean isBanned, String reason, UUID bannedBy, String bannedAt, String banExpiresAt) {
        try {
            PreparedStatement ps = connection.prepareStatement("UPDATE `bans` SET `isBanned` = ?, `reason` = ?, `bannedBy` = ?," +
                    " `bannedAt` = ?, `banExpiresAt` = ? WHERE `uuid` = ?"
            );
            ps.setBoolean(1, isBanned);
            ps.setString(2, reason);
            ps.setString(3, bannedBy.toString());
            ps.setString(4, bannedAt);
            ps.setString(5, banExpiresAt);
            ps.setString(6, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBan(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("DELETE FROM `bans` WHERE `uuid` = ?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isBanned(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `bans` WHERE `uuid` = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean("isBanned");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String getReason(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `bans` WHERE `uuid` = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("reason");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getBannedBy(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `bans` WHERE `uuid` = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("bannedBy");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Timestamp getBannedAt(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `bans` WHERE `uuid` = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getTimestamp("bannedAt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Timestamp getBanExpiresAt(UUID uuid) {
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `bans` WHERE `uuid` = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getTimestamp("banExpiresAt");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String[][] getBanHistory(UUID uuid) {
        try {

            // for loop to get all bans (all fields) that match the users UUID. To be used for /banhistory command to show all bans that match the users UUID
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM `ban_history` WHERE `uuid` = ?");
            ps.setString(1, uuid.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String[][] banHistory = new String[rs.getFetchSize()][5];
                for (int i = 0; i < rs.getFetchSize(); i++) {
                    String[] ban = new String[5];
                    ban[0] = rs.getString("notes");
                    ban[1] = rs.getString("reason");
                    ban[2] = rs.getString("bannedBy");
                    ban[3] = rs.getString("bannedAt");
                    ban[4] = rs.getString("banExpiresAt");
                    banHistory[i] = ban;
                }
                return banHistory;
            }
            return null;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
