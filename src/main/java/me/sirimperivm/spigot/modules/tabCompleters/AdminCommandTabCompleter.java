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
public class AdminCommandTabCompleter implements TabCompleter {

    private static Main plugin = Main.getPlugin();
    private static Config conf = Main.getConf();
    private static Db data = Main.getData();
    private static Modules mods = data.getMods();

    @Override
    public List<String> onTabComplete(CommandSender s, Command c, String l, String[] a) {

        if (c.getName().equalsIgnoreCase("tadmin")) {
            if (s.hasPermission(conf.getSettings().getString("permissions.commands.tadmin.main"))) {
                if (a.length == 1) {
                    List<String> toReturn = new ArrayList<String>();
                    toReturn.add("give");
                    toReturn.add("reload");
                    toReturn.add("reset");
                    toReturn.add("set");
                    toReturn.add("take");
                    return toReturn;
                } else if (a.length == 2) {
                    List<String> toReturn = new ArrayList<String>();
                    if (a[0].equalsIgnoreCase("give") || a[0].equalsIgnoreCase("reset") || a[0].equalsIgnoreCase("set") || a[0].equalsIgnoreCase("take")) {
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            toReturn.add(players.getName());
                        }
                    }
                    return toReturn;
                } else if (a.length == 3) {
                    List<String> toReturn = new ArrayList<String>();
                    if (a[0].equalsIgnoreCase("give") || a[0].equalsIgnoreCase("set") || a[0].equalsIgnoreCase("take")) {
                        for (int i=0; i<1000; i++) {
                            toReturn.add(String.valueOf(i));
                        }
                    }
                    return toReturn;
                }
            }
        }
        return null;
    }
}
