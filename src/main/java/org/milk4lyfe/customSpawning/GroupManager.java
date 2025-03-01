package org.milk4lyfe.customSpawning;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.milk4lyfe.customSpawning.commands.Spawner;

import java.util.*;

public class GroupManager {
    public static mobplusplus plugin;
    public GroupManager(mobplusplus plugin) {
        GroupManager.plugin = plugin;
    }
    private static HashMap<UUID, HashMap<UUID, LivingEntity>> entityGroups = new HashMap<>();

    static Map<UUID, UUID> entityGroupMap = new HashMap<>();
    static Map<UUID, Boolean> marchingGroupMap = new HashMap<>();
    static Map<UUID, Boolean> aiMap = new HashMap<>();
    static Map<UUID, LivingEntity> leaderMap = new HashMap<>();
    public static void assignGroup(UUID groupId,  HashMap<UUID, LivingEntity> group) {
        entityGroups.put(groupId, group);
    }
    public static void setLeader(UUID groupID, LivingEntity leader) {
        leaderMap.put(groupID, leader);
    }
    public static LivingEntity getLeader(UUID groupID) {
        return leaderMap.get(groupID);
    }
    public static HashMap<UUID, LivingEntity> getGroupForEntityId(UUID uuid) {
        return entityGroups.get(uuid);
    }
    public static boolean isGroupValid(UUID uuid) {
        return entityGroups.containsKey(uuid);
    }
    public static void updateGroup(UUID groupId, HashMap<UUID, LivingEntity> group) {
        entityGroups.remove(groupId);
        entityGroups.put(groupId, group);
    }
    public static void setAI(UUID groupId, Boolean bool) {

        aiMap.put(groupId, bool);

        Iterator<LivingEntity> iterator = entityGroups.get(groupId).values().iterator();
        while (iterator.hasNext()) {
            LivingEntity entity = iterator.next();

            entity.setAI(bool);

        }
    }
    public static Boolean getAI(UUID groupId) {
        return aiMap.get(groupId);
    }
    public static HashMap<UUID, LivingEntity> getGroup(UUID groupId) {
        return entityGroups.get(groupId);
    }

    public static void addEntitytoGroupMap(UUID uuid, UUID groupId) {
        entityGroupMap.put(uuid, groupId);
    }

    public static Map<UUID, UUID> returnEntitytoGroupMap() {
        return entityGroupMap;
    }

    public static ArrayList<UUID> returnGroupMapAsList() {
        return new ArrayList<>(entityGroups.keySet());
    }

    public static void setMarching(UUID groupID, Boolean yes) {
        marchingGroupMap.put(groupID, yes);
    }

    public static Boolean getMarching(UUID groupID) {
        return marchingGroupMap.get(groupID);
    }

    public static void teleportGroup(UUID groupId, Player player) {
        squareFormation(player, groupId, getPlayerDirection(player));
        leaderMap.get(groupId).teleport(player.getLocation());
    }

    public static void deleteGroup(UUID groupId, LivingEntity leader) {
        Iterator<LivingEntity> iterator = entityGroups.get(groupId).values().iterator();
        while (iterator.hasNext()) {
            LivingEntity entity = iterator.next();

                entity.remove(); // Safe removal

        }
        leader.remove();
        entityGroups.remove(groupId);



    }

    protected static Location placeEntity(int direction, Location loc, int xOffset, int zOffset) {
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
    public static void march(HashMap<UUID, LivingEntity>[] finalGroup, int direction, LivingEntity leader, UUID groupId) {
        new BukkitRunnable() {

            @Override
            public void run() {
                finalGroup[0] = getGroup(groupId);
                if (!getMarching(groupId)) {
                    cancel();
                }
                if (finalGroup[0].isEmpty()) {
                    deleteGroup(groupId, leader);
                    cancel();
                }
                for (UUID entry : finalGroup[0].keySet()) {
                    org.bukkit.util.Vector moveDirection = new org.bukkit.util.Vector(0, 0, 0);

                    moveDirection = switch (direction) {
                        case 0 -> //South
                                new org.bukkit.util.Vector(0, 0, 0.1);
                        case 1 ->//West
                                new org.bukkit.util.Vector(-0.1, 0, 0);
                        case 2 ->//North
                                new org.bukkit.util.Vector(0, 0, -0.1);
                        case 3 -> //East
                                new Vector(0.1, 0, 0);
                        default -> moveDirection;
                    };



                    leader.setVelocity(moveDirection);

                    finalGroup[0].get(entry).setVelocity(moveDirection);
                }


            }
        }.runTaskTimer(plugin, 0L, 1L); // Runs every tick (1L = 1 tick)
    }
    private static int getTotalEntitiesInGroup(List<String> entityList, String groupName) {

        int sum=0;
        for (String string : entityList) {
            sum = sum + plugin.getConfig().getConfigurationSection("group." + groupName+ ".members").getInt(string);
        }
        return sum;
    }


    public static int getPlayerDirection(Player player) {
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
    public static void squareFormation(Player player, UUID groupId, int direction) {
        int xOffset = 0, zOffset = 0;
        int gridSize = (int) Math.ceil(Math.sqrt(entityGroups.get(groupId).values().size()));
        for (LivingEntity e : entityGroups.get(groupId).values()) {
                Location loc = player.getLocation();

                Location newLoc = placeEntity( direction, loc, xOffset, zOffset);
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
                
                e.teleport(newLoc);

            }


    }

    public static HashMap<UUID, LivingEntity> spawnGroup(Player player, List<String> entityList, String groupName, int direction, UUID groupId) {
        HashMap<UUID, LivingEntity> group = new HashMap<UUID, LivingEntity>();

        ConfigurationSection groupConfig = plugin.getConfig().getConfigurationSection("group." + groupName + ".members");
        World world = player.getWorld();
        int gridSize = (int) Math.ceil(Math.sqrt(getTotalEntitiesInGroup(entityList, groupName)));


        int xOffset = 0, zOffset = 0;
        for (String string : entityList) {
            for (int j = 0; j < groupConfig.getInt(string); j++) {
                LivingEntity entity = Spawner.spawn(player, world, string, plugin, player.getLocation());

                Location loc = entity.getLocation();
                group.put(entity.getUniqueId(), entity);
                addEntitytoGroupMap(entity.getUniqueId(), groupId);

                Location newLoc = placeEntity( direction, loc, xOffset, zOffset);
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
