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
public class AdminCommand implements CommandExecutor {

    private static Main plugin = Main.getPlugin();
    private static Config conf = Main.getConf();
    private static Db data = Main.getData();
    private static Modules mods = data.getMods();

    private void getUsage(CommandSender s) {
        for (String usage : conf.getHelps().getStringList("helps.admin-commands.trophy-admin")) {
            s.sendMessage(Colors.text(usage));
        }
    }

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {

        if (c.getName().equalsIgnoreCase("tadmin")) {
            if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.tadmin.main"))) {
                return true;
            } else {
                if (a.length == 0) {
                    getUsage(s);
                } else if (a.length == 1) {
                    if (a[0].equalsIgnoreCase("reload")) {
                        if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.tadmin.reload"))) {
                            return true;
                        } else {
                            conf.loadAll();
                            s.sendMessage(Config.getTransl("settings", "messages.success.plugin.reloaded"));
                        }
                    } else {
                        getUsage(s);
                    }
                } else if (a.length == 2) {
                    if (a[0].equalsIgnoreCase("reset")) {
                        if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.tadmin.reset"))) {
                            return true;
                        } else {
                            String playerName = a[1];
                            if (data.getPlayers().checkUserData(playerName)) {
                                Player target = Bukkit.getPlayerExact(playerName);
                                data.getPlayers().updateUserData(playerName, plugin.getDefaultTrophy());
                                if (target != null) {
                                    target.sendMessage(Config.getTransl("settings", "messages.info.trophy.reset"));
                                }
                                s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-reset")
                                        .replace("${player}", playerName));
                            } else {
                                OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
                                if (target.hasPlayedBefore()) {
                                    data.getPlayers().insertUserData(playerName, plugin.getDefaultTrophy());
                                    if (target.isOnline()) {
                                        Player t = (Player) target;
                                        t.sendMessage(Config.getTransl("settings", "messages.info.trophy.reset"));
                                    }
                                    s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-reset")
                                            .replace("${player}", playerName));
                                } else {
                                    s.sendMessage(Config.getTransl("settings", "messages.errors.user-not-found"));
                                }
                            }
                        }
                    } else {
                        getUsage(s);
                    }
                } else if (a.length == 3) {
                    if (a[0].equalsIgnoreCase("give")) {
                        if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.tadmin.give"))) {
                            return true;
                        } else {
                            String valueInsert = a[2];
                            boolean containChars = false;
                            for (char ch : valueInsert.toCharArray()) {
                                if (!(ch >= '0' && ch <= '9')) {
                                    containChars = true;
                                    break;
                                }
                            }

                            if (!containChars) {
                                int trophyInsert = Integer.parseInt(a[2]);
                                String playerName = a[1];

                                if (data.getPlayers().checkUserData(playerName)) {
                                    long actual = data.getPlayers().getUserTrophys(playerName);
                                    long toGive = trophyInsert + actual;
                                    Player target = Bukkit.getPlayerExact(playerName);
                                    data.getPlayers().updateUserData(playerName, toGive);
                                    if (target != null) {
                                        target.sendMessage(Config.getTransl("settings", "messages.info.trophy.received")
                                                .replace("${given}", a[2])
                                                .replace("${actual}", Strings.formatNumber(toGive)));
                                    }
                                    s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-given")
                                            .replace("${player}", playerName)
                                            .replace("${given}", Strings.formatNumber(trophyInsert))
                                    );
                                } else {
                                    OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
                                    if (target.hasPlayedBefore()) {
                                        data.getPlayers().insertUserData(playerName, plugin.getDefaultTrophy());
                                        int toGive = trophyInsert;
                                        data.getPlayers().updateUserData(playerName, toGive);
                                        if (target.isOnline()) {
                                            Player t = (Player) target;
                                            t.sendMessage(Config.getTransl("settings", "messages.info.trophy.received")
                                                    .replace("${given}", a[2])
                                                    .replace("${actual}", Strings.formatNumber(toGive)));
                                        }
                                        s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-given")
                                                .replace("${player}", playerName)
                                                .replace("${given}", Strings.formatNumber(trophyInsert))
                                        );
                                    } else {
                                        s.sendMessage(Config.getTransl("settings", "messages.errors.user-not-found"));
                                    }
                                }
                            } else {
                                s.sendMessage(Config.getTransl("settings", "messages.errors.chars-not-allowed"));
                            }
                        }
                    } else if (a[0].equalsIgnoreCase("take")) {
                        if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.tadmin.take"))) {
                            return true;
                        } else {
                            String valueInsert = a[2];
                            boolean containChars = false;
                            for (char ch : valueInsert.toCharArray()) {
                                if (!(ch >= '0' && ch <= '9')) {
                                    containChars = true;
                                    break;
                                }
                            }

                            if (!containChars) {
                                int trophyTaken = Integer.parseInt(a[2]);
                                String playerName = a[1];

                                if (data.getPlayers().checkUserData(playerName)) {
                                    long actual = data.getPlayers().getUserTrophys(playerName);
                                    if (actual >= trophyTaken) {
                                        long toTake = actual-trophyTaken;
                                        Player target = Bukkit.getPlayerExact(playerName);
                                        data.getPlayers().updateUserData(playerName, toTake);
                                        if (target != null) {
                                            target.sendMessage(Config.getTransl("settings", "messages.info.trophy.taken")
                                                    .replace("${taken}", Strings.formatNumber(trophyTaken))
                                                    .replace("${actual}", Strings.formatNumber(toTake))
                                            );
                                        }
                                        s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-taken")
                                                .replace("${taken}", Strings.formatNumber(trophyTaken))
                                                .replace("${player}", playerName)
                                        );
                                    } else {
                                        s.sendMessage(Config.getTransl("settings", "messages.errors.target.trophy.not-enough"));
                                    }
                                } else {
                                    OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
                                    if (target.hasPlayedBefore()) {
                                        data.getPlayers().insertUserData(playerName, plugin.getDefaultTrophy());
                                        long actual = data.getPlayers().getUserTrophys(playerName);
                                        if (actual >= trophyTaken) {
                                            long toTake = actual - trophyTaken;
                                            data.getPlayers().updateUserData(playerName, toTake);
                                            if (target.isOnline()) {
                                                Player t = (Player) target;
                                                t.sendMessage(Config.getTransl("settings", "messages.info.trophy.taken")
                                                        .replace("${taken}", Strings.formatNumber(trophyTaken))
                                                        .replace("${actual}", Strings.formatNumber(toTake))
                                                );
                                            }
                                            s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-taken")
                                                    .replace("${taken}", Strings.formatNumber(trophyTaken))
                                                    .replace("${player}", playerName)
                                            );
                                        } else {
                                            s.sendMessage(Config.getTransl("settings", "messages.errors.target.trophy.not-enough"));
                                        }
                                    } else {
                                        s.sendMessage(Config.getTransl("settings", "messages.errors.user-not-found"));
                                    }
                                }
                            } else {
                                s.sendMessage(Config.getTransl("settings", "messages.errors.chars-not-allowed"));
                            }
                        }
                    } else if (a[0].equalsIgnoreCase("set")) {
                        if (Errors.noPerm(s, conf.getSettings().getString("permissions.commands.tadmin.set"))) {
                            return true;
                        } else {
                            String valueInsert = a[2];
                            boolean containChars = false;
                            for (char ch : valueInsert.toCharArray()) {
                                if (!(ch >= '0' && ch <= '9')) {
                                    containChars = true;
                                    break;
                                }
                            }

                            if (!containChars) {
                                int trophySet = Integer.parseInt(a[2]);
                                String playerName = a[1];

                                if (data.getPlayers().checkUserData(playerName)) {
                                    data.getPlayers().updateUserData(playerName, trophySet);
                                    Player target = Bukkit.getPlayerExact(playerName);
                                    if (target != null)
                                        target.sendMessage(Config.getTransl("settings", "messages.info.trophy.set")
                                                .replace("${set}", Strings.formatNumber(trophySet)));

                                    s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-set")
                                            .replace("${set}", Strings.formatNumber(trophySet))
                                            .replace("${player}", playerName));
                                } else {
                                    OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
                                    if (target.hasPlayedBefore()) {
                                        data.getPlayers().insertUserData(playerName, trophySet);
                                        if (target.isOnline()) {
                                            Player t = (Player) target;
                                            t.sendMessage(Config.getTransl("settings", "messages.info.trophy.set")
                                                    .replace("${set}", Strings.formatNumber(trophySet)));
                                        }
                                        s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-set")
                                                .replace("${set}", Strings.formatNumber(trophySet))
                                                .replace("${player}", playerName));
                                    } else {
                                        s.sendMessage(Config.getTransl("settings", "messages.errors.user-not-found"));
                                    }
                                }
                            } else {
                                s.sendMessage(Config.getTransl("settings", "messages.errors.chars-not-allowed"));
                            }
                        }
                    } else {
                        getUsage(s);
                    }
                } else {
                    getUsage(s);
                }
            }
        }
        return false;
    }
}
