package me.chrisx97.spleef.tasks;

import me.chrisx97.spleef.game.GameState;
import me.chrisx97.spleef.managers.GameManager;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class WaitingForPlayersTask extends BukkitRunnable
{
    int time;
    private GameManager gameManager;
    public WaitingForPlayersTask(GameManager gameManager) {
        this.gameManager = gameManager;
        this.time = this.gameManager.getGame().getTimeWaitingForPlayers();
    }
    @Override
    public void run()
    {



        if (time == 0) {
            if (gameManager.getGame().getMinPlayersToStart() > gameManager.getGame().getPlayers().size()) {
                Bukkit.broadcastMessage(Msg.format("&cNot enough players"));
                Bukkit.broadcastMessage(Msg.format("&cRestarting countdown"));
                cancel();
                gameManager.setState(GameState.WAITING);
                return;
            }
            //Switch state to pregame
            cancel();
            gameManager.setState(GameState.PREGAME);
            return;
        }
        if (time == 120) {
            Bukkit.broadcastMessage(Msg.format("&cSpleef event starting in &62 minutes"));
            Bukkit.broadcastMessage(Msg.format("&7(&e" + gameManager.getGame().getPlayers().size() + "&7/&e" + gameManager.getGame().getMaxPlayers() + "&7)"));
            Bukkit.broadcastMessage(Msg.format("&7Want to join? &c/spleef join"));
        }

        if (gameManager.getGame().getPlayers().size() >= gameManager.getGame().getMinPlayersToStart() && time > 60) {
            time = 60;
            Bukkit.broadcastMessage(Msg.format("&cSpleef has enough players to start"));
            Bukkit.broadcastMessage(Msg.format("&cReducing countdown"));
        }

        if (gameManager.getGame().getPlayers().size() == gameManager.getGame().getMaxPlayers() && time > 10) {
            time = 10;
            Bukkit.broadcastMessage(Msg.format("&cSpleef event has reached maximum players"));
            Bukkit.broadcastMessage(Msg.format("&cReducing countdown"));
        }

        if (time == 60) {
            Bukkit.broadcastMessage(Msg.format("&cSpleef event starting in &61 minute"));
            Bukkit.broadcastMessage(Msg.format("&7(&b" + gameManager.getGame().getPlayers().size() + "&7/&b" + gameManager.getGame().getMaxPlayers() + "&7)"));
            Bukkit.broadcastMessage(Msg.format("&7Want to join? &c/spleef join"));
        }

        if (time == 45 || time == 30) {
            Bukkit.broadcastMessage(Msg.format("&cSpleef event starting in &6" + time + " &cseconds"));
            Bukkit.broadcastMessage(Msg.format("&7(&e" + gameManager.getGame().getPlayers().size() + "&7/&e" + gameManager.getGame().getMaxPlayers() + "&7)"));
            Bukkit.broadcastMessage(Msg.format("&7Want to join? &c/spleef join"));
        }

        if (time == 10 || time <= 5) {
            Bukkit.broadcastMessage(Msg.format("&cSpleef event starting in &6" + time + " &cseconds"));
        }
        time--;
    }
}
