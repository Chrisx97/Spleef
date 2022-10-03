package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.managers.GameManager;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropListener implements Listener
{
    private GameManager gameManager;
    public PlayerDropListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    void onDrop(PlayerDropItemEvent e) {
        if (!gameManager.gameRunning()) return;
        if (!gameManager.getGame().getPlayers().contains(e.getPlayer().getUniqueId())) return;
        e.getPlayer().sendMessage(Msg.format("&cYou are not allowed to drop items in spleef."));
        e.setCancelled(!gameManager.playerCanDropItems());
    }

    public void onDisable() {
        PlayerDropItemEvent.getHandlerList().unregister(this);
    }
}
