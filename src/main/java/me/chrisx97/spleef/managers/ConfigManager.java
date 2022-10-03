package me.chrisx97.spleef.managers;

import me.chrisx97.spleef.Main;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;

public class ConfigManager
{
    private Main plugin;
    private HashMap<UUID, YamlConfiguration> playerDataCache;
    public ConfigManager(Main plugin) {
        this.plugin = plugin;
    }

    public void addSpleefWin(String uuid) {
        File file = new File(plugin.getDataFolder() + "/playerData/", uuid + ".yml");
        {
            if (file.exists()) {
                FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);
                playerData.set("Spleef.wins", playerData.getInt("Spleef.wins") + 1);
                try {
                    playerData.save(file);
                } catch (IOException e) {
                    plugin.getLogger().severe("Failed to add spleef win for UUID: " + uuid);
                }
            }
        }
    }

    public boolean hasPlayerData(String uuid) {
        File file = new File(plugin.getDataFolder() + "/playerData/" + uuid + ".yml");
        if (file.exists()) {
            return true;
        }
        return false;
    }

    public void setup() {
        File file = new File(plugin.getDataFolder(), "/playerData/");
        file.mkdir();
    }

    public void createPlayerData(String uuid) {
        File file = new File(plugin.getDataFolder() + "/playerData/", uuid + ".yml");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e){
                e.printStackTrace();
                plugin.getLogger().severe("Failed to create new playerData for " + uuid);
            }
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(file);
            playerData.set("Spleef.wins", 0);
            try {
                playerData.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public FileConfiguration getPlayerData(String uuid)
    {
        File file = new File(plugin.getDataFolder() + "/playerData/", uuid + ".yml");
        if (!hasPlayerData(uuid)) return null;
        return YamlConfiguration.loadConfiguration(file);
    }
}
