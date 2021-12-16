package me.corxl.hopperfilter.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HopperTabComplete implements TabCompleter {
    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("hopperfilter")) {
            if (args.length==1) {
                List<String> list = new ArrayList<>();
                if (sender.hasPermission("hopperfilter.whitelist"))
                    list.add("whitelist");
                if (sender.hasPermission("hopperfilter.blacklist"))
                    list.add("blacklist");
                if (sender.hasPermission("hopperfilter.clear"))
                    list.add("clear");
                return list;
            }
        }
        return null;
    }
}
