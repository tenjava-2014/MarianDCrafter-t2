package com.tenjava.entries.MarianDCrafter.t2.machines.calculator;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import com.tenjava.entries.MarianDCrafter.t2.machines.Delay;
import com.tenjava.entries.MarianDCrafter.t2.machines.Machine;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class CalculatorCommandExecutor implements CommandExecutor {

    private TenJava plugin;
    private Material driveMaterial;
    private Delay delay;
    private int materialPerDelay;
    private Map<String, CalculatorSession> sessions = new HashMap<String, CalculatorSession>();
    private Map<String, Machine> machines = new HashMap<String, Machine>();

    public CalculatorCommandExecutor(TenJava plugin) {
        this.plugin = plugin;
        driveMaterial = Material.getMaterial(plugin.getConfig().getString("machines.calculator.driveMaterial"));
        delay = Delay.valueOf(plugin.getConfig().getString("machines.calculator.delay"));
        materialPerDelay = plugin.getConfig().getInt("machines.calculator.materialPerDelay");
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

        if (!player.getInventory().contains(driveMaterial, materialPerDelay)) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You need at least " + materialPerDelay + " " + driveMaterial + " for this command.");
            return true;
        }

        final String name = player.getName();

        CalculatorSession session = new CalculatorSession(player);
        sessions.put(name, session);
        Bukkit.getPluginManager().registerEvents(session, plugin);
        session.start();

        Machine machine = new Machine(plugin, player, driveMaterial, materialPerDelay, delay, new Runnable() {
            @Override
            public void run() {
                if (!sessions.get(name).isRunning())
                    remove(name);

                if (!player.getInventory().contains(driveMaterial, materialPerDelay)) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have " + materialPerDelay + " " + driveMaterial + ".");
                    remove(name);
                }
            }
        });

        machines.put(name, machine);
        machine.start();

        return true;
    }

    private void remove(String name) {
        sessions.get(name).stop();
        sessions.remove(name);
        machines.get(name).cancel();
        machines.remove(name);
    }
}
