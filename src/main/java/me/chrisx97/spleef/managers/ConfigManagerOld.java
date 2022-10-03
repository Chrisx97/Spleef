package me.chrisx97.spleef.managers;

import me.chrisx97.spleef.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class ConfigManagerOld
{

    private Main plugin;
    private File playerData;
    private File configFile;
    private YamlConfiguration config;

    public ConfigManagerOld(Main plugin) {
        this.plugin = plugin;
        this.config = new YamlConfiguration();
        playerData = new File(plugin.getDataFolder(), "playerData");
        plugin.getDataFolder().mkdir();
        playerData.mkdir();
    }

    private boolean dataFolderExists() {
        return playerData.exists();
    }

    public YamlConfiguration getConfig(String name) {
        File file = new File(playerData, name + ".yml");
        if (!file.exists()) {
            return null;
        }
        config = new YamlConfiguration();
        try {
            config.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return config;
    }

        private int getSpleefWins(String uuid) {
        return getConfig(uuid).getInt("Spleef.wins");
    }

    void saveConfig(String name) {
        File file = new File(playerData, name + ".yml");
        if (!file.exists()) return;
        FileConfiguration cfg = null;
        try {
            cfg.load(file);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        try
        {
            cfg.save(file);
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public void addSpleefWin(String uuid) {
        getConfig(uuid).set("Spleef.wins", (getConfig(uuid).getInt("Spleef.wins") + 1));

    }

    public void createConfig(String name) {
        configFile = new File(playerData, name + ".yml");
        if (!dataFolderExists()) {
            configFile.getParentFile().mkdir();
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
            try {
                config.load(configFile);
            } catch (IOException | InvalidConfigurationException e) {
                e.printStackTrace();
            }
        }
    }
}
