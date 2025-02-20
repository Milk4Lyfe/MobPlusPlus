package org.milk4lyfe.customSpawning.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.List;
public class Spawner implements CommandExecutor {

    public mobplusplus plugin;
    public List<String> entityList;
    public Spawner(mobplusplus plugin) {
        this.plugin = plugin;

    }
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (args.length ==0 || args.length > 2) {
            commandSender.sendMessage("Incorrect usage. Use /spawn <entity> [quantity]");
            return true;
        }
        int times = 1;
        if (args.length == 2) {

                try {
                    times = Integer.parseInt(args[1]);


                } catch (NumberFormatException e) {
                    commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6Mob++&8] &cError: Invalid quantity! Submit an integer!"));
                    return true;
                }

        }




        Player player = (Player) commandSender;
        World world = player.getWorld();
        if (plugin.getConfig().getConfigurationSection("entities").contains(args[0])) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6Mob++&8] &r&aSpawned entities!"));
            for (int i=0; i< times; i++) {
                spawn(player, world, args[0]);
            }

        }
        else {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6Mob++&8] &cError: Invalid Custom Mob!"));
        }



        return true;
    }
    public void spawn(Player commandSender, World world, String args0) {
        EntityType type;
        try {
            type = EntityType.valueOf(mobplusplus.getConfigStringValue(plugin, "entities." + args0 + ".mob_type").toUpperCase());
        }
        catch (IllegalArgumentException e) {
            commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6Mob++&8] &cError: Mob_type is invalid!"));
            return;
        }

        LivingEntity entity = (LivingEntity) world.spawnEntity(commandSender.getLocation(), type);

        ConfigurationSection entityConfig = plugin.getConfig().getConfigurationSection("entities." + args0);
        entity.setCustomName(ChatColor.translateAlternateColorCodes('&', mobplusplus.getConfigStringValue(plugin, "entities." + args0 + ".name")));
        ConfigurationSection equipmentConfig = plugin.getConfig().getConfigurationSection("entities." + args0 + ".equipment");

        equipEntity(entity, equipmentConfig, commandSender);
        if (entityConfig.contains("health")) {
            setDoubleAttribute(entity, entityConfig.getDouble("health"), commandSender, entityConfig, Attribute.GENERIC_MAX_HEALTH);
        }
        if (entityConfig.contains("damage")) {
            setDoubleAttribute(entity, entityConfig.getDouble("damage"), commandSender, entityConfig, Attribute.GENERIC_ATTACK_DAMAGE);
        }
        if (entityConfig.contains("damage")) {
            setDoubleAttribute(entity, entityConfig.getDouble("movement_speed"), commandSender, entityConfig, Attribute.GENERIC_MOVEMENT_SPEED);
        }
        if (entityConfig.contains("damage")) {
            setDoubleAttribute(entity, entityConfig.getDouble("knockback_resistance"), commandSender, entityConfig, Attribute.GENERIC_KNOCKBACK_RESISTANCE);
        }




    }
    private void equipEntity(LivingEntity entity, ConfigurationSection equipment, Player player) {
        if (equipment == null) return;
        EntityEquipment entityEquipment = entity.getEquipment();
        try{
            if (equipment.contains("helmet")) {
                entityEquipment.setHelmet(new ItemStack(Material.valueOf(equipment.getString("helmet").toUpperCase())));
            }
        }
        catch(IllegalArgumentException e) {
            player.sendMessage("[Mob++] Error: Helmet type is invalid!");
        }
        try {
            if (equipment.contains("chestplate")) {
                entityEquipment.setChestplate(new ItemStack(Material.valueOf(equipment.getString("chestplate").toUpperCase())));
            }
        }
        catch(IllegalArgumentException e) {
            player.sendMessage("[Mob++] Error: Chestplate type is invalid!");
        }
        try {
            if (equipment.contains("leggings")) {
                entityEquipment.setLeggings(new ItemStack(Material.valueOf(equipment.getString("leggings").toUpperCase())));
            }
        }
        catch(IllegalArgumentException e) {
            player.sendMessage("[Mob++] Error: Leggings type is invalid!");
        }
        try {
            if (equipment.contains("boots")) {
                entityEquipment.setBoots(new ItemStack(Material.valueOf(equipment.getString("boots").toUpperCase())));
            }
        }
        catch(IllegalArgumentException e) {
            player.sendMessage("[Mob++] Error: Boot type is invalid!");
        }
        try {
            if (equipment.contains("main_hand")) {
                entityEquipment.setItemInMainHand(new ItemStack(Material.valueOf(equipment.getString("main_hand").toUpperCase())));
            }
        }
        catch(IllegalArgumentException e) {
            player.sendMessage("[Mob++] Error: Main hand type is invalid!");
        }
        try {
            if (equipment.contains("off_hand")) {
                entityEquipment.setItemInOffHand(new ItemStack(Material.valueOf(equipment.getString("off_hand").toUpperCase())));
            }
        }
        catch(IllegalArgumentException e) {
            player.sendMessage("[Mob++] Error: Offhand type is invalid!");
        }
    }
    public void setDoubleAttribute(LivingEntity entity, double health, Player player, ConfigurationSection entityConfig,  Attribute attribute) {
        try {
            entity.getAttribute(attribute).setBaseValue(health);
            entity.setHealth(health);

        }
        catch(IllegalArgumentException e) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6Mob++&8] &cError: Value of attribute is invalid."));
        }
    }
}
