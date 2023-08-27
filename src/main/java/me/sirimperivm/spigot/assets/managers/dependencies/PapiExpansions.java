package me.sirimperivm.spigot.assets.managers.dependencies;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.Config;
import me.sirimperivm.spigot.assets.managers.Db;
import me.sirimperivm.spigot.assets.managers.Modules;
import me.sirimperivm.spigot.assets.others.Strings;
import org.bukkit.OfflinePlayer;

@SuppressWarnings("all")
public class PapiExpansions extends PlaceholderExpansion {

    private final Main plugin;
    public PapiExpansions(Main plugin) {
        this.plugin = plugin;
    }

    private static Config conf = Main.getConf();
    private static Db data = Main.getData();
    private static Modules mods = data.getMods();

    @Override
    public String getIdentifier() {
        return "nt";
    }

    @Override
    public String getAuthor() {
        return "SirImperivm_";
    }

    @Override
    public String getVersion() {
        return "1.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String param) {
        String toReturn = "";

        if (param.equalsIgnoreCase(conf.getSettings().getString("placeholders.user.trophyCount"))) {
            toReturn = String.valueOf(data.getPlayers().getUserTrophys(player.getName()));
        }

        if (param.equalsIgnoreCase(conf.getSettings().getString("placeholders.user.trophyCountFormatted"))) {
            toReturn = Strings.formatNumber(data.getPlayers().getUserTrophys(player.getName()));
        }

        return toReturn;
    }
}
