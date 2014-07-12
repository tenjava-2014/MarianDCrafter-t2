package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class BagCommandExecutor implements CommandExecutor, Listener {

    private TenJava plugin;
    private FileConfiguration bagFile;
    private Map<String, BagInventory> inventories = new HashMap<String, BagInventory>();

    public BagCommandExecutor(TenJava plugin, FileConfiguration bagFile) {
        this.plugin = plugin;
        this.bagFile = bagFile;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(bagFile.contains(event.getPlayer().getUniqueId().toString()))
            inventories.put(event.getPlayer().getName(), new BagInventory((Map<String, Object>) bagFile.get(event.getPlayer().getUniqueId().toString())));
        //TODO startTask
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {

    }

    @EventHandler
    public void kick(PlayerKickEvent event) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
