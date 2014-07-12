package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import com.tenjava.entries.MarianDCrafter.t2.util.ConfirmDialogInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

/**
 * Represents an inventory where the player is asked to destroy his bag.
 */
public class BagDestroyInventory extends ConfirmDialogInventory implements Listener {

    public BagDestroyInventory(Player player, Runnable onYes) {
        super(player, onYes, "§c§lDestroy Bag?");
    }
}
