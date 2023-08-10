package me.wizzo.parkour.listeners;

import me.wizzo.parkour.Parkour;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener {

    private final Parkour main;

    public PlayerJoinQuitListener(Parkour main) {
        this.main = main;
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        main.getPlayerList().remove(player.getUniqueId());
    }
}
