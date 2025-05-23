package org.milk4lyfe.customSpawning;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;



public class PlayerUtil {
    public static mobplusplus plugin;
    public PlayerUtil(mobplusplus plugin) {
        PlayerUtil.plugin = plugin;
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
    public static void sendPlayerMessage(Player player, String message) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', plugin.getMessagesConfig().getString(message)));
    }
}
