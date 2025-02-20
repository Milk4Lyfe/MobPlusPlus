package org.milk4lyfe.customSpawning.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.List;

public class SpawnerTabCompleter implements TabCompleter {
    public mobplusplus plugin;
    public List<String> entityList;
    public SpawnerTabCompleter(mobplusplus plugin) {
        this.plugin = plugin;

    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {


        return mobplusplus.getListFromConfiguration(plugin, "entities");
    }

}
