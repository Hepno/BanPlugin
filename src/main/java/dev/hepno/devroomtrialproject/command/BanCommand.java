package dev.hepno.devroomtrialproject.command;

import dev.hepno.devroomtrialproject.manager.Command;
import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.sql.Timestamp;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BanCommand extends Command {

    public BanCommand(String command, String permission, String[] aliases, String description) {
        super(
                command,
                permission,
                aliases,
                description
        );
    }

    // todo: override vanilla ban command
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof OfflinePlayer)) {
            sender.sendMessage("You must be a player to use this command!");
            return;
        }
        Player player = (Player) sender;

        // Ensure command is used correctly
        if (args.length == 0) {
            player.sendMessage("You must specify a player to ban!");
            return;
        }

        if (args.length == 1) {
            player.sendMessage("You must specify a duration for the ban!");
            return;
        }

        if (args.length == 2) {
            player.sendMessage("You must specify a reason for the ban!");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        Player targetPlayer = Bukkit.getPlayer(args[0]);
        int[] duration = convert(args[1]);

        // Ban player
        if (targetPlayer != null) {
            targetPlayer.kickPlayer("You have been banned from the server for " + args[1] + " for " + args[2]);
            DatabaseManager databaseManager = new DatabaseManager();
            Timestamp bannedAt = new Timestamp(System.currentTimeMillis());
            Timestamp banExpiresAt = new Timestamp(System.currentTimeMillis() +
                    duration[0] * 2629746000L + duration[1] * 604800000L + duration[2] *
                    86400000L + duration[3] * 3600000L + duration[4] * 60000L + duration[5] * 1000L + duration[6]
            );
            databaseManager.createBan(targetPlayer.getUniqueId(), true, args[2], player.getUniqueId(), bannedAt, banExpiresAt);
        }

    }

    // Converter to convert duration string into six integers
    public int[] convert(String text) throws IllegalArgumentException {
        if (!text.replaceAll("(\\d+)\\s*(mo|h|m|s|w|d|ms)", "").equals(""))
            throw new IllegalArgumentException("Invalid time format");

        int[] duration = new int[7];

        Pattern pattern = Pattern.compile("(\\d+)\\s*(mo|h|m|s|w|d|ms)");
        Matcher matcher = pattern.matcher(text);

        while (matcher.find()) {
            int amount = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "mo":
                    duration[0] += amount;
                    break;
                case "w":
                    duration[1] += amount;
                    break;
                case "d":
                    duration[2] += amount;
                    break;
                case "h":
                    duration[3] += amount;
                    break;
                case "m":
                    duration[4] += amount;
                    break;
                case "s":
                    duration[5] += amount;
                    break;
                case "ms":
                    duration[6] += amount;
                    break;
            }
        }
        return duration;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
