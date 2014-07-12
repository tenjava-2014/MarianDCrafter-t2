package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class BagInventory implements ConfigurationSerializable {

    private Inventory inventory;
    private Player player;
    private Map<String, Object> serialized;

    public BagInventory(Map<String, Object> map) {
        this.serialized = map;
    }

    public BagInventory(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 54, "§3§lBag");
    }

    public void serialized(Player player) {
        this.player = player;
        this.inventory = Bukkit.createInventory(player, 54, "§3§lBag");
        for(Map.Entry<String, Object> entry : serialized.entrySet())
            inventory.setItem(Integer.parseInt(entry.getKey()), (org.bukkit.inventory.ItemStack) entry.getValue());
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }

    public Inventory getInventory() {
        return inventory;
    }
}
