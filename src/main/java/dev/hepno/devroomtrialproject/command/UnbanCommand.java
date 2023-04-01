package dev.hepno.devroomtrialproject.command;

import dev.hepno.devroomtrialproject.manager.Command;
import org.bukkit.command.CommandSender;

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

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
