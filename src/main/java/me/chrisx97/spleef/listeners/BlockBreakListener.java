package me.chrisx97.spleef.listeners;

import me.chrisx97.spleef.game.GameState;
import me.chrisx97.spleef.managers.GameManager;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class BlockBreakListener implements Listener
{

    private GameManager gameManager;
    private ItemStack snowballs;
    private Random random;
    private int maxSnowballs = 4;
    public BlockBreakListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.snowballs = new ItemStack(Material.SNOWBALL);
        random = new Random();
    }

    public void onDisable() {
        BlockBreakEvent.getHandlerList().unregister(this);
    }

    //PREGAME
    @EventHandler
    void onBreakPregame(BlockBreakEvent e) {
        if (!gameManager.getGame().getPlayers().contains(e.getPlayer().getUniqueId())) return;
        if (this.gameManager.getState() != GameState.PREGAME) return;
        e.setCancelled(true);
    }

    //INGAME
    @EventHandler
    void onBreak(BlockBreakEvent e) {
        if (!gameManager.getGame().getPlayers().contains(e.getPlayer().getUniqueId())) return;
        if (this.gameManager.getState() != GameState.INGAME) return;
        if (!gameManager.getBlockManager().canBreak(e.getBlock().getType())) {
            e.getPlayer().sendMessage(Msg.format("&cYou are not allowed to break that in spleef."));
            e.setCancelled(true);
            return;
        }
        e.setDropItems(false);
        if (gameManager.canDropItems()) {
            snowballs.setAmount(random.nextInt(maxSnowballs));
            e.getPlayer().getInventory().addItem(snowballs);
        }

        gameManager.getBlockManager().getBlocksBroken().add(e.getBlock());
    }
}
