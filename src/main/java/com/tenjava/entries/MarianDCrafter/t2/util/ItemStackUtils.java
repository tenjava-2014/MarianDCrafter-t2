package com.tenjava.entries.MarianDCrafter.t2.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

/**
 * Utils for Bukkit's class ItemStack.
 */
public final class ItemStackUtils {

    private ItemStackUtils() {
    }

    /**
     * Creates a new ItemStack.
     * @param material the material
     * @param amount the amount
     * @param data the data
     * @param name the display name
     * @param lore the lore strings
     * @return
     */
    public static ItemStack itemStack(Material material, int amount, short data, String name, String... lore) {
        ItemStack itemStack = new ItemStack(material, amount, data);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    public static void removeItem(Player player, Material material, int amount) {
        PlayerInventory inventory = player.getInventory();

        ItemStack[] contents = inventory.getContents();
        for (int i = 0; i < contents.length; i++) {
            if (contents[i] != null && contents[i].getType() == material) { // only execute if it's the driveMaterial
                int remove = contents[i].getAmount() - amount;
                if (remove > 0) { // in the ItemStack is enough material to remove
                    contents[i].setAmount(remove);
                    break;
                } else if (remove == 0) { // if the amount is 0, we need to remove the item instead of set amount to 0
                    inventory.setItem(i, null);
                    break;
                } else {
                    inventory.setItem(i, null);
                    amount = Math.abs(remove);
                }
            }
        }
    }

}
