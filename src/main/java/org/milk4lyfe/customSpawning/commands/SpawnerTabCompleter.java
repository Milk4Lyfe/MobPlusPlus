package org.milk4lyfe.customSpawning.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.ConfigurationSection;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.milk4lyfe.customSpawning.CustomSpawning;

import java.util.List;

public class SpawnerTabCompleter implements TabCompleter {
    public CustomSpawning plugin;
    public List<String> entityList;
    public SpawnerTabCompleter(CustomSpawning plugin) {
        this.plugin = plugin;

    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {


        return CustomSpawning.getListFromConfiguration(plugin);
    }

}
