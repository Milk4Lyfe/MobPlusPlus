package org.milk4lyfe.customSpawning;

import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GroupManager {
    private static HashMap<UUID, HashMap<UUID, LivingEntity>> entityGroups = new HashMap<>();
    static Map<UUID, UUID> entityGroupMap = new HashMap<>();

    public static void assignGroup(UUID groupId,  HashMap<UUID, LivingEntity> group) {
        entityGroups.put(groupId, group);
    }

    public static HashMap<UUID, LivingEntity> getGroupForEntityId(UUID uuid) {
        return entityGroups.get(uuid);
    }
    public static void updateGroup(UUID groupId, HashMap<UUID, LivingEntity> group) {
        entityGroups.remove(groupId);
        entityGroups.put(groupId, group);
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
}
