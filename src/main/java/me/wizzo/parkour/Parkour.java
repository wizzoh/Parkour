package me.wizzo.parkour;

import me.wizzo.parkour.commands.SubCommandsHandler;
import me.wizzo.parkour.files.ConfigFile;
import me.wizzo.parkour.files.CoordinatesFile;
import me.wizzo.parkour.files.TimersFile;
import me.wizzo.parkour.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class Parkour extends JavaPlugin {

    private final ConsoleCommandSender console = Bukkit.getConsoleSender();
    private String prefix;
    private ConfigFile configFile;
    private CoordinatesFile coordinatesFile;
    private TimersFile timersFile;
    private Tasks tasks;
    private final Map<UUID, Integer> playerList = new HashMap<>();
    private final Map<String, String[]> checkpointList = new HashMap<>();
    private final String adminPermission = "parkour.admin";

    @Override
    public void onEnable() {
        createFolder();
        commands();
        listeners();
        files();

        prefix = configFile.get().getString("Prefix");

        console.sendMessage("");
        console.sendMessage(messageFormat(prefix + "&7Plugin online!"));
        console.sendMessage(messageFormat(prefix + "&7By wizzo"));
        console.sendMessage("");

        reloadCheckpointList();

    }

    @Override
    public void onDisable() {
        console.sendMessage("");
        console.sendMessage(messageFormat(prefix + "&7Plugin offline!"));
        console.sendMessage(messageFormat(prefix + "&7By wizzo"));
        console.sendMessage("");
    }

    // PRIVATE

    private void createFolder() {
        if (!getDataFolder().exists()) {
            boolean success = getDataFolder().mkdirs();
            if (!success) {
                console.sendMessage(messageFormat("&cIMPOSSIBILE CREARE LA CARTELLA, PLUGIN DISABILITATO"));
                getPluginLoader().disablePlugin(this);
            }
        }
    }

    private void commands() {
        this.getCommand("parkour").setExecutor(new SubCommandsHandler(this));
    }

    private void listeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new ChatListener(this), this);
        pm.registerEvents(new PlayerJoinQuitListener(this), this);
        pm.registerEvents(new PressurePlateListener(this), this);
        pm.registerEvents(new ItemInteractListener(this), this);
        pm.registerEvents(new PlayerMoveListener(this), this);
    }

    private void files() {
        this.configFile = new ConfigFile(this);
        configFile.setup(this, "config.yml");

        this.coordinatesFile = new CoordinatesFile(this);
        coordinatesFile.setup(this, "checkpointCoords.yml");

        this.timersFile = new TimersFile(this);
        timersFile.setup(this, "timersSave.yml");

        this.tasks = new Tasks(this);
    }

    // PUBLIC

    public String messageFormat(String message) {
        return message.replaceAll("&", "§");
    }

    public ConsoleCommandSender getConsole() {
        return console;
    }

    public boolean havePermission(CommandSender sender, String permission) {
        return sender.hasPermission(permission) || sender.isOp();
    }

    public String getConfig(String string) {
        return messageFormat(configFile.get().getString(string)
                .replace("{prefix}", prefix)
        );
    }

    public String getCoords(String string) {
        return coordinatesFile.get().getString(string);
    }

    public void setCoords(String name, Object string) {
        coordinatesFile.get().set(name, string);
    }

    public void saveCoords() {
        coordinatesFile.save();
    }

    public String getTimers(String string) {
        return timersFile.get().getString(string);
    }

    public void setTimers(String name, Object object) {
        timersFile.get().set(name, object);
    }

    public void saveTimers() {
        timersFile.save();
    }

    public Map<UUID, Integer> getPlayerList() {
        return playerList;
    }

    public Map<String, String[]> getCheckpointList() {
        return checkpointList;
    }

    public String getAdminPermission() {
        return adminPermission;
    }

    public Tasks getTasks() {
        return tasks;
    }

    public void reloadCheckpointList() {
        int checkpointNumber = Integer.parseInt(getCoords("dont-edit-this"));

        if (checkpointNumber > 0) {
            checkpointList.clear();

            for (int i = 0; i <= checkpointNumber; i++) {
                String number = String.valueOf(i);
                String world = getCoords("Position.Checkpoint." + i + ".World");
                String x = getCoords("Position.Checkpoint." + i + ".X");
                String y = getCoords("Position.Checkpoint." + i + ".Y");
                String z = getCoords("Position.Checkpoint." + i + ".Z");

                String all = world + x + y + z;
                checkpointList.put(all, new String[]{number, world, x, y, z});
            }
        }
    }

    public ItemStack getParkourCheckpointItem() {
        ItemStack item = new ItemStack(Material.matchMaterial(getConfig("Event.Item.Checkpoint-teleport.Item").toUpperCase()));
        ItemMeta slabMeta = item.getItemMeta();
        slabMeta.setDisplayName(getConfig("Event.Item.Checkpoint-teleport.Name"));
        item.setItemMeta(slabMeta);
        return item;
    }

    public ItemStack getParkourResetItem() {
        ItemStack item = new ItemStack(Material.matchMaterial(getConfig("Event.Item.Reset.Item").toUpperCase()));
        ItemMeta slabMeta = item.getItemMeta();
        slabMeta.setDisplayName(getConfig("Event.Item.Reset.Name"));
        item.setItemMeta(slabMeta);
        return item;
    }

    public ItemStack getParkourCancelItem() {
        ItemStack item = new ItemStack(Material.matchMaterial(getConfig("Event.Item.Cancel.Item").toUpperCase()));
        ItemMeta slabMeta = item.getItemMeta();
        slabMeta.setDisplayName(getConfig("Event.Item.Cancel.Name"));
        item.setItemMeta(slabMeta);
        return item;
    }
}
