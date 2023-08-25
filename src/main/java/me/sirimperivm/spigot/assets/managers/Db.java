package me.sirimperivm.spigot.assets.managers;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.databases.Players;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("all")
public class Db {

    private static Main plugin = Main.getPlugin();
    private static Config conf = Main.getConf();
    private static Modules mods;

    private static Players players;
    public static Connection conn;

    public static String dbName = conf.getSettings().getString("settings.database.name");
    public static String tablePrefix = conf.getSettings().getString("settings.database.tablePrefix");

    private void connect() {
        String host = conf.getSettings().getString("settings.database.host");
        int port = conf.getSettings().getInt("settings.database.port");
        String user = conf.getSettings().getString("settings.database.user");
        String password = conf.getSettings().getString("settings.database.password");
        String options = conf.getSettings().getString("settings.database.options");

        if (plugin.getCanConnect()) {
            String url = "jdbc:mysql://" + host + ":" + port + "/" + dbName + options;

            try {
                conn = DriverManager.getConnection(url, user, password);
                plugin.log("&aPlugin connesso al database!");
            } catch (SQLException e) {
                plugin.log("&cImpossibile connettersi al database; il plugin verrà disattivato.");
                e.printStackTrace();
                plugin.setCanConnect(false);
                plugin.disablePlugin();
            }
        } else {
            plugin.log("&cImpossibile connettersi al database; il plugin verrà disattivato.");
            plugin.disablePlugin();
        }
    }

    public void disconnect() {
        if (plugin.getCanConnect()) {
            try {
                if (conn != null && !conn.isClosed()) {
                    conn.close();
                    plugin.log("&aPlugin disconnesso dal database.");
                }
            } catch (SQLException e) {
                plugin.log("&cImpossibile disconnettersi dal database.");
                e.printStackTrace();
            }
        }
    }

    public void setup() {
        connect();
        mods = new Modules();
        players = new Players();
        players.createTable();
    }

    public static Modules getMods() {
        return mods;
    }

    public static Players getPlayers() {
        return players;
    }
}
