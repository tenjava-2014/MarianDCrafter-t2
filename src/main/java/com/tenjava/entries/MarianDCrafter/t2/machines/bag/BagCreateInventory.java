package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import com.tenjava.entries.MarianDCrafter.t2.util.ConfirmDialogInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Represents an inventory where the player is asked to create a bag.
 */
public class BagCreateInventory extends ConfirmDialogInventory implements Listener {

    public BagCreateInventory(Player player, Runnable onYes) {
        super(player, onYes, "§c§lCreate Bag?");
    }

}
