package org.milk4lyfe.customSpawning.commands;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.milk4lyfe.customSpawning.PlayerUtil;
import org.milk4lyfe.customSpawning.group.GroupFormation;
import org.milk4lyfe.customSpawning.group.GroupRegistry;
import org.milk4lyfe.customSpawning.group.GroupSpawner;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.*;

import static org.milk4lyfe.customSpawning.group.GroupRegistry.returnEntityGroups;


public class groupCommand implements CommandExecutor {
    public mobplusplus plugin;


    int direction = 0;

    HashMap<UUID, LivingEntity> group = new HashMap<UUID, LivingEntity>();
    public groupCommand(mobplusplus plugin) {
        this.plugin = plugin;


    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,  String s,  String[] args) {
        if (!isGroup(args)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Incorrect usage. Idiot"));

            return true;
        }
        if (args[0].equalsIgnoreCase("spawn") && args.length==2) {
            spawnCommand(commandSender, args);
        }
        if (args[0].equalsIgnoreCase("march") && args.length == 2) {
            marchCommand(commandSender, args);
        }
        if (args[0].equalsIgnoreCase("delete") && args.length == 2) {
         deleteCommand(commandSender, args);

        }
        if (args[0].equalsIgnoreCase("tphere") && args.length == 2) {
            tphereCommand(commandSender, args);
        }
        if (args[0].equalsIgnoreCase("toggleAI") && args.length == 2) {
            toggleAiCommand(commandSender, args);

        }


        return true;
    }
    private void spawnCommand(CommandSender commandSender, String[] args) {
        Player player = (Player) commandSender;
        World world = player.getWorld();

        try {
            ConfigurationSection bettergroupConfig = plugin.getConfig().getConfigurationSection("group." + args[1]);
            List<String> entityList = mobplusplus.getListFromConfiguration(plugin, "group." + args[1] + ".members");
            UUID groupId  = UUID.randomUUID();
            direction = PlayerUtil.getPlayerDirection(player);
            GroupRegistry.setDirection(groupId, direction);
            LivingEntity leader = Spawner.spawn(player, world, bettergroupConfig.getString("leader"), plugin, player.getLocation());
            leader.setAI(false);
            GroupRegistry.setLeader(groupId, leader);
            group = GroupSpawner.spawnGroup(player, entityList, args[1], direction, groupId);
            commandSender.sendMessage(String.valueOf(direction));
            GroupRegistry.assignGroup(groupId, group);
        }catch(NullPointerException e) {
            PlayerUtil.sendPlayerMessage((Player) commandSender, "ER_INVALID_GROUP");
        }


    }
    private void marchCommand(CommandSender commandSender, String[] args) {
        if (Objects.equals(args[1], "all")) {
            List<UUID> toDelete = new ArrayList<>();
            toDelete.addAll(returnEntityGroups().keySet());
            for (UUID g : toDelete) {
                GroupRegistry.toggleMarch(g);
            }

        }
        else {
            try {
                if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    PlayerUtil.sendPlayerMessage((Player) commandSender, "ER_INVALID_GROUP");
                    return;
                }
            }
            catch (IllegalArgumentException e) {

                PlayerUtil.sendPlayerMessage((Player) commandSender, "ER_INVALID_UUID");
                return;

            }
            GroupRegistry.toggleMarch(UUID.fromString(args[1]));
        }

    }
    private void deleteCommand(CommandSender commandSender, String[] args) {
        if (Objects.equals(args[1], "all")) {
            List<UUID> toDelete = new ArrayList<>();
            toDelete.addAll(returnEntityGroups().keySet());
            for (UUID g : toDelete) {
                GroupRegistry.deleteGroup(g, GroupRegistry.getLeader(g));
            }

        }
        else {
            try {
                if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return;

            }
            GroupRegistry.deleteGroup(UUID.fromString(args[1]), GroupRegistry.getLeader(UUID.fromString(args[1])));

        }

    }
    private void tphereCommand(CommandSender commandSender, String[] args) {
        try {
            if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                PlayerUtil.sendPlayerMessage((Player) commandSender, "ER_INVALID_GROUP");
                return;
            }
        }
        catch (IllegalArgumentException e) {

            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
            return;

        }
        GroupFormation.teleportGroup(UUID.fromString(args[1]), (Player) commandSender);

    }
    private void toggleAiCommand(CommandSender commandSender, String[] args) {
        try {
            if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                return;
            }
        }
        catch (IllegalArgumentException e) {

            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
            return;

        }
        if (GroupRegistry.getAI(UUID.fromString(args[1])) == null) {
            GroupRegistry.setAI(UUID.fromString(args[1]), false);
        }
        GroupRegistry.setAI(UUID.fromString(args[1]), !GroupRegistry.getAI(UUID.fromString(args[1])));

    }


    private boolean isGroup(String[] args) {
        return args.length > 0;
    }


}
