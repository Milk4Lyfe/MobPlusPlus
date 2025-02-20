package org.milk4lyfe.customSpawning.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.milk4lyfe.customSpawning.CustomSpawning;

import java.util.ArrayList;
import java.util.List;
public class Spawner implements CommandExecutor {

    public CustomSpawning plugin;
    public List<String> entityList;
    public Spawner(CustomSpawning plugin) {
        this.plugin = plugin;

    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length ==0) {
            commandSender.sendMessage("Incorrect usage. Use /spawn <entity>");
        }

        commandSender.sendMessage("Thing spawned!");
        Player player = (Player) commandSender;
        EntityType type;
        try {
            type = EntityType.valueOf(CustomSpawning.getConfigStringValue(plugin, "entities." + args[0] + ".mob_type").toUpperCase());
        }
        catch (IllegalArgumentException e) {
            commandSender.sendMessage("[Mob++] Aw dangit, invalid mobtype");
            return true;
        }
        LivingEntity entity = (LivingEntity) player.getWorld().spawnEntity(player.getLocation(), type);

        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', CustomSpawning.getConfigStringValue(plugin, "entities." + args[0] + ".name")));

        return true;
    }
}
