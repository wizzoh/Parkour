package me.wizzo.parkour.listeners;

import me.wizzo.parkour.Parkour;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener {

    private final Parkour main;

    public PlayerMoveListener(Parkour main) {
        this.main = main;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();

        if (location.getY() <= 0 && main.getPlayerList().containsKey(player.getUniqueId())) {

            try {
                Location spawn = new Location(
                        player.getWorld(),
                        Double.parseDouble(main.getCoords("Position.Start.X")),
                        Double.parseDouble(main.getCoords("Position.Start.Y")),
                        Double.parseDouble(main.getCoords("Position.Start.Z"))
                );
                player.teleport(spawn);
                main.getTasks().getStopParkour(player);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
    }
}
