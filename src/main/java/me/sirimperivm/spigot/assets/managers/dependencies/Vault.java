package me.sirimperivm.spigot.assets.managers.dependencies;

import me.sirimperivm.spigot.Main;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

@SuppressWarnings("all")
public class Vault {

    private static Main plugin = Main.getPlugin();
    private static Economy econ = null;

    public Vault() {
        if (setupEconomy()) {
            plugin.log("&aRegistrazione a vault confermata!");
        }
    }

    private boolean setupEconomy() {
        if (plugin.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEcon() {
        return econ;
    }
}
