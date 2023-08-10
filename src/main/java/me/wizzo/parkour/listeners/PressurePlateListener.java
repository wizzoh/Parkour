package me.wizzo.parkour.listeners;

import me.wizzo.parkour.Parkour;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.UUID;

public class PressurePlateListener implements Listener {

    private final Parkour main;

    public PressurePlateListener(Parkour main) {
        this.main = main;
    }

    @EventHandler
    public void onPressurePlate(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        UUID playerUuid = player.getUniqueId();
        Block block = event.getClickedBlock();

        if (event.getAction().equals(Action.PHYSICAL) && block.getType() == Material.matchMaterial(main.getConfig("Plate-material").toUpperCase())) {

            double blockX = block.getLocation().getX();
            double blockY = block.getLocation().getY();
            double blockZ = block.getLocation().getZ();
            boolean isPlateStart;
            boolean isPlateFinish;

            try {
                double startPlateX = Double.parseDouble(main.getCoords("Position.Start.X"));
                double startPlateY = Double.parseDouble(main.getCoords("Position.Start.Y"));
                double startPlateZ = Double.parseDouble(main.getCoords("Position.Start.Z"));
                isPlateStart = blockX == startPlateX && blockY == startPlateY && blockZ == startPlateZ;
            } catch (Exception e) {
                return;
            }

            try {
                double finishPlateX = Double.parseDouble(main.getCoords("Position.Finish.X"));
                double finishPlateY = Double.parseDouble(main.getCoords("Position.Finish.Y"));
                double finishPlateZ = Double.parseDouble(main.getCoords("Position.Finish.Z"));
                isPlateFinish = blockX == finishPlateX && blockY == finishPlateY && blockZ == finishPlateZ;
            } catch (Exception e) {
                return;
            }

            // PER START
            if (isPlateStart) {
                if (main.getPlayerList().containsKey(playerUuid)) {
                    main.getPlayerList().remove(playerUuid);
                }
                main.getTasks().getStartParkour(player);
            }

            // PER FINE
            if (isPlateFinish && main.getPlayerList().containsKey(playerUuid)) {
                main.getTasks().getFinishParkour(player);
            }

            // PER CHECKPOINT
            if (main.getPlayerList().containsKey(playerUuid)) {
                String all = block.getWorld().getName() + block.getLocation().getX() + block.getLocation().getY() + block.getLocation().getZ();

                if (main.getCheckpointList().containsKey(all)) {
                    String[] a = main.getCheckpointList().get(all);

                    if (main.getPlayerList().get(playerUuid) >= Integer.parseInt(a[0])) {
                        return;
                    }

                    player.sendMessage(main.getConfig("Event.Plate.Checkpoint-message")
                            .replace("{numero}", a[0])
                            .replace("{timer}", main.getTasks().formatTime(main.getTasks().getPlayerTimer().get(playerUuid)))
                    );
                    main.getPlayerList().replace(playerUuid, Integer.parseInt(a[0]));
                }
            }
        }
    }
}
