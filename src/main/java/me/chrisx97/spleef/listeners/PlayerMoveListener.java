package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.managers.GameManager;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMoveListener implements Listener
{
    private GameManager gameManager;
    public PlayerMoveListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @EventHandler
    void onMove(PlayerMoveEvent e) {
        if (e.getPlayer().getGameMode() != GameMode.SPECTATOR) return;
        if (e.getTo().getBlockX() == e.getFrom().getBlockX() && e.getTo().getBlockY() == e.getFrom().getBlockY() && e.getTo().getBlockZ() == e.getFrom().getBlockZ()) return; //The player hasn't moved
        if (gameManager.getGame() == null) return;
        if (!gameManager.getGame().getSpectators().contains(e.getPlayer().getUniqueId())) return;
        if (gameManager.getGame().closeToLobby(e.getPlayer())) return;
        //if (e.getPlayer().getGameMode() != GameMode.SPECTATOR) return;
        e.getPlayer().teleport(gameManager.getGame().getSpectatorSpawn());
        e.getPlayer().sendMessage(Msg.format("&cYou wandered too far from the spectator spawn."));
    }

    public void onDisable() {
        PlayerMoveEvent.getHandlerList().unregister(this);
    }
}
