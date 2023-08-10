package me.wizzo.parkour.commands.subcommands;

import me.wizzo.parkour.Parkour;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class DelPosCommand implements CommandExecutor {

    private final Parkour main;
    private final String permission;

    public DelPosCommand(Parkour main, String permission) {
        this.main = main;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(main.getConfig("Only-player-execute-command"));
            return true;
        }

        Player player = (Player) sender;
        if (!main.havePermission(player, permission)) {
            player.sendMessage(main.getConfig("NoPerm"));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(main.getConfig("DelPos.No-args-found"));
            return true;
        }

        String input = args[0].toLowerCase();

        try {
            switch (input) {
                case "start":
                case "inizio":
                    if (main.getCoords("Position.Start") != null) {
                        main.setCoords("Position.Start", null);
                        main.saveCoords();
                        player.sendMessage(main.getConfig("DelPos.Success.Start-removed"));
                    } else {
                        player.sendMessage(main.getConfig("DelPos.Start-not-exist"));
                    }
                    break;

                case "finish":
                case "fine":
                    if (main.getCoords("Position.Finish") != null) {
                        main.setCoords("Position.Finish", null);
                        main.saveCoords();
                        player.sendMessage(main.getConfig("DelPos.Success.Finish-removed"));
                    } else {
                        player.sendMessage(main.getConfig("DelPos.Finish-not-exist"));
                    }
                    break;

                case "checkpoint":
                    int checkpointNumber = Integer.parseInt(main.getCoords("dont-edit-this"));

                    if (checkpointNumber <= 0) {
                        player.sendMessage(main.getConfig("DelPos.Checkpoint.Not-found"));
                        return true;
                    }

                    if (args.length == 1) {
                        player.sendMessage(main.getConfig("DelPos.Checkpoint.No-args-found"));
                        return true;
                    }

                    if (args[1].equalsIgnoreCase("all")) {
                        main.setCoords("Position.Checkpoint", null);
                        main.setCoords("dont-edit-this", 0);
                        main.saveCoords();

                        player.sendMessage(main.getConfig("DelPos.Success.Checkpoint-all-removed"));
                        return true;
                    }

                    try {
                        int removeCheckpointNumber = checkpointNumber - 1;
                        main.setCoords("Position.Checkpoint." + args[1], null);
                        main.setCoords("dont-edit-this", removeCheckpointNumber);
                        main.saveCoords();

                        player.sendMessage(main.getConfig("DelPos.Success.Checkpoint-removed")
                                .replace("{numero}", args[1])
                        );
                    } catch (Exception e) {
                        player.sendMessage(main.getConfig("DelPos.Checkpoint.Not-found"));
                    }
                    break;

                default:
                    player.sendMessage(main.getConfig("DelPos.No-args-found"));
                    break;
            }
            main.reloadCheckpointList();

        } catch (Exception e) {
            e.printStackTrace();
            player.sendMessage("Error");
        }

        return false;
    }
}
