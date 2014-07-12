package com.tenjava.entries.MarianDCrafter.t2.machines.itemchanger;

import com.tenjava.entries.MarianDCrafter.t2.TenJava;
import com.tenjava.entries.MarianDCrafter.t2.machines.Machine;
import com.tenjava.entries.MarianDCrafter.t2.util.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversable;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemChangerCommandExecutor implements CommandExecutor, Listener {

    private TenJava plugin;
    private List<ItemChanger> itemChangers;
    private Map<String, Machine> machines = new HashMap<String, Machine>();

    public ItemChangerCommandExecutor(TenJava plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
        itemChangers = new ItemChangerLoader(plugin.getConfig().getConfigurationSection("machines.itemchanger")).loadItemChangers();
    }

    public List<ItemChanger> getItemChangers() {
        return itemChangers;
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        remove(event.getPlayer());
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        remove(event.getPlayer());
    }

    private void remove(Player player) {
        if (machines.containsKey(player.getName())) {
            machines.get(player.getName()).cancel();
            machines.remove(player.getName());
        }
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

        if (machines.containsKey(player.getName())) {
            sender.sendMessage(TenJava.PREFIX_FAIL + "You have already started an ItemChanger.");
            return true;
        }

        player.beginConversation(new Conversation(plugin, player, new ItemChangerStartPrompt(this)));

        return true;
    }

    public Prompt getPrompt(String input, Conversable conversable) {
        if (input.equals("stop"))
            return null;
        else if (input.equals("list"))
            return new ItemChangerListPrompt(this);
        else {
            try {
                String error = start(Integer.parseInt(input), (Player) conversable);
                if (error != null)
                    return new ItemChangerStartPrompt(this, error);
                return null;
            } catch (NumberFormatException e) {
                return new ItemChangerStartPrompt(this);
            }
        }
    }

    public String start(int index, final Player player) {
        if (itemChangers.size() <= index) // ItemChanger not contained
            return "An ItemChanger with this id doesn't exist.";

        final ItemChanger itemChanger = itemChangers.get(index);

        if(!player.getInventory().contains(itemChanger.getDriveMaterial(), itemChanger.getDelayTime() * itemChanger.getMaterialPerDelay())) {
            return "You don't have enough drive material.";
        }

        if(!player.getInventory().contains(itemChanger.getFrom())) {
            return "You don't have enough 'from' material.";
        }

        Machine machine = new Machine(plugin, player, itemChanger.getDriveMaterial(), itemChanger.getMaterialPerDelay(), itemChanger.getDelay(), new Runnable() {

            int needed = itemChanger.getDelayTime();

            @Override
            public void run() {

                if(!player.getInventory().contains(itemChanger.getDriveMaterial(), needed * itemChanger.getMaterialPerDelay())) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have enough drive material.");
                    remove(player);
                }

                if(!player.getInventory().contains(itemChanger.getFrom())) {
                    player.sendMessage(TenJava.PREFIX_FAIL + "You don't have enough 'from' material.");
                    remove(player);
                }

                needed--;
                if(needed == 0) {
                    ItemStackUtils.removeItem(player, itemChanger.getFrom().getType(), itemChanger.getFrom().getAmount());
                    player.getInventory().addItem(itemChanger.getTo());
                    needed = itemChanger.getDelayTime();
                }
            }
        });
        machine.start();
        machines.put(player.getName(), machine);

        return null;
    }
}
