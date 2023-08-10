package me.wizzo.parkour.commands.subcommands;

import me.wizzo.parkour.Parkour;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetPosCommand implements CommandExecutor {

    private final Parkour main;
    private final String permission;

    public SetPosCommand(Parkour main, String permission) {
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
            player.sendMessage(main.getConfig("SetPos.No-args-found"));
            return true;
        }

        String input = args[0].toLowerCase();
        Block targetBlock = player.getTargetBlock(null, 4);
        Location targetLocation = targetBlock.getLocation();

        if (targetBlock.getType() == Material.matchMaterial(main.getConfig("Plate-material").toUpperCase())) {
            try {
                switch (input) {
                    case "start":
                    case "inizio":
                        main.setCoords("Position.Start.World", targetLocation.getWorld().getName());
                        main.setCoords("Position.Start.X", targetLocation.getX());
                        main.setCoords("Position.Start.Y", targetLocation.getY());
                        main.setCoords("Position.Start.Z", targetLocation.getZ());
                        main.saveCoords();
                        player.sendMessage(main.getConfig("SetPos.Success.Start"));
                        break;

                    case "finish":
                    case "fine":
                        main.setCoords("Position.Finish.World", targetLocation.getWorld().getName());
                        main.setCoords("Position.Finish.X", targetLocation.getX());
                        main.setCoords("Position.Finish.Y", targetLocation.getY());
                        main.setCoords("Position.Finish.Z", targetLocation.getZ());
                        main.saveCoords();
                        player.sendMessage(main.getConfig("SetPos.Success.Finish"));
                        break;

                    case "checkpoint":
                        String world = targetLocation.getWorld().getName();
                        double x = targetLocation.getX();
                        double y = targetLocation.getY();
                        double z = targetLocation.getZ();

                        if (main.getCheckpointList().containsKey(world + x + y + z)) {
                            player.sendMessage(main.getConfig("SetPos.Checkpoint-already-exist"));
                        } else {
                            int addCheckpointNumber = Integer.parseInt(main.getCoords("dont-edit-this")) + 1;
                            main.setCoords("Position.Checkpoint." + addCheckpointNumber + ".World", world);
                            main.setCoords("Position.Checkpoint." + addCheckpointNumber + ".X", x);
                            main.setCoords("Position.Checkpoint." + addCheckpointNumber + ".Y", y);
                            main.setCoords("Position.Checkpoint." + addCheckpointNumber + ".Z", z);
                            main.setCoords("dont-edit-this", addCheckpointNumber);
                            main.saveCoords();

                            player.sendMessage(main.getConfig("SetPos.Success.Checkpoint")
                                    .replace("{numero}", String.valueOf(addCheckpointNumber))
                            );
                        }
                        break;

                    default:
                        player.sendMessage(main.getConfig("SetPos.No-args-found"));
                        break;
                }
                main.reloadCheckpointList();

            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage("Error");
            }
        }
        return false;
    }
}
