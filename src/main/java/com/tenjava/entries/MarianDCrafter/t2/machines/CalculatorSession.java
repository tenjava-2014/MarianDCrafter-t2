package com.tenjava.entries.MarianDCrafter.t2.machines;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import com.tenjava.entries.MarianDCrafter.t2.util.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("unused")
public class CalculatorSession implements Listener {

    private final static int
            SLOT_ADDITION = 2,
            SLOT_SUBTRACTION = 3,
            SLOT_MULTIPLICATION = 5,
            SLOT_DIVISION = 6,
            SLOT_1 = 21,
            SLOT_2 = 22,
            SLOT_3 = 23,
            SLOT_4 = 30,
            SLOT_5 = 31,
            SLOT_6 = 32,
            SLOT_7 = 39,
            SLOT_8 = 40,
            SLOT_9 = 41,
            SLOT_0 = 48,
            SLOT_POINT = 49,
            SLOT_CALCULATE = 50;

    private StringBuilder input = new StringBuilder();
    private Player player;
    private Inventory inventory;
    private boolean closedToUpdateInventory = false;

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

    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player ||
                event.getSlot() != event.getRawSlot()) // because then the player has clicked into his inventory
            return;
        if (event.getInventory() != inventory)
            stop();
        switch (event.getSlot()) {
            case SLOT_ADDITION:
                append("+");
                break;
            case SLOT_SUBTRACTION:
                append("-");
                break;
            case SLOT_MULTIPLICATION:
                append("*");
                break;
            case SLOT_DIVISION:
                append("/");
                break;
            case SLOT_1:
                append("1");
                break;
            case SLOT_2:
                append("2");
                break;
            case SLOT_3:
                append("3");
                break;
            case SLOT_4:
                append("4");
                break;
            case SLOT_5:
                append("5");
                break;
            case SLOT_6:
                append("6");
                break;
            case SLOT_7:
                append("7");
                break;
            case SLOT_8:
                append("8");
                break;
            case SLOT_9:
                append("9");
                break;
            case SLOT_0:
                append("0");
                break;
            case SLOT_POINT:
                append(".");
                break;
            case SLOT_CALCULATE:
                calculate();
                break;
        }
        event.setCancelled(true);
    }

    private void calculate() {
        closedToUpdateInventory = true;
        try {
            createInventory("§6" + String.valueOf(new CalculatorStringParser(input.toString()).calculate()));
        } catch (CalculatorStringParserException e) {
            stop();
            player.sendMessage(TenJava.PREFIX_FAIL + e.getMessage());
        }
        input = new StringBuilder();
    }

    public void start() {
        createInventory("§3§lCalculator");
    }

    public void stop() {
        if (player.getOpenInventory() == inventory)
            player.closeInventory();
    }

    public void append(String append) {
        input.append(append);
        closedToUpdateInventory = true;
        createInventory("§3" + input.toString());
    }

    public String getInput() {
        return input.toString();
    }

    private void unregisterListener() {
        if (!closedToUpdateInventory)
            HandlerList.unregisterAll(this);
        else
            closedToUpdateInventory = false;
    }

    private void createInventory(String name) {
        inventory = Bukkit.createInventory(player, 54, name);

        inventory.setItem(SLOT_ADDITION, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 1, "§a+", "Addition"));
        inventory.setItem(SLOT_SUBTRACTION, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 2, "§a-", "Subtraction"));
        inventory.setItem(SLOT_MULTIPLICATION, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 3, "§a\u00D7", "Multiplication"));
        inventory.setItem(SLOT_DIVISION, ItemStackUtils.itemStack(Material.STAINED_CLAY, 1, (short) 4, "§a\u00F7", "Division"));

        inventory.setItem(SLOT_1, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 5, "§d1"));
        inventory.setItem(SLOT_2, ItemStackUtils.itemStack(Material.WOOL, 2, (short) 6, "§d2"));
        inventory.setItem(SLOT_3, ItemStackUtils.itemStack(Material.WOOL, 3, (short) 7, "§d3"));
        inventory.setItem(SLOT_4, ItemStackUtils.itemStack(Material.WOOL, 4, (short) 8, "§d4"));
        inventory.setItem(SLOT_5, ItemStackUtils.itemStack(Material.WOOL, 5, (short) 9, "§d5"));
        inventory.setItem(SLOT_6, ItemStackUtils.itemStack(Material.WOOL, 6, (short) 10, "§d6"));
        inventory.setItem(SLOT_7, ItemStackUtils.itemStack(Material.WOOL, 7, (short) 11, "§d7"));
        inventory.setItem(SLOT_8, ItemStackUtils.itemStack(Material.WOOL, 8, (short) 12, "§d8"));
        inventory.setItem(SLOT_9, ItemStackUtils.itemStack(Material.WOOL, 9, (short) 13, "§d9"));
        inventory.setItem(SLOT_0, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 14, "§d0"));

        inventory.setItem(SLOT_POINT, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 15, "§c,", "Point"));
        inventory.setItem(SLOT_CALCULATE, ItemStackUtils.itemStack(Material.BOOK, 1, (short) 0, "§c=", "Calculate"));

        player.openInventory(inventory);
    }

}
