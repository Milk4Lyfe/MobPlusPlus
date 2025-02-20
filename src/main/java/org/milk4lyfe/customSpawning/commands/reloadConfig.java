package org.milk4lyfe.customSpawning.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.milk4lyfe.customSpawning.mobplusplus;


public class reloadConfig implements CommandExecutor {
    public mobplusplus plugin;
    public reloadConfig(mobplusplus plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings) {
        plugin.reloadConfig();
        commandSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&8[&6Mob++&8] &r Config Reloaded!"));
        return true;
    }
}
