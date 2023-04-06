package dev.hepno.devroomtrialproject.command;

import dev.hepno.devroomtrialproject.gui.HistoryGUI;
import dev.hepno.devroomtrialproject.manager.Command;
import dev.hepno.devroomtrialproject.manager.DatabaseManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.List;

public class BanHistory extends Command {

        public BanHistory(String command, String permission, String[] aliases, String description) {
            super(
                    command,
                    permission,
                    aliases,
                    description
            );
        }

        @Override
        public void execute(CommandSender sender, String[] args) {
            // Connect to database
            DatabaseManager databaseManager = new DatabaseManager();

            try {
                databaseManager.connect();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            // Ensure command is used correctly
            if (args.length == 0) {
                sender.sendMessage("You must specify a player to check the ban history of!");
                return;
            }

            if (args.length > 1) {
                sender.sendMessage("Too many arguments!");
                return;
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            // Open ban GUI here
            new HistoryGUI((Player) sender, 1);

        }

        @Override
        public List<String> onTabComplete(CommandSender sender, String[] args) {
            return null;
        }
}
