package me.sirimperivm.spigot.modules.tabCompleters;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.Config;
import me.sirimperivm.spigot.assets.managers.Db;
import me.sirimperivm.spigot.assets.managers.Modules;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("all")
public class TrophiesTabCompleter implements TabCompleter {

    private static Main plugin = Main.getPlugin();
    private static Config conf = Main.getConf();
    private static Db data = Main.getData();
    private static Modules mods = data.getMods();

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {

        if (c.getName().equalsIgnoreCase("trophies")) {
            if (s.hasPermission(conf.getSettings().getString("permissions.commands.trophies.main"))) {
                if (a.length == 1) {
                    List<String> toReturn = new ArrayList<String>();
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        toReturn.add(players.getName());
                    }
                    return toReturn;
                }
            }
        }
        return null;
    }
}
