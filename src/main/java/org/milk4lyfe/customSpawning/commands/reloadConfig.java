package org.milk4lyfe.customSpawning.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.milk4lyfe.customSpawning.CustomSpawning;


public class reloadConfig implements CommandExecutor {
    public CustomSpawning plugin;
    public reloadConfig(CustomSpawning plugin) {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand( CommandSender commandSender, Command command, String s, String[] strings) {
        plugin.reloadConfig();
        commandSender.sendMessage("oi mate goodjob you reloaded the config im so proud");
        return true;
    }
}
