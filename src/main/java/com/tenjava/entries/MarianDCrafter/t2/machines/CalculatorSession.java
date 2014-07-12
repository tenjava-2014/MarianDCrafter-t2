package com.tenjava.entries.MarianDCrafter.t2.machines;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class CalculatorSession implements Listener {

    private StringBuilder input = new StringBuilder();
    private Player player;
    private Inventory inventory;

    public CalculatorSession(Player player) {
        this.player = player;
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if(event.getPlayer() == player) {
            stop();
            unregisterListener();
        }
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        unregisterListener();
    }

    @EventHandler
    public void kick(PlayerKickEvent event) {
        unregisterListener();
    }

    private void unregisterListener() {
        HandlerList.unregisterAll(this);
    }

    public void start() {
        inventory = Bukkit.createInventory(player, 54, "§3§lCalculator");



        player.openInventory(inventory);
    }

    public void stop() {
        if(player.getOpenInventory() == inventory)
            player.closeInventory();
    }

    public void append(String append) {
        input.append(append);
    }

    public String getInput() {
        return input.toString();
    }

}
