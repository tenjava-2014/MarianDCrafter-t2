package com.tenjava.entries.MarianDCrafter.t2;

import com.tenjava.entries.MarianDCrafter.t2.machines.Calculator;
import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin {

    public final static String PREFIX_FAIL = "§8[§6Machines§8]§c ";
    public final static String PREFIX = "§8[§6Machines§8]§3 ";

    @Override
    public void onEnable() {
        getCommand("calculator").setExecutor(new Calculator(this));
    }
}
