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

import java.util.HashMap;
import java.util.List;
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

                direction = PlayerUtil.getPlayerDirection(player);
                leader = Spawner.spawn(player, world, bettergroupConfig.getString("leader"), plugin, player.getLocation());
                leader.setAI(false);
                GroupRegistry.setLeader(groupId, leader);
                group = GroupSpawner.spawnGroup(player, entityList, args[1], direction, groupId);
                commandSender.sendMessage(String.valueOf(direction));
                GroupRegistry.assignGroup(groupId, group);
            }catch(NullPointerException e) {
                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
            }



        }
        if (args[0].equalsIgnoreCase("march") && args.length == 2) {
            try {
                if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }
            HashMap<UUID, LivingEntity>[] finalGroup = new HashMap[]{GroupRegistry.getGroup(UUID.fromString(args[1]))};

            commandSender.sendMessage(finalGroup[0].toString());
            if (GroupRegistry.getMarching(UUID.fromString(args[1])) == null) {
                GroupRegistry.setMarching(UUID.fromString(args[1]), false);
            }
            GroupRegistry.setMarching(UUID.fromString(args[1]), !GroupRegistry.getMarching(UUID.fromString(args[1])));
            if (GroupRegistry.getMarching(UUID.fromString(args[1]))) {
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
            GroupFormation.march(finalGroup, direction, leader, groupId);
        }
        if (args[0].equalsIgnoreCase("delete") && args.length == 2) {
            try {
                if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }

            GroupRegistry.deleteGroup(UUID.fromString(args[1]), leader);

        }
        if (args[0].equalsIgnoreCase("tphere") && args.length == 2) {
            try {
                if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }
            GroupFormation.teleportGroup(UUID.fromString(args[1]), (Player) commandSender);
        }
        if (args[0].equalsIgnoreCase("toggleAI") && args.length == 2) {
            try {
                if (!GroupRegistry.isGroupValid(UUID.fromString(args[1]))) { // On the rare case that the argument is an actual UUID but not a valid group
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_GROUP")));
                    return true;
                }
            }
            catch (IllegalArgumentException e) {

                commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString("ER_INVALID_UUID")));
                return true;

            }
            if (GroupRegistry.getAI(UUID.fromString(args[1])) == null) {
                GroupRegistry.setAI(UUID.fromString(args[1]), false);
            }
            GroupRegistry.setAI(UUID.fromString(args[1]), !GroupRegistry.getAI(UUID.fromString(args[1])));

        }


        return true;
    }

    private boolean isGroup(String[] args) {
        return args.length > 0;
    }


}
