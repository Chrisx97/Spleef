package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.managers.GameManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamageListener implements Listener
{
    private GameManager gameManager;
    public PlayerDamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    /**
     * cancels fire damage for game players falling in lava
     */

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!gameManager.gameRunning()) return;
        Player player = (Player) e.getEntity();
        if (!gameManager.getGame().getPlayers().contains(player.getUniqueId())) return;

        EntityDamageEvent.DamageCause damageCause = e.getCause();

        if (damageCause == EntityDamageEvent.DamageCause.LAVA || damageCause == EntityDamageEvent.DamageCause.FIRE || damageCause == EntityDamageEvent.DamageCause.FIRE_TICK)
            e.setCancelled(true);
    }

    public void onDisable() {
        EntityDamageEvent.getHandlerList().unregister(this);
    }
}
