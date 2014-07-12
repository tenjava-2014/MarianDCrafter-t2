package com.tenjava.entries.MarianDCrafter.t2.machines;

import com.tenjava.entries.MarianDCrafter.t2.util.ItemStackUtils;
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

@SuppressWarnings("unused")
public class CalculatorSession implements Listener {

    private StringBuilder input = new StringBuilder();
    private Player player;
    private Inventory inventory;

    public CalculatorSession(Player player) {
        this.player = player;
    }

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (event.getPlayer() == player) {
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

        inventory.setItem(2, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 1, "§a+", "Addition"));
        inventory.setItem(3, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 2, "§a-", "Subtraction"));
        inventory.setItem(5, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 3, "§a\u00D7", "Multiplication"));
        inventory.setItem(6, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 4, "§a\u00F7", "Division"));

        inventory.setItem(21, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 5, "§d1"));
        inventory.setItem(22, ItemStackUtils.itemStack(Material.WOOL, 2, (short) 6, "§d2"));
        inventory.setItem(23, ItemStackUtils.itemStack(Material.WOOL, 3, (short) 7, "§d3"));
        inventory.setItem(30, ItemStackUtils.itemStack(Material.WOOL, 4, (short) 8, "§d4"));
        inventory.setItem(31, ItemStackUtils.itemStack(Material.WOOL, 5, (short) 9, "§d5"));
        inventory.setItem(32, ItemStackUtils.itemStack(Material.WOOL, 6, (short) 10, "§d6"));
        inventory.setItem(39, ItemStackUtils.itemStack(Material.WOOL, 7, (short) 11, "§d7"));
        inventory.setItem(40, ItemStackUtils.itemStack(Material.WOOL, 8, (short) 12, "§d8"));
        inventory.setItem(41, ItemStackUtils.itemStack(Material.WOOL, 9, (short) 13, "§d9"));
        inventory.setItem(48, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 14, "§d0"));

        inventory.setItem(50, ItemStackUtils.itemStack(Material.BOOK, 1, (short) 15, "§c="));

        player.openInventory(inventory);
    }

    public void stop() {
        if (player.getOpenInventory() == inventory)
            player.closeInventory();
    }

    public void append(String append) {
        input.append(append);
    }

    public String getInput() {
        return input.toString();
    }

}
