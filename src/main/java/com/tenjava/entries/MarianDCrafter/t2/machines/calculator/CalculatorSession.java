package com.tenjava.entries.MarianDCrafter.t2.machines.calculator;

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
/**
 * Represents a calculator session.
 * Must be registered as a listener to work.
 * Creates the inventory, closes it automatically and calculates the result.
 */
public class CalculatorSession implements Listener {

    /**
     * The slots in the inventory.
     */
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

    /**
     * The input StringBuilder.
     */
    private StringBuilder input = new StringBuilder();
    /**
     * The player who started this session.
     */
    private Player player;
    /**
     * The inventory used for the calculator.
     */
    private Inventory inventory;
    /**
     * This prevents the method unregisterListener from unregister the listener.
     * Set to true, if the inventory has changed.
     */
    private boolean closedToUpdateInventory = false;
    /**
     * {@code true}, if the session is running, {@code false} otherwise
     */
    private boolean running = false;

    /**
     * Initializes the session with the given player.
     */
    public CalculatorSession(Player player) {
        this.player = player;
    }

    /**
     * Called when a player closes an inventory.
     * Used to stop the session.
     */
    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (event.getPlayer() == player)
            stop();
    }

    /**
     * Called when the player leaves the server.
     * Used to stop the session.
     */
    @EventHandler
    public void leave(PlayerQuitEvent event) {
        if (event.getPlayer() == player)
            stop();
    }

    /**
     * Called when the player is kicked from the server.
     * Used to stop the session.
     */
    @EventHandler
    public void kick(PlayerKickEvent event) {
        if (event.getPlayer() == player)
            stop();
    }

    /**
     * Called when a player clicks in his inventory.
     * Used to add the terms to the input string and calculate the result.
     */
    @EventHandler
    public void inventoryClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player ||
                event.getSlot() != event.getRawSlot()) // because then the player has clicked into his inventory
            return;

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

    /**
     * Calculates the result.
     * If there is an error, the session is stopped.
     */
    private void calculate() {
        try {
            createInventory("§6" + String.valueOf(new CalculatorStringParser(input.toString()).calculate()));
            input = new StringBuilder();
            closedToUpdateInventory = true; //Only set to true if the don't want to stop the session.
        } catch (CalculatorStringParserException e) {
            stop();
            player.sendMessage(TenJava.PREFIX_FAIL + e.getMessage());
        }
    }

    /**
     * Starts this session and creates the inventory.
     */
    public void start() {
        createInventory("§3§lCalculator");
        running = true;
    }

    /**
     * Stops this session.
     * Closes the inventory and unregister the listener.
     */
    public void stop() {
        if (!closedToUpdateInventory && running) { // && running, to prevent circle stop() -> closeInventory() -> InventoryCloseEvent -> stop() -> ...
            running = false;
            player.closeInventory();
            unregisterListener();
        } else
            closedToUpdateInventory = false;
    }

    /**
     * Appends the given string to the input string.
     * Changes the inventory to a new inventory with a new name.
     *
     * @param append the string to append
     */
    public void append(String append) {
        input.append(append);
        closedToUpdateInventory = true;
        createInventory("§3" + input.toString());
    }

    /**
     * Returns the input string.
     *
     * @return the input string
     */
    public String getInput() {
        return input.toString();
    }

    /**
     * Returns whether the session is currently running.
     *
     * @return {@code true}, if the session is running, {@code false} otherwise
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Unregister the listener.
     * If closedToUpdateInventory is set to true, the listener is not unregistered and set to false.
     */
    private void unregisterListener() {
        if (!closedToUpdateInventory)
            HandlerList.unregisterAll(this);
        else
            closedToUpdateInventory = false;
    }

    /**
     * Creates a new inventory.
     *
     * @param name the name of the inventory
     */
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
