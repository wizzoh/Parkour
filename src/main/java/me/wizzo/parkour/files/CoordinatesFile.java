package me.wizzo.parkour.files;

import me.wizzo.parkour.Parkour;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class CoordinatesFile {

    private File file;
    private FileConfiguration config;
    private final Parkour main;
    private String errorMessage;

    public CoordinatesFile(Parkour main) {
        this.main = main;
    }

    public void setup(Parkour main, String fileName) {
        file = new File(main.getDataFolder(), fileName);
        errorMessage = main.messageFormat("&cParkour: Errore con il caricamento del config (coordinate)");

        if (!file.exists()) {
            try {
                boolean success = file.createNewFile();
                if (success) {
                    load();
                    defaultConfig();
                    save();
                } else {
                    main.getConsole().sendMessage(errorMessage);
                }
            } catch (IOException e) {
                main.getConsole().sendMessage(errorMessage);
            }
        } else {
            load();
            save();
        }
    }

    public void load() {
        try {
            config = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            main.getConsole().sendMessage(errorMessage);
        }
    }

    public FileConfiguration get() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            main.getConsole().sendMessage(errorMessage);
        }
    }

    private void defaultConfig() {
        config.set("dont-edit-this", 0);
    }
}
