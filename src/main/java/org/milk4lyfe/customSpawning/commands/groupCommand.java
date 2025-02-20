package org.milk4lyfe.customSpawning.commands;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.HashMap;
import java.util.List;


public class groupCommand implements CommandExecutor {
    public mobplusplus plugin;
    public groupCommand(mobplusplus plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command,  String s,  String[] args) {
        HashMap<LivingEntity, Integer> group = new HashMap<LivingEntity, Integer>();
        Player player = (Player) commandSender;
        World world = player.getWorld();

        ConfigurationSection groupConfig = plugin.getConfig().getConfigurationSection("group." + args[0] + ".members");
        List<String> entityList = mobplusplus.getListFromConfiguration(plugin, "group." + args[0] + ".members");
        int sum=0;
        for (int i = 0; i< entityList.size(); i++) {
            sum=sum+groupConfig.getInt(entityList.get(i));
        }
        int gridSize = (int) Math.ceil(Math.sqrt(sum));
        int xOffset = 0, zOffset = 0;
        int tag = 0;
        for (int i = 0; i< entityList.size(); i++) {

            for (int j =0; j< groupConfig.getInt(entityList.get(i)); j++) {
                LivingEntity entity = Spawner.spawn(player, world, entityList.get(i), plugin, player.getLocation());
                group.put(entity, tag);
                Location loc = entity.getLocation().add(xOffset, 0, zOffset);
                entity.teleport(loc);
                entity.setAI(false);
                tag++;
                xOffset++;
                if (xOffset >= gridSize) {
                    xOffset=0;
                    zOffset++;
                }
            }


        }
        commandSender.sendMessage(group.toString());

        return true;
    }
}
