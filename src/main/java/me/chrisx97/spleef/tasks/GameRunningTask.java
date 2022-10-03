package me.chrisx97.spleef.tasks;

import me.chrisx97.spleef.game.GameState;
import me.chrisx97.spleef.managers.GameManager;

import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameRunningTask extends BukkitRunnable
{
    private final GameManager gameManager;
    private int timeRunning;
    public GameRunningTask(GameManager gameManager) {
        this.gameManager = gameManager;
        this.timeRunning = 1;
    }
    private final List<UUID> toRemove = new ArrayList<>();
    @Override
    public void run()
    {

        /**
         * Start giving players snowballs from breaking blocks
         */

        if (timeRunning == 10) {
            gameManager.getGame().message("&cYou may now receive snowballs from breaking blocks.");
            gameManager.setDropItems(true);
        }

        /**
         * Eliminates players that are touching lava
         */

        for (UUID uuid : gameManager.getGame().getAlivePlayers()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            if (player.getLocation().getBlock().getType() == gameManager.getGame().getLoseBlock() && gameManager.getGame().getAlivePlayers().size() > 1) {
                //Player lost
                toRemove.add(uuid);
                gameManager.getGame().message("&c" + player.getName() + " &7has been eliminated. &e" + (gameManager.getGame().getPlayers().size() - 1) + " players remain.");
            }
        }
        for (UUID uuid : toRemove) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            gameManager.setSpectating(player);
        }
        toRemove.clear();

        /**
         * if there is only one player left alive,
         * set them as the winner and end the game
         */

        if (gameManager.getGame().getAlivePlayers().size() == 1) {
            gameManager.getGame().setWinner(gameManager.getGame().getAlivePlayers().get(0));
            gameManager.getGame().setSpectating(gameManager.getGame().getWinner());
            cancel();
            gameManager.setState(GameState.WINNER);
            return;
        }

        /**
         * loop through spectators and send them back to
         * spectator spawn if they have wandered too far away
         */

        for (UUID uuid : gameManager.getGame().getSpectators()) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            if (!gameManager.getGame().closeToLobby(player)) {
                gameManager.getGame().sendToSpectatorSpawn(player);
                player.sendMessage(Msg.format("&cYou wandered too far from the spectator area"));
            }
        }


        timeRunning++;
    }
}
