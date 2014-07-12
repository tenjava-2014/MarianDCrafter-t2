package com.tenjava.entries.MarianDCrafter.t2;

import com.tenjava.entries.MarianDCrafter.t2.machines.bag.BagCommandExecutor;
import com.tenjava.entries.MarianDCrafter.t2.machines.calculator.CalculatorCommandExecutor;
import com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger.ItemChangerCommandExecutor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TenJava extends JavaPlugin {

    public final static String PREFIX_FAIL = "§8[§6Machines§8]§c ";
    public final static String PREFIX = "§8[§6Machines§8]§3 ";

    private BagCommandExecutor bagCommandExecutor;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if(getConfig().getBoolean("machines.calculator.enabled"))
            getCommand("calculator").setExecutor(new CalculatorCommandExecutor(this));

        File bagFile = new File(getDataFolder(), "bags.yml");
        if(getConfig().getBoolean("machines.bag.enabled")) {
            bagCommandExecutor = new BagCommandExecutor(this, YamlConfiguration.loadConfiguration(bagFile), bagFile);
            getCommand("bag").setExecutor(bagCommandExecutor);
        }

        if(getConfig().getBoolean("machines.itemchanger.enabled"))
            getCommand("itemchanger").setExecutor(new ItemChangerCommandExecutor(this));
    }

    @Override
    public void onDisable() {
        bagCommandExecutor.saveBags();
    }
}
