package me.sirimperivm.spigot.modules.commands;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.Config;
import me.sirimperivm.spigot.assets.managers.Db;
import me.sirimperivm.spigot.assets.managers.Modules;
import me.sirimperivm.spigot.assets.others.Strings;
import me.sirimperivm.spigot.assets.utils.Colors;
import me.sirimperivm.spigot.assets.utils.Errors;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class TrophiesCommand implements CommandExecutor {

    private static Main plugin = Main.getPlugin();
    private static Config conf = Main.getConf();
    private static Db data = Main.getData();
    private static Modules mods = data.getMods();

    private void getUsage(CommandSender s) {
        for (String usage : conf.getHelps().getStringList("helps.user-commands.trophies")) {
            s.sendMessage(Colors.text(usage));
        }
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {

        if (c.getName().equalsIgnoreCase("trophies")) {
            if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.trophies.main"))) {
                return true;
            } else {
                if (a.length == 0) {
                    if (Errors.noConsole(s)) {
                        return true;
                    } else {
                        if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.trophies.show.yourself"))) {
                            return true;
                        } else {
                            Player p = (Player) s;
                            String playerName = p.getName();

                            if (data.getPlayers().checkUserData(playerName)) {
                                int trophyCount = data.getPlayers().getUserTrophys(playerName);
                                p.sendMessage(Config.getTransl("settings", "messages.others.your-trophies")
                                        .replace("${trophyCount}", Strings.formatNumber(trophyCount)));
                            } else {
                                data.getPlayers().insertUserData(playerName, plugin.getDefaultTrophy());
                                int trophyCount = data.getPlayers().getUserTrophys(playerName);
                                p.sendMessage(Config.getTransl("settings", "messages.others.your-trophies")
                                        .replace("${trophyCount}", Strings.formatNumber(trophyCount)));
                            }
                        }
                    }
                } else if (a.length == 1) {
                    if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.trophies.show.others"))) {
                        return true;
                    } else {
                        String playerName = a[0];

                        if (data.getPlayers().checkUserData(playerName)) {
                            int trophyCount = data.getPlayers().getUserTrophys(playerName);
                            s.sendMessage(Config.getTransl("settings", "messages.others.other-trophies")
                                    .replace("${trophyCount}", Strings.formatNumber(trophyCount))
                                    .replace("${player}", playerName));
                        } else {
                            OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
                            if (target.hasPlayedBefore()) {
                                data.getPlayers().insertUserData(playerName, plugin.getDefaultTrophy());
                                int trophyCount = data.getPlayers().getUserTrophys(playerName);
                                s.sendMessage(Config.getTransl("settings", "messages.others.other-trophies")
                                        .replace("${trophyCount}", Strings.formatNumber(trophyCount))
                                        .replace("${player}", playerName));
                            } else {
                                s.sendMessage(Config.getTransl("settings", "messages.errors.user-not-found"));
                            }
                        }
                    }
                } else {
                    getUsage(s);
                }
            }
        }
        return false;
    }
}
