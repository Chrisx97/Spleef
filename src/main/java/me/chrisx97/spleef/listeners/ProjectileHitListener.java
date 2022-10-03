package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.managers.GameManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener
{

    private GameManager gameManager;

    public ProjectileHitListener(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @EventHandler
    void onHit(ProjectileHitEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) return;
        if (!(e.getEntity() instanceof Snowball)) return;
        Player player = (Player) e.getEntity().getShooter();
        if (!gameManager.getGame().getPlayers().contains(player.getUniqueId())) return;
        if (e.getHitBlock() != null) {
            if (e.getHitBlock().getType().equals(Material.SNOW_BLOCK)) {
                gameManager.getBlockManager().getBlocksBroken().add(e.getHitBlock());
                e.getHitBlock().setType(Material.AIR);
            }
        }
        Player hitPlayer = (Player) e.getHitEntity();
        if (hitPlayer != null) {
            //hit a player
        }
    }
    public void onDisable() {
        ProjectileHitEvent.getHandlerList().unregister(this);
    }
}
