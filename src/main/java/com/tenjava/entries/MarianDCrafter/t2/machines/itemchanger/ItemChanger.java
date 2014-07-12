package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.machines.Delay;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemChanger {

    public final static String CONVERSATION_PROMPT_TEXT = "§3'stop' §bto stop the ItemChanger\n" +
            "§3'list' §bto see all possible ItemChangers\n" +
            "§3'#' §bstart an ItemChanger with the given ID\n";

    private ItemStack from, to;
    private Delay delay;
    private int delayTime, materialPerDelay;
    private Material driveMaterial;

    public ItemChanger(ItemStack from, ItemStack to, Delay delay, int delayTime, int materialPerDelay, Material driveMaterial) {
        this.from = from;
        this.to = to;
        this.delay = delay;
        this.delayTime = delayTime;
        this.materialPerDelay = materialPerDelay;
        this.driveMaterial = driveMaterial;
    }

    @Override
    public String toString() {
        return "FROM: " + from.getType() + "(" + from.getAmount() + ") TO: " + to.getType() + "(" + to.getAmount() + ") " +
                "Every " + delay + " " + materialPerDelay + " " + driveMaterial + ". " + delay + "s for 1 item change: " + delayTime;
    }

    public ItemStack getFrom() {
        return from;
    }

    public ItemStack getTo() {
        return to;
    }

    public Delay getDelay() {
        return delay;
    }

    public int getDelayTime() {
        return delayTime;
    }

    public int getMaterialPerDelay() {
        return materialPerDelay;
    }

    public Material getDriveMaterial() {
        return driveMaterial;
    }
}
