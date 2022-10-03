package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.managers.GameManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener
{
    private GameManager gameManager;
    public PlayerQuitListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    void onQuit(PlayerQuitEvent e) {
        if (!this.gameManager.gameRunning()) return;
        if(!this.gameManager.getGame().getPlayers().contains(e.getPlayer().getUniqueId())) return;
        this.gameManager.removeFromGame(e.getPlayer());

    }
}
