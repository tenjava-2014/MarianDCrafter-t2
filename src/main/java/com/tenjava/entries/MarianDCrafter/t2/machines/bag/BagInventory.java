package com.tenjava.entries.MarianDCrafter.t2.machines.bag;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class BagInventory implements ConfigurationSerializable {

    private Inventory inventory;

    public BagInventory(Map<String, Object> map) {

    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
