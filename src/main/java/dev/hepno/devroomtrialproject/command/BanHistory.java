package dev.hepno.devroomtrialproject.command;

import dev.hepno.devroomtrialproject.DevroomTrialProject;
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

    private final DevroomTrialProject plugin;

        public BanHistory(String command, String permission, String[] aliases, String description, DevroomTrialProject plugin) {
            super(
                    command,
                    permission,
                    aliases,
                    description
            );
            this.plugin = plugin;
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

            Player target = Bukkit.getPlayer(args[0]);
            // Open ban GUI here
            new HistoryGUI((Player) sender, target, 1, plugin);

        }

        @Override
        public List<String> onTabComplete(CommandSender sender, String[] args) {
            return null;
        }
}
