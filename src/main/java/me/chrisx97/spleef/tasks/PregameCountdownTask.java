package me.chrisx97.spleef.tasks;

import me.chrisx97.spleef.game.GameState;
import me.chrisx97.spleef.managers.GameManager;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class PregameCountdownTask extends BukkitRunnable
{
    private GameManager gameManager;
    private int time;
    public PregameCountdownTask(GameManager gameManager) {
        this.gameManager = gameManager;
        this.time = this.gameManager.getGame().getTimePregameLobby();
    }

    @Override
    public void run()
    {
        if (time == 0) {

            cancel();
            gameManager.setState(GameState.INGAME);
            return;
        }
        //if (time == 10 || time <= 5) {
        //    gameManager.getGame().message("&aSpleef starting in &b" + time + " &aseconds.");
        //}
        gameManager.getGame().message("&aSpleef starting in &b" + time + " &aseconds.");
        time--;
    }
}
