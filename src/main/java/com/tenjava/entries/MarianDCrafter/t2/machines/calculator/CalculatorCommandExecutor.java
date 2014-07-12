package com.tenjava.entries.MarianDCrafter.t2.machines.calculator;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;

public class CalculatorCommandExecutor implements CommandExecutor {

    private TenJava plugin;
    private Material driveMaterial;
    private int materialPerSecond;
    private Map<String, CalculatorSession> sessions = new HashMap<String, CalculatorSession>();
    private Map<String, BukkitTask> tasks = new HashMap<String, BukkitTask>();

    public CalculatorCommandExecutor(TenJava plugin) {
        this.plugin = plugin;
        driveMaterial = Material.getMaterial(plugin.getConfig().getString("machines.calculator.driveMaterial"));
        materialPerSecond = plugin.getConfig().getInt("machines.calculator.materialPerSecond");
    }

    /**
     * Executes the command 'calculator'.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("calculator"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You must be a player to execute this command!");
            return true;
        }

        final Player player = (Player) sender;

        if (!player.hasPermission("machines.calculator")) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You don't have permission to execute this command.");
            return true;
        }

        if (!player.getInventory().contains(driveMaterial, materialPerSecond)) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You need at least " + materialPerSecond + " " + driveMaterial + " for this command.");
            return true;
        }

        final String name = player.getName();

        CalculatorSession session = new CalculatorSession(player);
        sessions.put(name, session);
        Bukkit.getPluginManager().registerEvents(session, plugin);
        session.start();

        tasks.put(name, Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
            @Override
            public void run() {
                if (!sessions.get(name).isRunning())
                    remove(name);

                if (!player.getInventory().contains(driveMaterial, materialPerSecond)) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have " + materialPerSecond + " " + driveMaterial + ".");
                    remove(name);
                }

                PlayerInventory inventory = player.getInventory();
                ItemStack[] contents = inventory.getContents();
                int remove = materialPerSecond;
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
        }, 20L, 20L));

        return true;
    }

    private void remove(String name) {
        sessions.get(name).stop();
        sessions.remove(name);
        tasks.get(name).cancel();
        tasks.remove(name);
    }
}
