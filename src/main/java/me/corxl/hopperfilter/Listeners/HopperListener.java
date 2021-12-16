package me.corxl.hopperfilter.Listeners;

import me.corxl.hopperfilter.Commands.HopperListenerCommand;
import me.corxl.hopperfilter.HopperFilter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Hopper;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class HopperListener implements Listener {

    @EventHandler
    public void interactEventPlayer(PlayerInteractEvent e) {
        if (e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR)) return;
        if (e.getClickedBlock() == null) return;
        if (!e.getClickedBlock().getType().equals(Material.HOPPER)) return;
        Player p = e.getPlayer();
        if (HopperListenerCommand.getWhitelist().containsKey(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
            Block block = p.getWorld().getBlockAt(e.getClickedBlock().getLocation());
            BlockState blockState = block.getState();
            if (!(blockState instanceof TileState)) return;
            TileState tileState = (TileState) blockState;
            List<String> list = null;
            ItemStack item = p.getInventory().getItemInMainHand();
            if (tileState.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"), PersistentDataType.BYTE_ARRAY)) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX
                        + "&cThis hopper has a blacklist! to add a whitelist clear the blacklist first. &7/hopperfilter clear"));
                HopperListenerCommand.endCountdown(p);
                return;
            }
            if (tileState.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"), PersistentDataType.BYTE_ARRAY)) {
                ByteArrayInputStream bis =
                        new ByteArrayInputStream(tileState.getPersistentDataContainer().get(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"),
                                PersistentDataType.BYTE_ARRAY));
                ObjectInput in = null;
                try {
                    in = new ObjectInputStream(bis);
                } catch (IOException err) {
                    err.printStackTrace();
                }
                try {
                    list = (List<String>) in.readObject();
                } catch (ClassNotFoundException err) {
                    err.printStackTrace();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            } else {
                list = new ArrayList<>();
            }
            if (list.contains(item.getType().name())) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX
                + "&cThat item type is already in the whitelist!"));
                return;
            }
            list.add(item.getType().name());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BukkitObjectOutputStream out;
            try {
                out = new BukkitObjectOutputStream(bos);
                out.writeObject(list);
                out.flush();
            } catch (IOException err) {
                err.printStackTrace();
            }
            byte[] data = bos.toByteArray();
            tileState.getPersistentDataContainer().set(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"), PersistentDataType.BYTE_ARRAY, data);
            tileState.update();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX + "&6" +
                    item.getType().name() + " &2has been added to the whitelist of &bHopper"));
            StringBuilder builder = new StringBuilder("[");
            for (String s: list) {
                if (s.equalsIgnoreCase(list.get(list.size() - 1))){
                    builder.append(s + "]");
                } else{
                    builder.append(s +", ");
                }
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX + "&6Current whitelist: &6" + builder));
            HopperListenerCommand.clickTextMessage(p);
            return;
        }
        if (HopperListenerCommand.getBlacklist().containsKey(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
            Block block = p.getWorld().getBlockAt(e.getClickedBlock().getLocation());
            BlockState blockState = block.getState();
            if (!(blockState instanceof TileState)) return;
            TileState tileState = (TileState) blockState;
            List<String> list = null;
            ItemStack item = p.getInventory().getItemInMainHand();
            if (tileState.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"), PersistentDataType.BYTE_ARRAY)) {
                e.setCancelled(true);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX
                        + "&cThis hopper has a whitelist! to add a blacklist clear the whitelist first. &7/hopperfilter clear"));
                HopperListenerCommand.endCountdown(p);
                return;
            }
            if (tileState.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"), PersistentDataType.BYTE_ARRAY)) {
                ByteArrayInputStream bis =
                        new ByteArrayInputStream(tileState.getPersistentDataContainer().get(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"),
                                PersistentDataType.BYTE_ARRAY));
                ObjectInput in = null;
                try {
                    in = new ObjectInputStream(bis);
                } catch (IOException err) {
                    err.printStackTrace();
                }
                try {
                    list = (List<String>) in.readObject();
                } catch (ClassNotFoundException err) {
                    err.printStackTrace();
                } catch (IOException err) {
                    err.printStackTrace();
                }
            } else {
                list = new ArrayList<>();
            }
            if (list.contains(item.getType().name())) {
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX
                        + "&cThat item type is already in the blacklist!"));
                return;
            }
            list.add(item.getType().name());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            BukkitObjectOutputStream out;
            try {
                out = new BukkitObjectOutputStream(bos);
                out.writeObject(list);
                out.flush();
            } catch (IOException err) {
                err.printStackTrace();
            }
            byte[] data = bos.toByteArray();
            tileState.getPersistentDataContainer().set(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"), PersistentDataType.BYTE_ARRAY, data);
            tileState.update();
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX + "&6" +
                    item.getType().name() + " &2has been added to the blacklist of &bHopper"));
            StringBuilder builder = new StringBuilder("[");
            for (String s: list) {
                if (s.equalsIgnoreCase(list.get(list.size() - 1))){
                    builder.append(s + "]");
                } else{
                    builder.append(s +", ");
                }
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX + "&6Current blacklist: &6" + builder));
            HopperListenerCommand.clickTextMessage(p);
            return;
        }
        if (HopperListenerCommand.getClearHopper().containsKey(e.getPlayer().getUniqueId().toString())) {
            e.setCancelled(true);
            Block block = p.getWorld().getBlockAt(e.getClickedBlock().getLocation());
            BlockState blockState = block.getState();
            if (!(blockState instanceof TileState)) return;
            TileState tileState = (TileState) blockState;
            HopperListenerCommand.getClearHopper().remove(p.getUniqueId().toString());
            if (tileState.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"), PersistentDataType.BYTE_ARRAY)) {
                tileState.getPersistentDataContainer().remove(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"));
                tileState.update();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX + "&cThe whitelist of &bHopper &c has been cleared!"));
                return;
            }
            if (tileState.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"), PersistentDataType.BYTE_ARRAY)) {
                tileState.getPersistentDataContainer().remove(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"));
                tileState.update();
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX + "&cThe blacklist of &bHopper &c has been cleared!"));
                return;
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', HopperFilter.PLUGINPREFIX + "&cThis Hoppper does not have a whitelist/blacklist!"));
        }

    }

    @EventHandler
    public void onHopperInput(InventoryPickupItemEvent event) throws IOException, ClassNotFoundException {
        if (!event.getInventory().getType().equals(InventoryType.HOPPER)) return;
        Hopper hopper = (Hopper) event.getInventory().getLocation().getWorld().getBlockAt(event.getInventory().getLocation()).getState();
        if (hopper.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"), PersistentDataType.BYTE_ARRAY)) {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(hopper.getPersistentDataContainer().get(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"),
                            PersistentDataType.BYTE_ARRAY));
            ObjectInput in = new ObjectInputStream(bis);
            List<String> mats = (List<String>) in.readObject();
            if (!mats.contains(event.getItem().getItemStack().getType().name())) event.setCancelled(true);
        } else if (hopper.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"), PersistentDataType.BYTE_ARRAY)) {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(hopper.getPersistentDataContainer().get(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"),
                            PersistentDataType.BYTE_ARRAY));
            ObjectInput in = new ObjectInputStream(bis);
            List<String> mats = (List<String>) in.readObject();
            if (mats.contains(event.getItem().getItemStack().getType().name())) event.setCancelled(true);
        }

    }

    @EventHandler
    public void onHopperMove(InventoryMoveItemEvent event) throws IOException, ClassNotFoundException {
        if (!event.getDestination().getType().equals(InventoryType.HOPPER)) return;
        Hopper hopper = (Hopper) event.getDestination().getLocation().getWorld().getBlockAt(event.getDestination().getLocation()).getState();
        if (hopper.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"), PersistentDataType.BYTE_ARRAY)) {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(hopper.getPersistentDataContainer().get(new NamespacedKey(HopperFilter.getPlugin(), "whitelist"),
                            PersistentDataType.BYTE_ARRAY));
            ObjectInput in = new ObjectInputStream(bis);
            List<String> mats = (List<String>) in.readObject();
            if (!mats.contains(event.getItem().getType().name())) event.setCancelled(true);
        } else if (hopper.getPersistentDataContainer().has(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"), PersistentDataType.BYTE_ARRAY)) {
            ByteArrayInputStream bis =
                    new ByteArrayInputStream(hopper.getPersistentDataContainer().get(new NamespacedKey(HopperFilter.getPlugin(), "blacklist"),
                            PersistentDataType.BYTE_ARRAY));
            ObjectInput in = new ObjectInputStream(bis);
            List<String> mats = (List<String>) in.readObject();
            if (mats.contains(event.getItem().getType().name())) event.setCancelled(true);
        }

    }

}
