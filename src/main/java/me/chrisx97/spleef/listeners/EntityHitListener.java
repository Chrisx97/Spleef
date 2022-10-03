package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.managers.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class EntityHitListener implements Listener
{
    private GameManager gameManager;
    public EntityHitListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @EventHandler
    void onHit(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player) || !(e.getEntity() instanceof Player)) return;
        Player attacker = (Player) e.getDamager();
        Player victim = (Player) e.getEntity();
        if (!gameManager.getGame().getPlayers().contains(attacker.getUniqueId()) || !gameManager.getGame().getPlayers().contains(victim.getUniqueId())) return;
        e.setCancelled(true);
    }

    public void onDisable() {
        EntityDamageByEntityEvent.getHandlerList().unregister(this);
    }
}
