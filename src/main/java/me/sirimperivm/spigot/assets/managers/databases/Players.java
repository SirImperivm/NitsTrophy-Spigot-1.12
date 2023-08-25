package me.sirimperivm.spigot.assets.managers.databases;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.Config;
import me.sirimperivm.spigot.assets.managers.Db;
import me.sirimperivm.spigot.assets.managers.Modules;

import java.sql.*;

@SuppressWarnings("all")
public class Players {

    private static Main plugin = Main.getPlugin();
    private static Config conf = Main.getConf();
    private static Db data = Main.getData();
    private static Modules mods = data.getMods();

    static Connection conn = data.conn;
    String dbName = data.dbName;
    String tablePrefix = data.tablePrefix;
    String tableName = "players";
    String database = dbName + "." + tablePrefix + tableName;

    boolean tableExists() {
        boolean value = false;
        try {
            DatabaseMetaData dmd = conn.getMetaData();
            ResultSet rs = dmd.getTables(null, null, database, new String[]{"TABLE"});
            value = rs.next();
        } catch (SQLException e) {
            plugin.log("&cImpossibile ottenere l'esistenza del database players.");
            e.printStackTrace();
        }
        return value;
    }

    public void createTable() {
        if (!tableExists()) {
            String query = "CREATE TABLE " + database + "(`id` INT AUTO_INCREMENT primary key NOT NULL, `playerName` TEXT NOT NULL, `trophys` INT NOT NULL);";

            try {
                PreparedStatement state = conn.prepareStatement(query);
                state.executeUpdate();
                plugin.log("&aTabella players creata con successo.");
            } catch (SQLException e) {
                plugin.log("&cImpossibile creare la tabella players.");
                e.printStackTrace();
                data.disconnect();
                plugin.setCanConnect(false);
                plugin.disablePlugin();
            }
        }
    }

    public void insertUserData(String playerName, int trophys) {
        String query = "INSERT INTO " + database + "(playerName, trophys) VALUES (?, ?)";

        try {
            PreparedStatement state = conn.prepareStatement(query);
            state.setString(1, playerName);
            state.setInt(2, trophys);
            state.executeUpdate();
        } catch (SQLException e) {
            plugin.log("&cImpossibile aggiungere un dato al database players: " +
                    "\n Username: " + playerName +
                    "\n Trofei: " + trophys + "" +
                    "\n...!");
            e.printStackTrace();
        }
    }

    public int getUserTrophys(String playerName) {
        int value = 0;
        String query = "SELECT * FROM " + database;

        try {
            PreparedStatement state = conn.prepareStatement(query);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                if (rs.getString("playerName").equalsIgnoreCase(playerName)) {
                    value = rs.getInt("trophys");
                    break;
                }
            }
        } catch (SQLException e) {
            plugin.log("&cImpossibile ricevere la quantità di trofei relativi all'utente " + playerName + " nel database players.");
            e.printStackTrace();
        }
        return value;
    }

    public boolean checkUserData(String playerName) {
        boolean value = false;
        String query = "SELECT * FROM " + database;

        try {
            PreparedStatement state = conn.prepareStatement(query);
            ResultSet rs = state.executeQuery();
            while (rs.next()) {
                if (rs.getString("playerName").equalsIgnoreCase(playerName)) {
                    value = true;
                    break;
                }
            }
        } catch (SQLException e) {
            plugin.log("&cImpossibile trovare un dato relativo all'utente " + playerName + " nel database players.");
            e.printStackTrace();
        }
        return value;
    }
}
