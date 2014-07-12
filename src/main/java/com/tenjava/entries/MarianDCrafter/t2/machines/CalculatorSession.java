package com.tenjava.entries.MarianDCrafter.t2.machines;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

        ItemStack plus = new ItemStack(Material.STAINED_CLAY, 1, (short) 1);
        ItemMeta plusMeta = plus.getItemMeta();
        plusMeta.setDisplayName("§a§l+");
        plus.setItemMeta(plusMeta);

        ItemStack minus = new ItemStack(Material.STAINED_CLAY, 1, (short) 2);
        ItemMeta minusMeta = minus.getItemMeta();
        minusMeta.setDisplayName("§a§l-");
        minus.setItemMeta(minusMeta);

        ItemStack multiplication = new ItemStack(Material.STAINED_CLAY, 1, (short) 3);
        ItemMeta multiplicationMeta = multiplication.getItemMeta();
        multiplicationMeta.setDisplayName("§a§l\u00D7");
        multiplication.setItemMeta(multiplicationMeta);

        ItemStack divide = new ItemStack(Material.STAINED_CLAY, 1, (short) 4);
        ItemMeta divideMeta = divide.getItemMeta();
        divideMeta.setDisplayName("§a§l\u00F7");
        divide.setItemMeta(divideMeta);

        inventory.setItem(2, plus);
        inventory.setItem(3, minus);
        inventory.setItem(5, multiplication);
        inventory.setItem(6, divide);

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
