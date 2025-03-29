package org.milk4lyfe.customSpawning.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.milk4lyfe.customSpawning.group.GroupRegistry;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class entityDeathEvent implements Listener {
    public mobplusplus plugin;
    public entityDeathEvent(mobplusplus plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        UUID entityId = e.getEntity().getUniqueId();
        //OBJECTIVE: GET THE GROUP
        Map<UUID, UUID> entityGroupMap = GroupRegistry.returnEntitytoGroupMap();
        UUID groupId;
        if (entityGroupMap.containsKey(entityId)) {
            groupId = entityGroupMap.get(entityId);
            HashMap<UUID, LivingEntity> group = GroupRegistry.getGroupForEntityId(groupId);
            group.remove(entityId);
            GroupRegistry.updateGroup(groupId, group);
        }


    }
}
