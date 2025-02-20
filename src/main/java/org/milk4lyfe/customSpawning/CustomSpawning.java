package org.milk4lyfe.customSpawning;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import org.milk4lyfe.customSpawning.commands.Spawner;
import org.milk4lyfe.customSpawning.commands.SpawnerTabCompleter;
import org.milk4lyfe.customSpawning.commands.reloadConfig;

import java.util.ArrayList;
import java.util.List;

public final class CustomSpawning extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("CustomSpawner activated!");
        saveDefaultConfig();

        getCommand("spawn").setExecutor(new Spawner(this));
        getCommand("spawn").setTabCompleter(new SpawnerTabCompleter(this));
        getCommand("CSpawn.reloadConfig").setExecutor(new reloadConfig(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
    public static List<String> getListFromConfiguration(JavaPlugin plugin) {
        ConfigurationSection entitySection = plugin.getConfig().getConfigurationSection("entities");
        List<String> entityList = new ArrayList<>();
        if (entitySection != null) {
            entityList.addAll(entitySection.getKeys(false));
        }
        return entityList;
    }
    public static String getConfigStringValue(JavaPlugin plugin, String path) {

        return plugin.getConfig().getString(path);
    }
}
