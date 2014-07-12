package com.tenjava.entries.MarianDCrafter.t2.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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

}
