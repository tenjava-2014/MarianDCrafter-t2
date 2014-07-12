package com.tenjava.entries.MarianDCrafter.t2;

import com.tenjava.entries.MarianDCrafter.t2.machines.bag.BagCommandExecutor;
import com.tenjava.entries.MarianDCrafter.t2.machines.bag.BagInventory;
import com.tenjava.entries.MarianDCrafter.t2.machines.calculator.CalculatorCommandExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class TenJava extends JavaPlugin {

    static {
        ConfigurationSerialization.registerClass(BagInventory.class);
    }

    public final static String PREFIX_FAIL = "§8[§6Machines§8]§c ";
    public final static String PREFIX = "§8[§6Machines§8]§3 ";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if(getConfig().getBoolean("machines.calculator.enabled"))
            getCommand("calculator").setExecutor(new CalculatorCommandExecutor(this));
        if(getConfig().getBoolean("machines.bag.enabled"))
            getCommand("bag").setExecutor(new BagCommandExecutor(this, createBagFile()));
    }

    private FileConfiguration createBagFile() {
        File bagFile = new File(getDataFolder(), "bags.yml");
        return YamlConfiguration.loadConfiguration(bagFile);
    }
}
