package me.sirimperivm.spigot.assets.utils;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.Config;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class Errors {

    private static Config conf = Main.getConf();

    public static boolean noPerm(CommandSender s, String node) {
        if (s.hasPermission(node))
            return false;
        s.sendMessage(Config.getTransl("messages.errors.no-perm"));
        return true;
    }

    public static boolean noConsole(CommandSender s) {
        if (s instanceof Player)
            return false;
        s.sendMessage(Config.getTransl("messages.errors.no-console"));
        return true;
    }
}
