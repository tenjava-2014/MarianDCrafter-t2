package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import com.tenjava.entries.MarianDCrafter.t2.util.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Represents an bag inventory.
 */
public class BagInventory {

    /**
     * The slot of the destroy wool button.
     */
    public final static int SLOT_DESTROY = 53;

    private Inventory inventory;
    private Player player;

    /**
     * Initializes a new BagInventory with a ConfigurationSection and a Player.
     * @param section the ConfigurationSection in the bag file
     * @param player the player
     */
    public BagInventory(ConfigurationSection section, Player player) {
        this.player = player;
        createInventory();
        for(String key : section.getKeys(false))
            inventory.setItem(Integer.parseInt(key), (ItemStack) section.get(key));
    }

    /**
     * Initializes a new empty BagInventory with a Player.
     * @param player the player
     */
    public BagInventory(Player player) {
        this.player = player;
        createInventory();
    }

    /**
     * Returns the inventory.
     * @return the inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Saves this inventory to the given ConfigurationSection.
     * @param section the section
     */
    public void save(ConfigurationSection section) {
        for(int i = 0; i < inventory.getContents().length; i++)
            section.set(Integer.toString(i), inventory.getContents()[i]);
    }

    /**
     * Creates the internal inventory with the destroy wool button.
     */
    private void createInventory() {
        inventory = Bukkit.createInventory(player, 54, "§3§lBag");
        inventory.setItem(SLOT_DESTROY, ItemStackUtils.itemStack(Material.WOOL, 1, (short)14, "§4Destroy Bag"));
    }
}
