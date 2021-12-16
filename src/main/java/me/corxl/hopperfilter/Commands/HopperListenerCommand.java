package me.corxl.hopperfilter.Commands;

import me.corxl.hopperfilter.HopperFilter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class HopperListenerCommand implements CommandExecutor {

    private static Map<String, Long> whitelist = new HashMap<>();
    private static Map<String, Long> clearHopper = new HashMap<>();
    private static Map<String, Long> blacklist = new HashMap<>();

    public static Map<String, Long> getWhitelist() {
        return whitelist;
    }

    public static Map<String, Long> getClearHopper() {
        return clearHopper;
    }

    public static Map<String, Long> getBlacklist() {
        return blacklist;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if (command.getName().equalsIgnoreCase("hopperfilter")) {
            if (!(commandSender instanceof Player)) return false;
            Player p = (Player) commandSender;
            if (!p.hasPermission("hopperfilter.use")) {
                p.sendMessage(ChatColor.RED + "You do not have permission to use [HopperFilter]");
                return true;
            }
            if (!(strings.length==1)) {
                p.sendMessage(ChatColor.RED + "Invalid use of /hopperfilter. -->"+ ChatColor.GRAY + " /hopperfilter [whitelist | clear]");
                return true;
            }
            if (strings[0].equalsIgnoreCase("whitelist")) {
                if (!p.hasPermission("hopperfilter.whitelist")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to use [HopperFilter - Whitelist]");
                    return true;
                }
                if (getClearHopper().containsKey(p.getUniqueId().toString())) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            HopperFilter.PLUGINPREFIX +
                                    "&cYou cannot add an item to a Hopper's whitelist while you are attempting to clear a Hopper's whitelist!"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            HopperFilter.PLUGINPREFIX +
                                    "&cPlease wait: &6" + (int) ((20000 - (System.currentTimeMillis() - getClearHopper().get(p.getUniqueId().toString())))) / 1000) + ChatColor.RED + " seconds");
                    return true;
                }
                getWhitelist().put(p.getUniqueId().toString(), System.currentTimeMillis());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        HopperFilter.PLUGINPREFIX +
                        "&3You have &520s &3to add an item to a hopper's whitelist."));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        HopperFilter.PLUGINPREFIX +
                                "&3Right click with the item in your main hand to add it to the hopper's whitelist!"));
                new Thread(()->{
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getWhitelist().containsKey(p.getUniqueId().toString())) {
                        endCountdown(p);
                    }
                }).start();
                clickTextMessage(p);
            }
            else if (strings[0].equalsIgnoreCase("clear")) {
                if (!p.hasPermission("hopperfilter.clear")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to use [HopperFilter - Clear]");
                    return true;
                }
                if (getWhitelist().containsKey(p.getUniqueId().toString())) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            HopperFilter.PLUGINPREFIX +
                                    "&cYou cannot clear a hopper while you are attempting to add an item to its whitelist!"));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            HopperFilter.PLUGINPREFIX +
                                    "&cPlease wait: &6" + (int) ((20000 - (System.currentTimeMillis() - getWhitelist().get(p.getUniqueId().toString())))) / 1000) + ChatColor.RED + " seconds");
                    return true;
                }
                getClearHopper().put(p.getUniqueId().toString(), System.currentTimeMillis());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        HopperFilter.PLUGINPREFIX +
                                "&3You have &520s &3to choose a Hopper to clear."));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        HopperFilter.PLUGINPREFIX +
                                "&3Right click a Hopper to clear its whitelists!"));
                new Thread(()->{
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getClearHopper().containsKey(p.getUniqueId().toString())) {
                        endCountdown(p);
                    }
                }).start();
                clickTextMessage(p);
            }
            else if (strings[0].equalsIgnoreCase("blacklist")) {
                if (!p.hasPermission("hopperfilter.blacklist")) {
                    p.sendMessage(ChatColor.RED + "You do not have permission to use [HopperFilter - Blacklist]");
                    return true;
                }
                if (getWhitelist().containsKey(p.getUniqueId().toString()) || getClearHopper().containsKey(p.getUniqueId().toString())) {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            HopperFilter.PLUGINPREFIX +
                                    "&cYou cannot blacklist a hopper while you are attempting to " + (getWhitelist().containsKey(p.getUniqueId().toString()) ? "whitelist an item" : "clear an item")));
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                            HopperFilter.PLUGINPREFIX +
                                    "&cPlease wait: &6" + (int) ((20000 - (System.currentTimeMillis() - getWhitelist().get(p.getUniqueId().toString())))) / 1000) + ChatColor.RED + " seconds");
                    return true;
                }
                blacklist.put(p.getUniqueId().toString(), System.currentTimeMillis());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        HopperFilter.PLUGINPREFIX +
                                "&3You have &520s &3to choose a Hopper to blacklist an item to."));
                p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                        HopperFilter.PLUGINPREFIX +
                                "&3Right click a Hopper to add it to the blacklist!"));
                new Thread(()->{
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (getBlacklist().containsKey(p.getUniqueId().toString())) {
                        endCountdown(p);
                    }
                }).start();
                clickTextMessage(p);
            }
            if (strings[0].equalsIgnoreCase("cancel")){
                endCountdown(p);
            }
        }
        return false;
    }

    public static void clickTextMessage(Player p) {
        TextComponent message = new TextComponent(ChatColor.translateAlternateColorCodes('&',
                HopperFilter.PLUGINPREFIX +
                        "&c&l[CANCEL]"));
        message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/hopperfilter cancel"));
        message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&',
                HopperFilter.PLUGINPREFIX +
                        "&4Cancel action"))));
        p.sendMessage(message);
    }

    public static void endCountdown(Player p) {
        if (!(getBlacklist().containsKey(p.getUniqueId().toString()) || getClearHopper().containsKey(p.getUniqueId().toString()) || getWhitelist().containsKey(p.getUniqueId().toString()))) return;
        getBlacklist().remove(p.getUniqueId().toString());
        getClearHopper().remove(p.getUniqueId().toString());
        getWhitelist().remove(p.getUniqueId().toString());
        p.sendMessage(ChatColor.translateAlternateColorCodes('&',
                HopperFilter.PLUGINPREFIX + "&3Prompt cancelled. To enable &c-> &7/hopperfilter [whitelist | blacklist | clear]"));
    }
}
