package me.sirimperivm.spigot.assets.managers;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.utils.Colors;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;

@SuppressWarnings("all")
public class Config {

    private static Main plugin = Main.getPlugin();
    private File folder = plugin.getDataFolder();
    private File settingsFile, helpsFile;
    private FileConfiguration settings, helps;

    public Config() {
        settingsFile = new File(folder, "settings.yml");
        settings = new YamlConfiguration();
        helpsFile = new File(folder, "helps.yml");
        helps = new YamlConfiguration();

        if (!folder.exists()) {
            folder.mkdir();
        }

        if (!settingsFile.exists()) {
            create(settings, settingsFile);
        }

        if (!helpsFile.exists()) {
            create(helps, helpsFile);
        }
    }

    private void create(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            Files.copy(plugin.getResource(n), f.toPath(), new CopyOption[0]);
            load(c, f);
        } catch (IOException e) {
            plugin.log("&cImpossibile creare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void save(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            c.save(f);
        } catch (IOException e) {
            plugin.log("&cImpossibile salvare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void load(FileConfiguration c, File f) {
        String n = f.getName();
        try {
            c.load(f);
        } catch (IOException | InvalidConfigurationException e) {
            plugin.log("&cImpossibile caricare il file " + n + "!");
            e.printStackTrace();
        }
    }

    public void saveAll() {
        save(settings, settingsFile);
        save(helps, helpsFile);
    }

    public void loadAll(){
        load(settings, settingsFile);
        load(helps, helpsFile);
    }

    public File getSettingsFile() {
        return settingsFile;
    }

    public File getHelpsFile() {
        return helpsFile;
    }

    public FileConfiguration getSettings() {
        return settings;
    }

    public FileConfiguration getHelps() {
        return helps;
    }

    public static String getTransl(String type, String key) {
        switch (type) {
            case "helps":
                return Colors.text(Main.getConf().getHelps().getString(key)
                        .replace("%sp", Main.getSuccessPrefix())
                        .replace("%ip", Main.getInfoPrefix())
                        .replace("%fp", Main.getFailPrefix()));
            default:
                return Colors.text(Main.getConf().getSettings().getString(key)
                        .replace("%sp", Main.getSuccessPrefix())
                        .replace("%ip", Main.getInfoPrefix())
                        .replace("%fp", Main.getFailPrefix()));
        }
    }
}
