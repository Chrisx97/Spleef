package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.Main;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;


public class PlayerJoinListener implements Listener
{

    private Main plugin;
    public PlayerJoinListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    void onJoin(PlayerJoinEvent e) {
        if (plugin.getConfigManager().hasPlayerData(e.getPlayer().getUniqueId().toString())) return;
        plugin.getConfigManager().createPlayerData(e.getPlayer().getUniqueId().toString());
    }
}
