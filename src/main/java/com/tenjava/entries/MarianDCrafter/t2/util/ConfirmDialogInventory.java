package com.tenjava.entries.MarianDCrafter.t2.util;

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

/**
 * Represents an inventory where the user can click on YES or NO.
 * If he clicks on YES, the given Runnable will be executed.
 */
public abstract class ConfirmDialogInventory implements Listener {

    private final static int
            SLOT_YES = 11,
            SLOT_NO = 15;

    private Runnable onYes;
    private Inventory inventory;
    private Player player;

    /**
     * Initializes the ConfirmDialogInventory with the player, the 'onYes' Runnable and the name of the inventory.
     * The name of the inventory is usually a question like 'Destroy Bag?'
     * @param player the player
     * @param onYes the runnable which will be executed if the player clicks on 'YES'
     * @param name the name or question of this view
     */
    public ConfirmDialogInventory(Player player, Runnable onYes, String name) {
        this.onYes = onYes;
        this.player = player;

        inventory = Bukkit.createInventory(player, 27, name);
        inventory.setItem(SLOT_YES, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 5, "§a§lYES"));
        inventory.setItem(SLOT_NO, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 14, "§c§lNO"));
        player.openInventory(inventory);
    }

    /**
     * Called when a player closes the inventory.
     * Used to unregister this listener from the list, if it was the right player.
     */
    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getPlayer() == player)
            HandlerList.unregisterAll(this);
    }

    /**
     * Called when a player quits the game.
     * Used to unregister this listener from the list, if it was the right player.
     */
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (event.getPlayer() == player)
            HandlerList.unregisterAll(this);
    }

    /**
     * Called when a player is kicked from the server.
     * Used to unregister this listener from the list, if it was the right player.
     */
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (event.getPlayer() == player)
            HandlerList.unregisterAll(this);
    }

    /**
     * Called when a player clicks in his inventory
     * Used to call the 'onYes' Runnable, if the player has clicked on 'YES'.
     * Also unregister this listener, if the player has clicked on 'YES' or 'NO' and closes this inventory.
     */
    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked() != player ||
                event.getSlot() != event.getRawSlot())
            return;

        event.setCancelled(true);

        if(event.getSlot() == SLOT_YES) {
            HandlerList.unregisterAll(this);
            player.closeInventory();
            onYes.run();
        } else if(event.getSlot() == SLOT_NO) {
            HandlerList.unregisterAll(this);
            player.closeInventory();
        }
    }
}
