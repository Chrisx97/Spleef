package me.chrisx97.spleef;

import me.chrisx97.spleef.commands.SpleefCommand;
import me.chrisx97.spleef.listeners.PlayerJoinListener;
import me.chrisx97.spleef.listeners.PlayerQuitListener;
import me.chrisx97.spleef.managers.ConfigManager;
import me.chrisx97.spleef.managers.GameManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin
{
    private GameManager gameManager;
    private ConfigManager configManager;

    @Override
    public void onEnable() {
        gameManager = new GameManager(this);
        configManager = new ConfigManager(this);
        configManager.setup();
        saveDefaultConfig();
        getCommand("spleef").setExecutor(new SpleefCommand(gameManager));
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(this.gameManager), this);
    }

    @Override
    public void onDisable() {

    }

    public GameManager getGameManager() {
        return gameManager;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
