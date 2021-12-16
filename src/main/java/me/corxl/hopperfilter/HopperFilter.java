package me.corxl.hopperfilter;

import me.corxl.hopperfilter.Commands.HopperListenerCommand;
import me.corxl.hopperfilter.Commands.HopperTabComplete;
import me.corxl.hopperfilter.Listeners.HopperListener;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public final class HopperFilter extends JavaPlugin {

    private static HopperFilter plugin;
    public static final String PLUGINPREFIX = ChatColor.translateAlternateColorCodes('&', "&l&2[&r&5HopperFilter&r&l&2]&r ");

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        this.getCommand("hopperfilter").setExecutor(new HopperListenerCommand());
        this.getCommand("hopperfilter").setTabCompleter(new HopperTabComplete());
        this.getServer().getPluginManager().registerEvents(new HopperListener(), this);
    }

    public static HopperFilter getPlugin() {
        return plugin;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
