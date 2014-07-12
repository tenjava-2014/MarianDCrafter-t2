package com.tenjava.entries.MarianDCrafter.t2.machines;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 *  Represents a machine, which takes a material from the inventory of the player.
 */
public class Machine {

    private Plugin plugin;
    private Player player;
    private Material driveMaterial;
    private int materialPerDelay;
    private Delay delay;
    private Runnable runnable;
    private BukkitTask task;

    /**
     * Initializes a machine without an extra Runnable.
     * @param plugin the plugin
     * @param player the player from who to take the material
     * @param driveMaterial the material to take
     * @param materialPerDelay the amount of material which to take every delay
     * @param delay the delay
     */
    public Machine(Plugin plugin, Player player, Material driveMaterial, int materialPerDelay, Delay delay) {
        this(plugin, player, driveMaterial, materialPerDelay, delay, null);
    }

    /**
     * Initializes a machine with an extra Runnable, which will be executed every delay.
     * @param plugin the plugin
     * @param player the player from who to take the material
     * @param driveMaterial the material to take
     * @param materialPerDelay the amount of material which to take every delay
     * @param delay the delay
     * @param runnable the runnable
     */
    public Machine(Plugin plugin, Player player, Material driveMaterial, int materialPerDelay, Delay delay, Runnable runnable) {
        this.plugin = plugin;
        this.player = player;
        this.driveMaterial = driveMaterial;
        this.materialPerDelay = materialPerDelay;
        this.delay = delay;
        this.runnable = runnable;
    }

    /**
     * Starts the timer.
     */
    public void start() {
        task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if(!player.isOnline()) {
                    cancel();
                    return;
                }

                runnable.run();

                PlayerInventory inventory = player.getInventory();
                ItemStack[] contents = inventory.getContents();
                int remove = materialPerDelay;
                for (int i = 0; i < contents.length; i++) {
                    if (contents[i] != null && contents[i].getType() == driveMaterial) { // only execute if it's the driveMaterial
                        int amount = contents[i].getAmount() - remove;
                        if (amount > 0) { // in the ItemStack is enough material to remove
                            contents[i].setAmount(contents[i].getAmount() - remove);
                            break;
                        } else if (amount == 0) { // if the amount is 0, we need to remove the item instead of set amount to 0
                            inventory.setItem(i, null);
                            break;
                        } else {
                            contents[i].setAmount(0);
                            remove = Math.abs(amount);
                        }
                    }
                }
            }
        }, delay.getTicks(), delay.getTicks());
    }

    /**
     * Cancels the timer.
     */
    public void cancel() {
        task.cancel();
        task = null;
    }

    public void change(int materialPerDelay, Delay delay, Runnable runnable) {
        this.materialPerDelay = materialPerDelay;
        this.delay = delay;
        this.runnable = runnable;

        if(task != null) {
            task.cancel();
            start();
        }
    }

}
