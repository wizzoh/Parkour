package me.wizzo.parkour.commands.subcommands;

import me.wizzo.parkour.Parkour;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetTimerCommand implements CommandExecutor {
    private final Parkour main;
    private final String permission;

    public GetTimerCommand(Parkour main, String permission) {
        this.main = main;
        this.permission = permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(main.getConfig("Only-player-execute-command"));
                return true;
            }

            Player player = (Player) sender;

            try {
                String timers = main.getTimers(player.getUniqueId().toString());
                player.sendMessage(main.getConfig("GetTimer.timer-own")
                        .replace("{timer}", timers)
                );
            } catch (Exception e) {
                sender.sendMessage(main.getConfig("GetTimer.timer-error"));
            }
            return true;
        }

        if (!main.havePermission(sender, permission)) {
            sender.sendMessage(main.getConfig("NoPerm"));
            return true;
        }
        OfflinePlayer offlinePlayer = main.getServer().getOfflinePlayer(args[0].toLowerCase());
        String offlinePlayerUuid = offlinePlayer.getUniqueId().toString();

        try {
            String timers = main.getTimers(offlinePlayerUuid);
            sender.sendMessage(main.getConfig("GetTimer.timer-other")
                    .replace("{timer}", timers)
                    .replace("{player}", offlinePlayer.getName())
            );
        } catch (Exception e) {
            sender.sendMessage(main.getConfig("GetTimer.timer-error"));
        }
        return true;
    }
}
