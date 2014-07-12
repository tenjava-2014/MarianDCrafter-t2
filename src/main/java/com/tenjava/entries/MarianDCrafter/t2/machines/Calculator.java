package com.tenjava.entries.MarianDCrafter.t2.machines;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Calculator implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!command.getName().equals("calculator"))
            return false;

        sender.sendMessage("Open the calculator!");

        return true;
    }
}
