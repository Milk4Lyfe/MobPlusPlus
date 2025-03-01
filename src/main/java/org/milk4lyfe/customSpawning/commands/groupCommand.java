package org.milk4lyfe.customSpawning.commands;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.milk4lyfe.customSpawning.GroupManager;
import org.milk4lyfe.customSpawning.listeners.entityDeathEvent;
import org.milk4lyfe.customSpawning.mobplusplus;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class groupCommand implements CommandExecutor {
    public mobplusplus plugin;
    public UUID groupId = UUID.randomUUID();
    public boolean marching = false;
    int direction = 0;
    public LivingEntity leader;
    HashMap<UUID, LivingEntity> group = new HashMap<UUID, LivingEntity>();
    public groupCommand(mobplusplus plugin) {
        this.plugin = plugin;
        this.groupId = UUID.randomUUID();

    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,  String s,  String[] args) {
        if (!isGroup(args)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "Incorrect usage. Idiot"));

            return true;
        }
        if (args[0].equalsIgnoreCase("spawn") && args.length==2) {
            Player player = (Player) commandSender;
            World world = player.getWorld();

            try {
                ConfigurationSection bettergroupConfig = plugin.getConfig().getConfigurationSection("group." + args[1]);
                List<String> entityList = mobplusplus.getListFromConfiguration(plugin, "group." + args[1] + ".members");

                direction = GroupManager.getPlayerDirection(player);
                leader = Spawner.spawn(player, world, bettergroupConfig.getString("leader"), plugin, player.getLocation());
                leader.setAI(false);
                GroupManager.setLeader(groupId, leader);
                group = GroupManager.spawnGroup(player, entityList, args[1], direction, groupId);
                commandSender.sendMessage(String.valueOf(direction));
                GroupManager.assignGroup(groupId, group);
            }catch(NullPointerException e) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
            }



        }
        if (args[0].equalsIgnoreCase("march") && args.length == 2) {
            try {
                if (!GroupManager.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }
            HashMap<UUID, LivingEntity>[] finalGroup = new HashMap[]{GroupManager.getGroup(UUID.fromString(args[1]))};

            commandSender.sendMessage(finalGroup[0].toString());
            if (GroupManager.getMarching(UUID.fromString(args[1])) == null) {
                GroupManager.setMarching(UUID.fromString(args[1]), false);
            }
            GroupManager.setMarching(UUID.fromString(args[1]), !GroupManager.getMarching(UUID.fromString(args[1])));
            if (GroupManager.getMarching(UUID.fromString(args[1]))) {
                for(LivingEntity e : finalGroup[0].values()) {
                    e.setAI(true);

                }
                leader.setAI(true);
            }
            else {
                for(LivingEntity e : finalGroup[0].values()) {
                    e.setAI(false);

                }
                leader.setAI(false);
            }
            GroupManager.march(finalGroup, direction, leader, groupId);
        }
        if (args[0].equalsIgnoreCase("delete") && args.length == 2) {
            try {
                if (!GroupManager.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }

            GroupManager.deleteGroup(UUID.fromString(args[1]), leader);

        }
        if (args[0].equalsIgnoreCase("tphere") && args.length == 2) {
            try {
                if (!GroupManager.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }
            GroupManager.teleportGroup(UUID.fromString(args[1]), (Player) commandSender);
        }
        if (args[0].equalsIgnoreCase("toggleAI") && args.length == 2) {
            try {
                if (!GroupManager.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }
            if (GroupManager.getAI(UUID.fromString(args[1])) == null) {
                GroupManager.setAI(UUID.fromString(args[1]), false);
            }
            GroupManager.setAI(UUID.fromString(args[1]), !GroupManager.getAI(UUID.fromString(args[1])));

        }


        return true;
    }

    private boolean isGroup(String[] args) {
        return args.length > 0;
    }


}
