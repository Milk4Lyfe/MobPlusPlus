package org.milk4lyfe.customSpawning;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.milk4lyfe.customSpawning.commands.*;
import org.milk4lyfe.customSpawning.listeners.entityDeathEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public final class mobplusplus extends JavaPlugin {
    private File messagesFile;
    private FileConfiguration messagesConfig;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("Mob++ activated!");
        saveDefaultConfig();
        loadMessagesYAML();

        getCommand("spawn").setExecutor(new Spawner(this));
        getCommand("spawn").setTabCompleter(new SpawnerTabCompleter(this));
        getCommand("mobPlusPlusconfigReload").setExecutor(new reloadConfig(this));
        getCommand("group").setExecutor(new groupCommand(this));
        getCommand("group").setTabCompleter(new groupCommandTabCompleter(this));
        getServer().getPluginManager().registerEvents(new entityDeathEvent(this), this);
        GroupManager thing = new GroupManager(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfig();
    }
    public static List<String> getListFromConfiguration(JavaPlugin plugin, String path) {
        ConfigurationSection entitySection = plugin.getConfig().getConfigurationSection(path);
        List<String> entityList = new ArrayList<>();
        if (entitySection != null) {
            entityList.addAll(entitySection.getKeys(false));
        }
        return entityList;
    }
    public static String getConfigStringValue(JavaPlugin plugin, String path) {

        return plugin.getConfig().getString(path);
    }
    private void loadMessagesYAML() {
        messagesFile = new File(getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            saveResource("messages.yml", false); // Copies from jar if it doesn't exist
        }
        messagesConfig = YamlConfiguration.loadConfiguration(messagesFile);
    }

    public FileConfiguration getMessagesConfig() {
        return messagesConfig;
    }
}
