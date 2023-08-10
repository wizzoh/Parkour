package me.wizzo.parkour.files;

import me.wizzo.parkour.Parkour;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigFile {

    private File file;
    private FileConfiguration config;
    private final Parkour main;
    private String errorMessage;

    public ConfigFile(Parkour main) {
        this.main = main;
    }

    public void setup(Parkour main, String fileName) {
        file = new File(main.getDataFolder(), fileName);
        errorMessage = main.messageFormat("&cParkour: Errore con il caricamento del config");

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
        config.set("Prefix", "&6&lParkour &r");
        config.set("NoPerm", "{prefix}&7Non hai il permesso per eseguire questo comando.");
        config.set("Command-not-found", "{prefix}&7Comando sconosciuto. Digita &6/parkour help&r");
        config.set("Block-command-into-parkour", "{prefix}&7Non puoi eseguire comandi finchè sei nel parkour.");
        config.set("Only-player-execute-command", "{prefix}&7Solo i player possono eseguire questo comando.");
        config.set("Plate-material", "stone_pressure_plate");
        config.set("Error", "{prefix}&cErrore durante il processo. Controlla la console per maggiore dettagli.");
        config.set("Help-message-formatted", "&7&m-----&7 Lista comandi - &6&lParkour &7&m-----&7\n" +
                "&6/Parkour &7- Comando principale.\n&6/Parkour help &7- Visualizza questa lista.\n" +
                "&6/Parkour set &7- Aggiungi una pedana.\n&6/Parkour remove &7- Rimuovi una pedana.\n" +
                "&6/Parkour timer <Player> &7- Visualizza il timer proprio o altrui.\n" +
                "&7&m------------&6 By Wizzo &7&m-------------&7"
        );
        config.set("SetPos.No-args-found", "{prefix}&7Utilizza &6/parkour set <start|checkpoint|finish>&7.");
        config.set("SetPos.Success.Start", "{prefix}&7Posizione di &a&lSTART &7settata.");
        config.set("SetPos.Success.Finish", "{prefix}&7Posizione di &c&lFINE &7settata.");
        config.set("SetPos.Success.Checkpoint", "{prefix}&7Posizione &e&lCHECKPOINT {numero} &7settata.");
        config.set("SetPos.Checkpoint-already-exist", "{prefix}&7Checkpoint già esistente.");

        config.set("DelPos.No-args-found", "{prefix}&7Utilizza &6/parkour delete <start|checkpoint|finish>&7.");
        config.set("DelPos.Success.Start-removed", "{prefix}&7Posizione di &a&lSTART &7rimossa.");
        config.set("DelPos.Success.Finish-removed", "{prefix}&7Posizione di &c&lFINE &7rimossa.");
        config.set("DelPos.Success.Checkpoint-removed", "{prefix}&7Posizione &e&lCHECKPOINT {numero} &7rimossa.");
        config.set("DelPos.Success.Checkpoint-all-removed", "{prefix}&7Tutti i checkpoint sono stati rimossi.");
        config.set("DelPos.Checkpoint.Not-found", "{prefix}&7Nessun checkpoint trovato.");
        config.set("DelPos.Checkpoint.No-args-found", "{prefix}&7Utilizza &6/parkour delete checkpoint <all|numero checkpoint>&7.");
        config.set("DelPos.Start-not-exist", "{prefix}&7Posizione di start non trovata.");
        config.set("DelPos.Finish-not-exist", "{prefix}&7Posizione di fine non trovata.");

        config.set("GetTimer.timer-own", "{prefix}&7Ecco il tuo timer:\n{timer}");
        config.set("GetTimer.timer-other", "{prefix}&7Ecco il timer di {player}:\n{timer}");
        config.set("GetTimer.timer-error", "{prefix}&7Nessun timer salvato.");

        config.set("Event.Plate.Start-message", "{prefix}&7Parkour &ainiziato&7.");
        config.set("Event.Plate.Finish-message", "{prefix}&7Parkour &cfinito&7 in {timer}.");
        config.set("Event.Plate.Checkpoint-message", "{prefix}&7Checkpoint numero {numero} &eraggiunto&7 in {timer}.");

        config.set("Event.Item.Checkpoint-teleport.Item", "quartz_slab");
        config.set("Event.Item.Checkpoint-teleport.Name", "&aCheckpoint");
        config.set("Event.Item.Checkpoint-teleport.Slot", 3);

        config.set("Event.Item.Reset.Item", "oak_door");
        config.set("Event.Item.Reset.Name", "&cResetta");
        config.set("Event.Item.Reset.Slot", 4);

        config.set("Event.Item.Cancel.Item", "red_bed");
        config.set("Event.Item.Cancel.Name", "&cCancella");
        config.set("Event.Item.Cancel.Slot", 5);
    }
}
