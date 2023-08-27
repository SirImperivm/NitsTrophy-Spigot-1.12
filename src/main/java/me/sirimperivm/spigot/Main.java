package me.sirimperivm.spigot;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sirimperivm.spigot.assets.managers.Config;
import me.sirimperivm.spigot.assets.managers.Db;
import me.sirimperivm.spigot.assets.managers.Modules;
import me.sirimperivm.spigot.assets.managers.dependencies.PapiExpansions;
import me.sirimperivm.spigot.assets.managers.dependencies.Vault;
import me.sirimperivm.spigot.assets.utils.Colors;
import me.sirimperivm.spigot.modules.commands.AdminCommand;
import me.sirimperivm.spigot.modules.commands.TrophiesCommand;
import me.sirimperivm.spigot.modules.listeners.JoinListener;
import me.sirimperivm.spigot.modules.tabCompleters.AdminCommandTabCompleter;
import me.sirimperivm.spigot.modules.tabCompleters.TrophiesTabCompleter;
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
    private static Vault vault;
    private static PlaceholderExpansion papi;

    private boolean canConnect = false;
    private long defaultTrophy;

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
        defaultTrophy = conf.getSettings().getLong("settings.trophys.defaultValue");
        canConnect = true;
        data = new Db();
        data.setup();
        mods = data.getMods();
        vault = new Vault();
        papi = new PapiExpansions(plugin);
        if (getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PapiExpansions(plugin).register();
            log("&aRegistrazione a placeholderapi confermata.");
        }
    }

    void close() {
        data.disconnect();
    }

    @Override
    public void onEnable() {
        setup();

        getServer().getPluginCommand("tadmin").setExecutor(new AdminCommand());
        getServer().getPluginCommand("trophies").setExecutor(new TrophiesCommand());

        getServer().getPluginCommand("tadmin").setTabCompleter(new AdminCommandTabCompleter());
        getServer().getPluginCommand("trophies").setTabCompleter(new TrophiesTabCompleter());

        getServer().getPluginManager().registerEvents(new JoinListener(), this);

        log("&aPlugin attivato correttamente!");
    }

    @Override
    public void onDisable() {
        close();
        log("&aPlugin disattivato correttamente!");
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

    public long getDefaultTrophy() {
        return defaultTrophy;
    }

    public static Db getData() {
        return data;
    }

    public static Modules getMods() {
        return mods;
    }

    public static Vault getVault() {
        return vault;
    }

    public static PlaceholderExpansion getPapi() {
        return papi;
    }
}
