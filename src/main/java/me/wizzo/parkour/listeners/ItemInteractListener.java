package me.wizzo.parkour.listeners;

import me.wizzo.parkour.Parkour;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class ItemInteractListener implements Listener {

    private final Parkour main;

    public ItemInteractListener(Parkour main) {
        this.main = main;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        ItemStack currentItem = player.getInventory().getItemInMainHand();
        Action action = event.getAction();
        boolean click = action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK);
        ItemMeta currentItemMeta = currentItem.getItemMeta();

        if (!click) {
            return;
        }

        if (!main.getPlayerList().containsKey(playerUuid)) {
            return;
        }

        if (currentItemMeta.getDisplayName().equals(main.getConfig("Event.Item.Checkpoint-teleport.Name")) &&
                currentItem.getType() == Material.matchMaterial(main.getConfig("Event.Item.Checkpoint-teleport.Item").toUpperCase())) {

            try {
                int checkpoint = main.getPlayerList().get(playerUuid);
                String world;
                double x;
                double y;
                double z;

                if (checkpoint > 0) {
                    world = main.getCoords("Position.Checkpoint." + checkpoint + ".World");
                    x = Double.parseDouble(main.getCoords("Position.Checkpoint." + checkpoint + ".X"));
                    y = Double.parseDouble(main.getCoords("Position.Checkpoint." + checkpoint + ".Y"));
                    z = Double.parseDouble(main.getCoords("Position.Checkpoint." + checkpoint + ".Z"));

                } else {
                    world = main.getCoords("Position.Start.World");
                    x = Double.parseDouble(main.getCoords("Position.Start.X"));
                    y = Double.parseDouble(main.getCoords("Position.Start.Y"));
                    z = Double.parseDouble(main.getCoords("Position.Start.Z"));
                }
                player.teleport(new Location(main.getServer().getWorld(world), x, y, z));

            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(main.getConfig("Error"));
            }

        } else if (currentItemMeta.getDisplayName().equals(main.getConfig("Event.Item.Reset.Name")) &&
                currentItem.getType() == Material.matchMaterial(main.getConfig("Event.Item.Reset.Item").toUpperCase())) {

            try {
                String world = main.getCoords("Position.Start.World");
                double x = Double.parseDouble(main.getCoords("Position.Start.X"));
                double y = Double.parseDouble(main.getCoords("Position.Start.Y"));
                double z = Double.parseDouble(main.getCoords("Position.Start.Z"));
                player.teleport(new Location(main.getServer().getWorld(world), x, y, z));

            } catch (Exception e) {
                e.printStackTrace();
                player.sendMessage(main.getConfig("Error"));
            }

        } else if (currentItemMeta.getDisplayName().equals(main.getConfig("Event.Item.Cancel.Name")) &&
                currentItem.getType() == Material.matchMaterial(main.getConfig("Event.Item.Cancel.Item").toUpperCase())) {
            main.getTasks().getStopParkour(player);
        }
    }

    @EventHandler
    public void onMove(InventoryMoveItemEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        event.setCancelled(true);
    }
}
