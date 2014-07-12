package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import com.tenjava.entries.MarianDCrafter.t2.machines.Delay;
import com.tenjava.entries.MarianDCrafter.t2.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class BagCommandExecutor implements CommandExecutor, Listener {

    private Material driveMaterial;
    private Delay delayInBag;
    private int materialPerDelayInBag;
    private Delay delayOutsideBag;
    private int materialPerDelayOutsideBag;

    private TenJava plugin;
    private FileConfiguration bagFile;
    private Map<String, BagInventory> inventories = new HashMap<String, BagInventory>();
    private Map<String, Machine> machines = new HashMap<String, Machine>();

    public BagCommandExecutor(TenJava plugin, FileConfiguration bagFile) {
        this.plugin = plugin;
        this.bagFile = bagFile;

        this.driveMaterial = Material.getMaterial(plugin.getConfig().getString("machines.bag.driveMaterial"));
        this.delayInBag = Delay.valueOf(plugin.getConfig().getString("machines.bag.delayInBag"));
        this.materialPerDelayInBag = plugin.getConfig().getInt("machines.bag.materialPerDelayInBag");
        this.delayOutsideBag = Delay.valueOf(plugin.getConfig().getString("machines.bag.delayOutsideBag"));
        this.materialPerDelayOutsideBag = plugin.getConfig().getInt("machines.bag.materialPerDelayOutsideBag");

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(bagFile.contains(event.getPlayer().getUniqueId().toString())) {
            createBagFromConfig(event.getPlayer());
        }
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    @EventHandler
    public void kick(PlayerKickEvent event) {
        remove(event.getPlayer());
    }

    private void remove(Player player) {
        String name = player.getName();
        if(inventories.containsKey(name)) {
            bagFile.set(player.getUniqueId().toString(), inventories.get(name));
            inventories.remove(name);
        }
        if(machines.containsKey(name)) {
            machines.get(name).cancel();
            machines.remove(name);
        }
    }

    private void removeDeleteInventory(Player player) {
        String name = player.getName();
        if(inventories.containsKey(name)) {
            bagFile.set(player.getUniqueId().toString(), null);
            inventories.remove(name);
        }
        if(machines.containsKey(name)) {
            machines.get(name).cancel();
            machines.remove(name);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("bag"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You must be a player to execute this command!");
            return true;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("machines.bag")) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You don't have permission to execute this command.");
            return true;
        }

        final String name = player.getName();

        if(inventories.containsKey(name)) {
            player.openInventory(inventories.get(name).getInventory());
            machines.get(name).change(materialPerDelayInBag, delayInBag, new Runnable() {
                @Override
                public void run() {
                    if (!player.getInventory().contains(driveMaterial, materialPerDelayInBag)) {
                        player.sendMessage(TenJava.PREFIX_FAIL + "You don't have " + materialPerDelayInBag + " " + driveMaterial + ". Delete Bag...");
                        removeDeleteInventory(player);
                    }
                }
            });
        } else {
            Bukkit.getPluginManager().registerEvents(new BagCreateInventory(player, new Runnable() {
                @Override
                public void run() {
                    if (!player.getInventory().contains(driveMaterial, materialPerDelayInBag)) {
                        player.sendMessage(TenJava.PREFIX_FAIL + "You need at least " + materialPerDelayInBag + " " + driveMaterial + " for this command.");
                    } else {
                        createBag(player);
                    }
                }
            }), plugin);
        }

        return true;
    }

    private void createBag(Player player) {
        start(player, new BagInventory(player));
    }

    private void createBagFromConfig(Player player) {
        BagInventory inventory = new BagInventory((Map<String, Object>) bagFile.get(player.getUniqueId().toString()));
        inventory.serialized(player);
        start(player, inventory);
    }

    private void start(final Player player, BagInventory inventory) {
        inventories.put(player.getName(), inventory);
        Machine machine = new Machine(plugin, player, driveMaterial, materialPerDelayInBag, delayInBag, new Runnable() {
            @Override
            public void run() {
                if (!player.getInventory().contains(driveMaterial, materialPerDelayInBag)) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have " + materialPerDelayInBag + " " + driveMaterial + ". Delete Bag...");
                    removeDeleteInventory(player);
                }
            }
        });
        machines.put(player.getName(), machine);
        machine.start();
    }
}
