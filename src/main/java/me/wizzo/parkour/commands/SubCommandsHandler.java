package me.wizzo.parkour.commands;

import me.wizzo.parkour.Parkour;
import me.wizzo.parkour.commands.subcommands.DelPosCommand;
import me.wizzo.parkour.commands.subcommands.GetTimerCommand;
import me.wizzo.parkour.commands.subcommands.HelpCommand;
import me.wizzo.parkour.commands.subcommands.SetPosCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class SubCommandsHandler implements CommandExecutor {

    private final Parkour main;
    private final HelpCommand helpCmd;
    private final SetPosCommand setPosCmd;
    private final DelPosCommand delPosCmd;
    private final GetTimerCommand getTimerCmd;

    public SubCommandsHandler(Parkour main) {
        this.main = main;
        this.helpCmd = new HelpCommand(main, main.getAdminPermission());
        this.setPosCmd = new SetPosCommand(main, main.getAdminPermission());
        this.delPosCmd = new DelPosCommand(main, main.getAdminPermission());
        this.getTimerCmd = new GetTimerCommand(main, main.getAdminPermission());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!main.havePermission(sender, main.getAdminPermission())) {
            sender.sendMessage(main.getConfig("NoPerm"));
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage(main.getConfig("Command-not-found"));
            return true;
        } else {
            switch (args[0].toLowerCase()) {
                case "help":
                    this.helpCmd.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    break;

                case "set":
                case "add":
                    this.setPosCmd.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    break;

                case "delete":
                case "del":
                case "remove":
                    this.delPosCmd.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    break;

                case "timer":
                case "tempo":
                    this.getTimerCmd.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
                    break;

                default:
                    sender.sendMessage(main.getConfig("Command-not-found"));
                    break;
            }
        }
        return false;
    }
}
