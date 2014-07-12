package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import com.tenjava.entries.MarianDCrafter.t2.machines.*;
import com.tenjava.entries.MarianDCrafter.t2.util.UUIDUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.event.player.*;

import java.io.*;
import java.util.*;

/**
 * Executes the bag commands and manages the bags.
 */
@SuppressWarnings("unused")
public class BagCommandExecutor implements CommandExecutor, Listener {

    private Material driveMaterial;
    private Delay delayInBag;
    private int materialPerDelayInBag;
    private Delay delayOutsideBag;
    private int materialPerDelayOutsideBag;

    private TenJava plugin;
    private FileConfiguration bagFileConf;
    private File bagFile;
    /**
     * The BagInventory's of the players. The player is the String.
     */
    private Map<String, BagInventory> inventories = new HashMap<String, BagInventory>();
    /**
     * The machines of the players. The player is the String.
     */
    private Map<String, Machine> machines = new HashMap<String, Machine>();
    /**
     * All players who have the bag open are stored in this Set.
     */
    private Set<String> playersInBag = new HashSet<String>();

    public BagCommandExecutor(TenJava plugin, FileConfiguration bagFileConf, File bagFile) {
        this.plugin = plugin;
        this.bagFileConf = bagFileConf;
        this.bagFile = bagFile;

        this.driveMaterial = Material.getMaterial(plugin.getConfig().getString("machines.bag.driveMaterial"));
        this.delayInBag = Delay.valueOf(plugin.getConfig().getString("machines.bag.delayInBag"));
        this.materialPerDelayInBag = plugin.getConfig().getInt("machines.bag.materialPerDelayInBag");
        this.delayOutsideBag = Delay.valueOf(plugin.getConfig().getString("machines.bag.delayOutsideBag"));
        this.materialPerDelayOutsideBag = plugin.getConfig().getInt("machines.bag.materialPerDelayOutsideBag");

        for (Player player : Bukkit.getOnlinePlayers())
            if (bagFileConf.contains(UUIDUtils.simpleString(player.getUniqueId())))
                createBagFromConfig(player);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    /**
     * Called when a player joins the game.
     * Used to load the bag from the bags file.
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (bagFileConf.contains(UUIDUtils.simpleString(event.getPlayer().getUniqueId())))
            createBagFromConfig(event.getPlayer());
    }

    /**
     * Called when a player quits the game.
     * Used to remove and save the bag.
     */
    @EventHandler
    public void quit(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    /**
     * Called when a player is kicked from the server.
     * Used to remove and save the bag.
     */
    @EventHandler
    public void kick(PlayerKickEvent event) {
        remove(event.getPlayer());
    }

    /**
     * Called when a player closes the inventory.
     * Used to change the machine.
     */
    @EventHandler
    public void inventoryClosed(final InventoryCloseEvent event) {
        if (playersInBag.contains(event.getPlayer().getName())) {
            final Player player = (Player) event.getPlayer();
            changeMachineToOutsideBag(player);
        }
    }

    /**
     * Called when a player clicks in his inventory.
     * Used to change the machine or open the BagDestroyInventory view.
     */
    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (playersInBag.contains(event.getWhoClicked().getName()) &&
                event.getSlot() == event.getRawSlot() &&
                event.getSlot() == BagInventory.SLOT_DESTROY) {
            final Player player = (Player) event.getWhoClicked();
            changeMachineToOutsideBag(player);

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    Bukkit.getPluginManager().registerEvents(new BagDestroyInventory(player, new Runnable() {
                        @Override
                        public void run() {
                            removeDeleteInventory(player);
                        }
                    }), plugin);
                }
            }, 1L);
            event.setCancelled(true);
            /* We need to run this 1 tick later, because otherwise the InventoryClickEvent in ConfirmDialogInventory
               won't execute. Seems like a bug in Bukkit.
               Well, Bukkit is named BUGit, so there must be bugs! :)
            */
        }
    }

    /**
     * Saves and removes all bags.
     */
    public void saveBags() {
        for (Player player : Bukkit.getOnlinePlayers())
            remove(player);

    }

    /**
     * Removes the bag of a player and saves it to the bag file.
     * Also cancels and removes the machine.
     * @param player the player of who to remove the bag
     */
    private void remove(Player player) {
        String name = player.getName();
        if (inventories.containsKey(name)) {
            ConfigurationSection section = bagFileConf.createSection(UUIDUtils.simpleString(player.getUniqueId()));
            inventories.get(name).save(section);
            try {
                bagFileConf.save(bagFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            inventories.remove(name);
        }
        if (machines.containsKey(name)) {
            machines.get(name).cancel();
            machines.remove(name);
        }
        playersInBag.remove(name);
    }

    /**
     * Removes the bag of a player and deletes the bag.
     * Also cancels and removes the machine.
     * @param player the player of who to remove the bag
     */
    private void removeDeleteInventory(Player player) {
        String name = player.getName();
        if (inventories.containsKey(name)) {
            bagFileConf.set(player.getUniqueId().toString(), null);
            try {
                bagFileConf.save(bagFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
            inventories.remove(name);
        }
        if (machines.containsKey(name)) {
            machines.get(name).cancel();
            machines.remove(name);
        }
        playersInBag.remove(name);
        player.closeInventory();
    }

    /**
     * Called when the bag command is executed.
     * Used to open the bag or to show the BagCreateInventory view.
     */
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

        if (inventories.containsKey(name)) {
            player.openInventory(inventories.get(name).getInventory());
            changeMachineToInBag(player);
            playersInBag.add(name);
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

    /**
     * Creates a new bag for the player.
     * @param player the player for who to create a new bag
     */
    private void createBag(Player player) {
        start(player, new BagInventory(player));
    }

    /**
     * Loads a bag from the bags file.
     * @param player the player for who to create a new bag
     */
    private void createBagFromConfig(Player player) {
        start(player, new BagInventory(bagFileConf.getConfigurationSection(UUIDUtils.simpleString(player.getUniqueId())), player));
    }

    /**
     * Starts a new machine and puts the inventory and machine into the maps.
     * @param player the player
     * @param inventory the inventory
     */
    private void start(final Player player, BagInventory inventory) {
        inventories.put(player.getName(), inventory);
        Machine machine = startMachine(player);
        machines.put(player.getName(), machine);
        machine.start();
    }

    /**
     * Starts a new closed machine.
     * @param player the player of the machine
     * @return the new machine
     */
    private Machine startMachine(final Player player) {
        return new Machine(plugin, player, driveMaterial, materialPerDelayOutsideBag, delayOutsideBag, new Runnable() {
            @Override
            public void run() {
                if (!player.getInventory().contains(driveMaterial, materialPerDelayOutsideBag)) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have " + materialPerDelayInBag + " " + driveMaterial + ". Delete Bag...");
                    removeDeleteInventory(player);
                }
            }
        });
    }

    /**
     * Changes the machine delay and materialPerDelay to the values 'inBag' (usually a faster delay)
     * @param player the player of the machine
     */
    private void changeMachineToInBag(final Player player) {
        machines.get(player.getName()).change(materialPerDelayInBag, delayInBag, new Runnable() {
            @Override
            public void run() {
                if (!player.getInventory().contains(driveMaterial, materialPerDelayInBag)) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have " + materialPerDelayInBag + " " + driveMaterial + ". Delete Bag...");
                    removeDeleteInventory(player);
                }
            }
        });
    }

    /**
     * Changes the machine delay and materialPerDelay to the values 'outsideBag' (usually a slower delay)
     * @param player the player of the machine
     */
    private void changeMachineToOutsideBag(final Player player) {
        machines.get(player.getName()).change(materialPerDelayOutsideBag, delayOutsideBag, new Runnable() {
            @Override
            public void run() {
                if (!player.getInventory().contains(driveMaterial, materialPerDelayOutsideBag)) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have " + materialPerDelayOutsideBag + " " + driveMaterial + ". Delete Bag...");
                    removeDeleteInventory(player);
                }
            }
        });
        playersInBag.remove(player.getName());
    }
}
