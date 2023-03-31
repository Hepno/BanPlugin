package dev.hepno.devroomtrialproject.command;

import dev.hepno.devroomtrialproject.manager.Command;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BanCommand extends Command {

    public BanCommand(String command, String permission, String[] aliases, String description) {
        super(
                command,
                permission,
                aliases,
                description
        );
    }

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

        // Get duration
        // Durations in seconds, minutes, hours, days, weeks, and months. Format: 1s, 1m, 1h, 1d, 1w, 1mo, and they should be able to be combined. For example, 1h30m would be 1 hour and 30 minutes.

        int durationSeconds = 0;
        int durationMinutes = 0;
        int durationHours = 0;
        int durationDays = 0;
        int durationWeeks = 0;
        int durationMonths = 0;

        if (args[1].contains("s")) {
            String[] duration = args[1].split("s");
            durationSeconds = Integer.parseInt(duration[0]);
        }
        if (args[1].contains("m")) {
            String[] duration = args[1].split("m");
            durationMinutes = Integer.parseInt(duration[0]);
        }
        if (args[1].contains("h")) {
            String[] duration = args[1].split("h");
            durationHours = Integer.parseInt(duration[0]);
        }
        if (args[1].contains("d")) {
            String[] duration = args[1].split("d");
            durationDays = Integer.parseInt(duration[0]);
        }
        if (args[1].contains("w")) {
            String[] duration = args[1].split("w");
            durationWeeks = Integer.parseInt(duration[0]);
        }
        if (args[1].contains("mo")) {
            String[] duration = args[1].split("mo");
            durationMonths = Integer.parseInt(duration[0]);
        }

        if (durationSeconds == 0 && durationMinutes == 0 && durationHours == 0 && durationDays == 0 && durationWeeks == 0 && durationMonths == 0) {
            player.sendMessage("You must specify a valid duration for the ban!");
            return;
        }

        // Debug
        System.out.println(durationSeconds);
        System.out.println(durationMinutes);
        System.out.println(durationHours);
        System.out.println(durationDays);
        System.out.println(durationWeeks);
        System.out.println(durationMonths);


        // Ban player
        if (targetPlayer != null) {
            targetPlayer.kickPlayer("You have been banned from the server for " + args[1] + " for " + args[2]);
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
