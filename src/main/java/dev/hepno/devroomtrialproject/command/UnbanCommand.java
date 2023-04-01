package dev.hepno.devroomtrialproject.command;

import dev.hepno.devroomtrialproject.manager.Command;
import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class UnbanCommand extends Command {

        public UnbanCommand(String command, String permission, String[] aliases, String description) {
            super(
                    command,
                    permission,
                    aliases,
                    description
            );
        }

    @Override
    public void execute(CommandSender sender, String[] args) {
        DatabaseManager databaseManager = new DatabaseManager();

        // Connect to database
        try {
            databaseManager.connect();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Ensure command is used correctly
        if (args.length == 0) {
            sender.sendMessage("You must specify a player to unban!");
            return;
        }

        if (args.length > 1) {
            sender.sendMessage("Too many arguments!");
            return;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (databaseManager.isBanned(target.getUniqueId())) {
            databaseManager.deleteBan(target.getUniqueId());
            sender.sendMessage("Player " + args[0] + " has been unbanned!");
        } else {
            sender.sendMessage("Player " + args[0] + " is not banned. Did you mean to use /ban?");
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
