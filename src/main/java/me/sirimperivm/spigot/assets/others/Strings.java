package me.sirimperivm.spigot.assets.others;

import me.sirimperivm.spigot.Main;
import me.sirimperivm.spigot.assets.managers.Config;

import java.util.List;

@SuppressWarnings("all")
public class Strings {

    private static Main plugin = Main.getPlugin();
    private static Config conf = Main.getConf();

    public static String formatNumber(long number) {
        String toReturn = String.valueOf(number);
        if (!conf.getSettings().getBoolean("settings.numberFormatting.enabled")) return toReturn;
        String valueCurrency = "";
        int formatCount = conf.getSettings().getInt("settings.numberFormatting.formatCount");
        List<String> refs = conf.getSettings().getStringList("settings.numberFormatting.formatTypes");
        StringBuilder sb = new StringBuilder(formatCount);

        for (String ref : refs) {
            String[] splitter = ref.split("-");
            long refValue = Long.parseLong(splitter[0]);
            if (number >= refValue) {
                toReturn = String.valueOf(number/refValue);
                valueCurrency = splitter[1];
            }
        }

        if (toReturn.length() >= formatCount) {
            for (int i=0; i<formatCount; i++) {
                if (toReturn.charAt(i) == '.') {
                    break;
                }
                sb.append(toReturn.charAt(i));
            }

            toReturn = sb.toString();
        }

        if (toReturn.contains(".0")) {
            toReturn = toReturn.replace(".0", "");
        }

        toReturn = toReturn+valueCurrency;

        return toReturn;
    }
}
