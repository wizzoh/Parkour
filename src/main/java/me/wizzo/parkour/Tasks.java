package me.wizzo.parkour;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Tasks {

    private final Parkour main;
    private final Map<UUID, BukkitRunnable> playerTasks = new HashMap<>();
    private final Map<UUID, Integer> playerTimers = new HashMap<>();

    public Tasks(Parkour main) {
        this.main = main;
    }

    // TASK PER INIZIO PARKOUR. AGGIUNGE ITEM, PLAYER E PARTE IL TIMER
    private void startParkour(Player player) {
        UUID playerUuid = player.getUniqueId();
        int seconds = 0;

        playerTasks.remove(playerUuid);
        playerTimers.remove(playerUuid);
        main.getPlayerList().put(playerUuid, 0);
        player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Checkpoint-teleport.Slot")), main.getParkourCheckpointItem());
        player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Reset.Slot")), main.getParkourResetItem());
        player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Cancel.Slot")), main.getParkourCancelItem());

        BukkitRunnable timerTask = new BukkitRunnable() {
            int newSeconds = seconds;
            @Override
            public void run() {
                newSeconds++;
                playerTimers.put(playerUuid, newSeconds);
            }
        };
        playerTasks.put(playerUuid, timerTask);
        timerTask.runTaskTimerAsynchronously(main, 0L, 20L);
        player.sendMessage(main.getConfig("Event.Plate.Start-message"));
    }

    // TASK PER BLOCCO PARKOUR. RIMUOVE ITEM, PLAYER. PARKOUR NON FINITO.
    private void stopParkour(Player player) {
        UUID playerUuid = player.getUniqueId();
        BukkitRunnable timerTask = playerTasks.get(playerUuid);

        if (timerTask != null) {
            timerTask.cancel();
            player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Checkpoint-teleport.Slot")), null);
            player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Reset.Slot")), null);
            player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Cancel.Slot")), null);
            playerTasks.remove(playerUuid);
            playerTimers.remove(playerUuid);
            main.getPlayerList().remove(playerUuid);
        }
    }

    // TASK PER FINE PARKOUR. RIMUOVE ITEM, PLAYER E FERMA IL TIMER.
    private void finishParkour(Player player) {
        UUID playerUuid = player.getUniqueId();
        BukkitRunnable timerTask = playerTasks.get(playerUuid);

        if (timerTask != null) {
            timerTask.cancel();
            player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Checkpoint-teleport.Slot")), null);
            player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Reset.Slot")), null);
            player.getInventory().setItem(Integer.parseInt(main.getConfig("Event.Item.Cancel.Slot")), null);
            playerTasks.remove(playerUuid);
        }
        int seconds = playerTimers.get(playerUuid);
        player.sendMessage(main.getConfig("Event.Plate.Finish-message")
                .replace("{timer}", formatTime(seconds))
        );
        main.setTimers(playerUuid.toString() + ".Name", player.getName());
        main.setTimers(playerUuid.toString() + ".Timer", formatTime(seconds));
        main.saveTimers();
        playerTimers.remove(playerUuid);
        main.getPlayerList().remove(playerUuid);
    }

    public String formatTime(int seconds) {
        int secondss = seconds % 60;
        int minutes = seconds / 60;
        int hour = minutes / 60;
        return String.format("%02dh : %02dm : %02ds", hour, minutes, secondss);
    }

    public Map<UUID, Integer> getPlayerTimer() {
        return playerTimers;
    }

    public void getStartParkour(Player player) {
        startParkour(player);
    }

    public void getStopParkour(Player player) {
        stopParkour(player);
    }

    public void getFinishParkour(Player player) {
        finishParkour(player);
    }
}
