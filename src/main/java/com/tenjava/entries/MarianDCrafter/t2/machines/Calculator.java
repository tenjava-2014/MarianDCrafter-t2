package com.tenjava.entries.MarianDCrafter.t2.machines;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class Calculator implements CommandExecutor {

    private TenJava plugin;
    private Map<String, CalculatorSession> sessions = new HashMap<String, CalculatorSession>();

    public Calculator(TenJava plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("calculator"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You must be a player to execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("machines.calculator")) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You don't have permission to execute this command.");
            return true;
        }

        CalculatorSession session = new CalculatorSession(player);
        sessions.put(player.getName(), session);
        Bukkit.getPluginManager().registerEvents(session, plugin);
        session.start();

        return true;
    }
}
