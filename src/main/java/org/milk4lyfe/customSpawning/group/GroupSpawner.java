package org.milk4lyfe.customSpawning.group;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.milk4lyfe.customSpawning.commands.Spawner;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GroupSpawner {
    public static mobplusplus plugin;
    public GroupSpawner(mobplusplus plugin) {
        GroupSpawner.plugin = plugin;
    }
    public static HashMap<UUID, LivingEntity> spawnGroup(Player player, List<String> entityList, String groupName, int direction, UUID groupId) {
        HashMap<UUID, LivingEntity> group = new HashMap<UUID, LivingEntity>();

        ConfigurationSection groupConfig = plugin.getConfig().getConfigurationSection("group." + groupName + ".members");
        World world = player.getWorld();
        int gridSize = (int) Math.ceil(Math.sqrt(GroupRegistry.getTotalEntitiesInGroup(entityList, groupName)));


        int xOffset = 0, zOffset = 0;
        for (String string : entityList) {
            for (int j = 0; j < groupConfig.getInt(string); j++) {
                LivingEntity entity = Spawner.spawn(player, world, string, plugin, player.getLocation());

                Location loc = entity.getLocation();
                group.put(entity.getUniqueId(), entity);
                GroupRegistry.addEntitytoGroupMap(entity.getUniqueId(), groupId);

                Location newLoc = GroupFormation.placeEntity( direction, loc, xOffset, zOffset);
                if (direction ==1 || direction == 2) {
                    xOffset++;
                }
                else {
                    xOffset--;
                }
                if (Math.abs(xOffset) >= gridSize){
                    xOffset = 0;
                    if (direction ==2 || direction == 1) {
                        zOffset++;
                    }
                    else {
                        zOffset--;
                    }
                }
                entity.setAI(false);
                entity.teleport(newLoc);



            }
        }
        return group;
    }
}
