package me.wizzo.parkour.commands.subcommands;

import me.wizzo.parkour.Parkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements CommandExecutor {

    private final Parkour main;
    private final String permission;

    public HelpCommand(Parkour main, String permission) {
        this.main = main;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        sender.sendMessage(main.getConfig("Help-message-formatted"));
        return true;
    }
}
