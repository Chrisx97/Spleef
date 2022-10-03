package me.chrisx97.spleef.tasks;

import me.chrisx97.spleef.game.GameState;
import me.chrisx97.spleef.managers.GameManager;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public class RewardWinnerTask extends BukkitRunnable
{
    private int time;
    private GameManager gameManager;

    public RewardWinnerTask(GameManager gameManager) {
        this.gameManager = gameManager;
        this.time = this.gameManager.getGame().getTimeRewardingPhase();
    }

    @Override
    public void run()
    {
        if (time == 0) {
            gameManager.setState(GameState.GAMEOVER);
            cancel();
            return;
        }

        Bukkit.broadcastMessage(Msg.format("&6Game ending!"));
        if (time % 2 == 0) {
            gameManager.getGame().doFireworks();
        }
        time--;
    }
}
