package me.sirimperivm.spigot.modules.commands;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.Config;
import me.sirimperivm.spigot.assets.managers.Db;
import me.sirimperivm.spigot.assets.managers.Modules;
import me.sirimperivm.spigot.assets.utils.Colors;
import me.sirimperivm.spigot.assets.utils.Errors;
import org.bukkit.Bukkit;
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
                                Player target = Bukkit.getPlayerExact(playerName);
                                data.getPlayers().updateUserData(playerName, plugin.getDefaultTrophy());
                                if (target != null) {
                                    target.sendMessage(Config.getTransl("settings", "messages.info.trophy.reset"));
                                }
                                s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-reset")
                                        .replace("${player}", playerName));
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
                                    int actual = data.getPlayers().getUserTrophys(playerName);
                                    int toGive = trophyInsert + actual;
                                    Player target = Bukkit.getPlayerExact(playerName);
                                    data.getPlayers().updateUserData(playerName, toGive);
                                    if (target != null) {
                                        target.sendMessage(Config.getTransl("settings", "messages.info.trophy.received")
                                                .replace("${given}", a[2])
                                                .replace("${actual}", String.valueOf(toGive)));
                                    }
                                    s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-given")
                                            .replace("${player}", playerName)
                                            .replace("${given}", String.valueOf(trophyInsert))
                                    );
                                } else {
                                    data.getPlayers().insertUserData(playerName, plugin.getDefaultTrophy());
                                    int toGive = trophyInsert;
                                    Player target = Bukkit.getPlayerExact(playerName);
                                    data.getPlayers().updateUserData(playerName, toGive);
                                    if (target != null) {
                                        target.sendMessage(Config.getTransl("settings", "messages.info.trophy.received")
                                                .replace("${given}", a[2])
                                                .replace("${actual}", String.valueOf(toGive)));
                                    }
                                    s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-given")
                                            .replace("${player}", playerName)
                                            .replace("${given}", String.valueOf(trophyInsert))
                                    );
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
                                    int actual = data.getPlayers().getUserTrophys(playerName);
                                    if (actual >= trophyTaken) {
                                        int toTake = actual-trophyTaken;
                                        Player target = Bukkit.getPlayerExact(playerName);
                                        data.getPlayers().updateUserData(playerName, toTake);
                                        if (target != null) {
                                            target.sendMessage(Config.getTransl("settings", "messages.info.trophy.taken")
                                                    .replace("${taken}", String.valueOf(trophyTaken))
                                                    .replace("${actual}", String.valueOf(toTake))
                                            );
                                        }
                                        s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-taken")
                                                .replace("${taken}", String.valueOf(trophyTaken))
                                                .replace("${player}", playerName)
                                        );
                                    } else {
                                        s.sendMessage(Config.getTransl("settings", "messages.errors.target.trophy.not-enough"));
                                    }
                                } else {
                                    data.getPlayers().insertUserData(playerName, plugin.getDefaultTrophy());
                                    int actual = data.getPlayers().getUserTrophys(playerName);
                                    if (actual >= trophyTaken) {
                                        int toTake = actual-trophyTaken;
                                        Player target = Bukkit.getPlayerExact(playerName);
                                        data.getPlayers().updateUserData(playerName, toTake);
                                        if (target != null) {
                                            target.sendMessage(Config.getTransl("settings", "messages.info.trophy.taken")
                                                    .replace("${taken}", String.valueOf(trophyTaken))
                                                    .replace("${actual}", String.valueOf(toTake))
                                            );
                                        }
                                        s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-taken")
                                                .replace("${taken}", String.valueOf(trophyTaken))
                                                .replace("${player}", playerName)
                                        );
                                    } else {
                                        s.sendMessage(Config.getTransl("settings", "messages.errors.target.trophy.not-enough"));
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

                                if (data.getPlayers().checkUserData(playerName))
                                    data.getPlayers().updateUserData(playerName, trophySet);
                                data.getPlayers().insertUserData(playerName, trophySet);

                                Player target = Bukkit.getPlayerExact(playerName);
                                if (target != null)
                                    target.sendMessage(Config.getTransl("settings", "messages.info.trophy.set")
                                            .replace("${set}", String.valueOf(trophySet)));

                                s.sendMessage(Config.getTransl("settings", "messages.success.target.trophy-set")
                                        .replace("${set}", String.valueOf(trophySet))
                                        .replace("${player}", playerName));
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
