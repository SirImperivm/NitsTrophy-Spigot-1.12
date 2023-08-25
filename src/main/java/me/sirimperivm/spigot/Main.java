package me.sirimperivm.spigot;

import me.sirimperivm.spigot.assets.managers.Config;
import me.sirimperivm.spigot.assets.managers.Db;
import me.sirimperivm.spigot.assets.managers.Modules;
import me.sirimperivm.spigot.assets.utils.Colors;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getConsoleSender;
import static org.bukkit.Bukkit.getPluginManager;

@SuppressWarnings("all")
public final class Main extends JavaPlugin {

    private static Main plugin;
    private static Config conf;

    private static String successPrefix;
    private static String infoPrefix;
    private static String failPrefix;

    private static Db data;
    private static Modules mods;

    private boolean canConnect = false;

    public void setCanConnect(boolean canConnect) {
        this.canConnect = canConnect;
    }

    public void log(String message) {
        getConsoleSender().sendMessage(Colors.text("&9NitsTrophy: " + message));
    }

    void setup() {
        plugin = this;
        conf = new Config();
        conf.loadAll();
        successPrefix = Colors.text(conf.getSettings().getString("messages.prefixes.success"));
        infoPrefix = Colors.text(conf.getSettings().getString("messages.prefixes.info"));
        failPrefix = Colors.text(conf.getSettings().getString("messages.prefixes.fail"));
        canConnect = true;
        data = new Db();
        data.setup();
        mods = data.getMods();
    }

    void close() {
        data.disconnect();
    }

    @Override
    public void onEnable() {
        setup();
    }

    @Override
    public void onDisable() {
        close();
    }

    public void disablePlugin() {
        getPluginManager().disablePlugin(this);
    }

    public static Main getPlugin() {
        return plugin;
    }

    public static Config getConf() {
        return conf;
    }

    public static String getSuccessPrefix() {
        return successPrefix;
    }

    public static String getInfoPrefix() {
        return infoPrefix;
    }

    public static String getFailPrefix() {
        return failPrefix;
    }

    public boolean getCanConnect() {
        return canConnect;
    }

    public static Db getData() {
        return data;
    }

    public static Modules getMods() {
        return mods;
    }
}
