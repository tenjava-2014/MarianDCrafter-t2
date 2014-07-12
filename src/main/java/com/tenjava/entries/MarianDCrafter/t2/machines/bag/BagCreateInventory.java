package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

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

public class BagCreateInventory implements Listener {

    private final static int
            SLOT_YES = 11,
            SLOT_NO = 15;

    private Runnable onYes;
    private Inventory inventory;
    private Player player;

    public BagCreateInventory(Player player, Runnable onYes) {
        this.onYes = onYes;
        this.player = player;

        inventory = Bukkit.createInventory(player, 27, "§c§lCreate Bag?");
        inventory.setItem(SLOT_YES, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 5, "§a§lYES"));
        inventory.setItem(SLOT_NO, ItemStackUtils.itemStack(Material.WOOL, 1, (short) 14, "§c§lNO"));
        player.openInventory(inventory);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent inventory) {
        if (inventory.getPlayer() == player)
            HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (event.getPlayer() == player)
            HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (event.getPlayer() == player)
            HandlerList.unregisterAll(this);
    }

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
