package org.milk4lyfe.customSpawning.group;

import org.bukkit.entity.LivingEntity;

import java.util.*;

import static org.milk4lyfe.customSpawning.group.GroupSpawner.plugin;

public class GroupRegistry {
   protected static HashMap<UUID, HashMap<UUID, LivingEntity>> entityGroups = new HashMap<>();

    static Map<UUID, UUID> entityGroupMap = new HashMap<>();
    static Map<UUID, Boolean> marchingGroupMap = new HashMap<>();
    static Map<UUID, Boolean> aiMap = new HashMap<>();
    static Map<UUID, Integer> directionMap = new HashMap<>();
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
    public static void setDirection(UUID groupID, int direction) {
        directionMap.put(groupID, direction);
    }
    public static Integer getDirection(UUID groupID) {
        return directionMap.get(groupID);
    }
    public static void updateGroup(UUID groupId, HashMap<UUID, LivingEntity> group) {
        entityGroups.remove(groupId);
        entityGroups.put(groupId, group);
    }
    public static void toggleMarch(UUID groupId) {
        HashMap<UUID, LivingEntity>[] finalGroup = new HashMap[]{GroupRegistry.getGroup(groupId)};


        if (GroupRegistry.getMarching(groupId) == null) {
            GroupRegistry.setMarching(groupId, false);
        }
        GroupRegistry.setMarching(groupId, !GroupRegistry.getMarching(groupId));
        if (GroupRegistry.getMarching(groupId)) {

            GroupRegistry.setAI(groupId, true);


            GroupRegistry.getLeader(groupId).setAI(true);
        }
        else {
            GroupRegistry.setAI(groupId, false);
            GroupRegistry.getLeader(groupId).setAI(false);
        }
        GroupFormation.march(finalGroup, getDirection(groupId), GroupRegistry.getLeader(groupId), groupId);

    }
    public static void setAI(UUID groupId, Boolean bool) {

        GroupRegistry.aiMap.put(groupId, bool);

        Iterator<LivingEntity> iterator = entityGroups.get(groupId).values().iterator();
        while (iterator.hasNext()) {
            LivingEntity entity = iterator.next();

            entity.setAI(bool);

        }
    }
    public static HashMap<UUID, HashMap<UUID, LivingEntity>> returnEntityGroups() {
        return entityGroups;
    }
    public static HashMap<UUID, LivingEntity> getGroupForEntityId(UUID uuid) {
        return entityGroups.get(uuid);
    }
    public static boolean isGroupValid(UUID uuid) {
        return entityGroups.containsKey(uuid);
    }
    protected static int getTotalEntitiesInGroup(List<String> entityList, String groupName) {

        int sum=0;
        for (String string : entityList) {
            sum = sum + plugin.getConfig().getConfigurationSection("group." + groupName+ ".members").getInt(string);
        }
        return sum;
    }
    public static void deleteGroup(UUID groupId, LivingEntity leader) {
        Iterator<LivingEntity> iterator = entityGroups.get(groupId).values().iterator();
        while (iterator.hasNext()) {
            LivingEntity entity = iterator.next();

            entity.remove(); // Safe removal

        }
        leader.remove();
        entityGroups.remove(groupId);
        marchingGroupMap.remove(groupId);
        aiMap.remove(groupId);
        leaderMap.remove(groupId);
        directionMap.remove(groupId);



    }
    public static void setMarching(UUID groupID, Boolean yes) {
        GroupRegistry.marchingGroupMap.put(groupID, yes);
    }

    public static Boolean getMarching(UUID groupID) {
        if (GroupRegistry.marchingGroupMap.get(groupID) == null) {
            return false;
        }
        else {
            return GroupRegistry.marchingGroupMap.get(groupID);
        }

    }
    public static Boolean getAI(UUID groupId) {
        return GroupRegistry.aiMap.get(groupId);
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
}
