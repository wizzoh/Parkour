package me.wizzo.parkour.listeners;

import me.wizzo.parkour.Parkour;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class ChatListener implements Listener {

    private final Parkour main;

    public ChatListener(Parkour main) {
        this.main = main;
    }

    @EventHandler
    public void onChat(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        String message = event.getMessage();

        if (message.startsWith("/") && main.getPlayerList().containsKey(player.getUniqueId())) {
            if (!main.havePermission(player, main.getAdminPermission())) {
                event.setCancelled(true);
                player.sendMessage(main.getConfig("Block-command-into-parkour"));
            }
        }
    }
}
