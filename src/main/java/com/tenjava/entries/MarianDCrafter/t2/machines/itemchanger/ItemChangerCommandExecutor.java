package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemChangerCommandExecutor implements CommandExecutor {

    private TenJava plugin;
    private List<ItemChanger> itemChangers;

    public ItemChangerCommandExecutor(TenJava plugin) {
        this.plugin = plugin;
        itemChangers = new ItemChangerLoader(plugin.getConfig().getConfigurationSection("machines.itemchanger")).loadItemChangers();
    }

    public List<ItemChanger> getItemChangers() {
        return itemChangers;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equals("itemchanger"))
            return false;

        if (!(sender instanceof Player)) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You must be a player to execute this command!");
            return true;
        }

        Player player = (Player) sender;

        if (!player.hasPermission("machines.itemchanger")) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You don't have permission to execute this command.");
            return true;
        }

        player.beginConversation(new Conversation(plugin, player, new ItemChangerStartPrompt(this)));

        return true;
    }
}
