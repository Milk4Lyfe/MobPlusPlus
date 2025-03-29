package org.milk4lyfe.customSpawning.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.milk4lyfe.customSpawning.group.GroupRegistry;
import org.milk4lyfe.customSpawning.mobplusplus;

import java.util.*;
import java.util.stream.Collectors;

public class groupCommandTabCompleter implements TabCompleter {
    public mobplusplus plugin;
    public groupCommandTabCompleter(mobplusplus plugin) {
        this.plugin = plugin;
    }
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        List<String> thing = new ArrayList<>();
        thing.add("spawn");
        thing.add("march");
        thing.add("delete");
        thing.add("tphere");
        thing.add("toggleAI");
        if (strings.length == 1) {
            return thing;
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("spawn")){
            return mobplusplus.getListFromConfiguration(plugin, "group");
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("march")){
            List<String> stringList = GroupRegistry.returnGroupMapAsList().stream()
                    .map(UUID::toString)  // Convert each UUID to a String
                    .collect(Collectors.toList());
            return stringList;
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("delete")){
            List<String> stringList = GroupRegistry.returnGroupMapAsList().stream()
                    .map(UUID::toString)  // Convert each UUID to a String
                    .collect(Collectors.toList());
            return stringList;
        }
        else if (strings.length == 2 && strings[0].equalsIgnoreCase("tphere")){
            List<String> stringList = GroupRegistry.returnGroupMapAsList().stream()
                    .map(UUID::toString)  // Convert each UUID to a String
                    .collect(Collectors.toList());
            return stringList;
        }else if (strings.length == 2 && strings[0].equalsIgnoreCase("toggleAI")){
            List<String> stringList = GroupRegistry.returnGroupMapAsList().stream()
                    .map(UUID::toString)  // Convert each UUID to a String
                    .collect(Collectors.toList());
            return stringList;
        }
        return Collections.emptyList();
    }
}
