package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.machines.Delay;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ItemChangerLoader {

    private ConfigurationSection section;

    public ItemChangerLoader(ConfigurationSection section) {
        this.section = section;
    }

    public List<ItemChanger> loadItemChangers() {
        List<ItemChanger> itemChangers = new ArrayList<ItemChanger>();

        for(int i = 0; section.contains(Integer.toString(i)); i++) {
            String iStr = Integer.toString(i);

            String fromString = section.getString(iStr + ".from");
            String[] fromSplitted = fromString.split(" ");

            String toString = section.getString(iStr + ".to");
            String[] toSplitted = toString.split(" ");

            ItemChanger itemChanger = new ItemChanger(
                    new ItemStack(Material.getMaterial(fromSplitted[0]), Integer.parseInt(fromSplitted[1])),
                    new ItemStack(Material.getMaterial(toSplitted[0]), Integer.parseInt(toSplitted[1])),
                    Delay.valueOf(section.getString(iStr + ".delay")),
                    section.getInt(iStr + ".delayTime"),
                    section.getInt(iStr  +".materialPerDelay"),
                    Material.getMaterial(section.getString(iStr + ".driveMaterial"))
            );
            itemChangers.add(itemChanger);
        }

        return itemChangers;
    }

}
