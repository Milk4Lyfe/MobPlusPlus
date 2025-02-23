package org.milk4lyfe.customSpawning.commands;

import net.kyori.adventure.text.ComponentLike;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class groupCommand implements CommandExecutor {
    public mobplusplus plugin;
    public groupCommand(mobplusplus plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,  String s,  String[] args) {
        if (!isGroup(args)) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6Mob++&8] &cError: Invalid Group!"));
            return true;
        }
        HashMap<LivingEntity, Integer> group = new HashMap<LivingEntity, Integer>();
        Player player = (Player) commandSender;
        World world = player.getWorld();

        ConfigurationSection bettergroupConfig = plugin.getConfig().getConfigurationSection("group." + args[0]);
        List<String> entityList = mobplusplus.getListFromConfiguration(plugin, "group." + args[0] + ".members");

        int direction = getPlayerDirection(player);
        LivingEntity leader = Spawner.spawn(player, world, bettergroupConfig.getString("leader"), plugin, player.getLocation());
        
        group = spawnGroup(player, entityList, args[0], direction);
        commandSender.sendMessage(String.valueOf(direction));

        HashMap<LivingEntity, Integer> finalGroup = group;
        commandSender.sendMessage(finalGroup.toString());
        new BukkitRunnable() {

            @Override
            public void run() {

                for (LivingEntity entry : finalGroup.keySet()) {
                    Vector moveDirection = new Vector(0, 0, 0);

                        moveDirection = switch (direction) {
                            case 0 -> //South
                                    new Vector(0, 0, 0.1);
                            case 1 ->//West
                                    new Vector(-0.1, 0, 0);
                            case 2 ->//North
                                    new Vector(0, 0, -0.1);
                            case 3 -> //East
                                    new Vector(0.1, 0, 0);
                            default -> moveDirection;
                        };


                    leader.setVelocity(moveDirection);
                    entry.setVelocity(moveDirection);
                }


            }
        }.runTaskTimer(plugin, 0L, 1L); // Runs every tick (1L = 1 tick)

        return true;
    }
    private int getTotalEntitiesInGroup(List<String>entityList, String groupName) {

        int sum=0;
        for (String string : entityList) {
            sum = sum + plugin.getConfig().getConfigurationSection("group." + groupName+ ".members").getInt(string);
        }
        return sum;
    }

    protected int getPlayerDirection(Player player) {
        float yaw = player.getLocation().getYaw();
        int direction = 0;
        if (yaw >= -45 && yaw < 45) {
            // Facing South
            direction = 0;
        } else if (yaw >= 45 && yaw < 135) {
            // Facing West
            direction = 1;
        } else if (yaw >= 135 || yaw < -135) {
            direction = 2;
            // Facing North
        } else if (yaw >= -135 && yaw < -45) {
            direction = 3;
            // Facing East
        }
        return direction;
    }

    protected HashMap<LivingEntity, Integer> spawnGroup(Player player, List<String>entityList, String groupName, int direction) {
        HashMap<LivingEntity, Integer> group = new HashMap<LivingEntity, Integer>();
        ConfigurationSection groupConfig = plugin.getConfig().getConfigurationSection("group." + groupName + ".members");
        World world = player.getWorld();
        int gridSize = (int) Math.ceil(Math.sqrt(getTotalEntitiesInGroup(entityList, groupName)));

        int tag = 0;
        int xOffset = 0, zOffset = 0;
        for (String string : entityList) {
            for (int j = 0; j < groupConfig.getInt(string); j++) {
                LivingEntity entity = Spawner.spawn(player, world, string, plugin, player.getLocation());

                Location loc = entity.getLocation();
                group.put(entity, tag);
                placeEntity( direction, loc, xOffset, zOffset);
                if (direction ==1 || direction == 2) {
                    xOffset++;
                }
                else {
                    xOffset--;
                }
                if (Math.abs(xOffset) >= gridSize){
                    xOffset = 0;
                    if (direction ==2) {
                        zOffset++;
                    }
                    else {
                        zOffset--;
                    }
                }
                tag++;
                entity.teleport(loc);

            }
        }
        return group;
    }
    private boolean isGroup(String[] args) {
        return args.length > 0 && plugin.getConfig().contains("group." + args[0]);
    }

    protected Location placeEntity(int direction, Location loc, int xOffset, int zOffset) {
        switch(direction) {
            case 0: //South
                loc.add(xOffset, 0, zOffset-2);
                break;
            case 1://West
                loc.add(zOffset+2 , 0, xOffset);
                break;
            case 2://North
                loc.add(xOffset*-1 , 0, zOffset+2);
                break;
            case 3: //East
                loc.add(zOffset-2 , 0, xOffset);
                break;
        }
        return loc;
    }
}
