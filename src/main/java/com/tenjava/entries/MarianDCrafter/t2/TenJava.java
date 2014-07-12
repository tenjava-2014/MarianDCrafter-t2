package com.tenjava.entries.MarianDCrafter.t2;

import com.tenjava.entries.MarianDCrafter.t2.machines.Calculator;
import org.bukkit.plugin.java.JavaPlugin;

public class TenJava extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("calculator").setExecutor(new Calculator());
    }
}
