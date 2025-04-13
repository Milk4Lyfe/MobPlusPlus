package org.milk4lyfe.customSpawning.group;
import org.bukkit.entity.Player;
import org.milk4lyfe.customSpawning.PlayerUtil;
import org.milk4lyfe.customSpawning.group.GroupRegistry;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

import static org.milk4lyfe.customSpawning.group.GroupRegistry.entityGroups;

public class GroupFormation {
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
    public static void squareFormation(Player player, UUID groupId, int direction) {
        int xOffset = 0, zOffset = 0;
        int gridSize = (int) Math.ceil(Math.sqrt(entityGroups.get(groupId).values().size()));
        for (LivingEntity e : entityGroups.get(groupId).values()) {
            Location loc = player.getLocation();

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

            e.teleport(newLoc);

        }


    }

    public static void entityJump(LivingEntity entity, Vector moveDirection) {
        Location loc = entity.getLocation().add(moveDirection);
        entity.teleport(loc.add(0, 1, 0));
    }
    public static void entityDrop(LivingEntity entity) {
        entity.teleport(entity.getLocation().add(0, -1, 0));
    }
    public static boolean checkForSolidBlockInFront(LivingEntity entity, Vector moveDirection) {
        Location loc = entity.getLocation();
        Vector dir = loc.getDirection();
        return loc.add(dir.multiply(1)).getBlock().isSolid();
    }
    public static boolean checkIfFloating(LivingEntity entity) {
        Location loc = entity.getLocation();
        loc.add(0, -1, 0);
        return loc.getBlock().isSolid();
    }
    public static void teleportGroup(UUID groupId, Player player) {
        squareFormation(player, groupId, PlayerUtil.getPlayerDirection(player));
        GroupRegistry.leaderMap.get(groupId).teleport(player.getLocation());
    }
    public static void march(HashMap<UUID, LivingEntity>[] finalGroup, int direction, LivingEntity leader, UUID groupId) {
        new BukkitRunnable() {

            @Override
            public void run() {
                finalGroup[0] = GroupRegistry.getGroup(groupId);
                if (!GroupRegistry.getMarching(groupId) || GroupRegistry.getMarching(groupId) == null) {
                    return;
                }

                if (finalGroup[0].isEmpty()) {
                    GroupRegistry.deleteGroup(groupId, leader);
                    return;
                }
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
                for (UUID entry : finalGroup[0].keySet()) {

                    if (checkForSolidBlockInFront(finalGroup[0].get(entry), moveDirection)) {
                        entityJump(finalGroup[0].get(entry), moveDirection);
                    }
                    if (!checkIfFloating(finalGroup[0].get(entry)) && !checkForSolidBlockInFront(leader, moveDirection)) {
                        entityDrop(finalGroup[0].get(entry));
                    }

                    finalGroup[0].get(entry).setVelocity(moveDirection);
                    leader.setVelocity(moveDirection);
                }

                if (checkForSolidBlockInFront(leader, moveDirection)) {
                    entityJump(leader, moveDirection);
                }
                if (!checkIfFloating(leader) && !checkForSolidBlockInFront(leader, moveDirection)) {
                    entityDrop(leader);
                }



            }
        }.runTaskTimer(GroupSpawner.plugin, 0L, 1L); // Runs every tick (1L = 1 tick)
    }
}
