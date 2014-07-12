package com.tenjava.entries.MarianDCrafter.t2;

import com.tenjava.entries.MarianDCrafter.t2.machines.calculator.CalculatorCommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin {

    public final static String PREFIX_FAIL = "§8[§6Machines§8]§c ";
    public final static String PREFIX = "§8[§6Machines§8]§3 ";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if(getConfig().getBoolean("machines.calculator.enabled"))
            getCommand("calculator").setExecutor(new CalculatorCommandExecutor(this));
    }
}
