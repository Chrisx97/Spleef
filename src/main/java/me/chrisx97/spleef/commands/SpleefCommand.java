package me.chrisx97.spleef.commands;

import me.chrisx97.spleef.Main;
import me.chrisx97.spleef.game.GameState;
import me.chrisx97.spleef.managers.GameManager;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class SpleefCommand implements TabExecutor
{

    private GameManager gameManager;
    public SpleefCommand(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args)
    {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage(Msg.format("                    &aSpleef"));
            player.sendMessage(Msg.format(""));
            player.sendMessage(Msg.format("&a/spleef join &7- Join the spleef queue."));
            player.sendMessage(Msg.format("&a/spleef leave &7- Leave the spleef queue."));
            player.sendMessage(Msg.format(""));
            if (player.isOp()) {
                player.sendMessage(Msg.format("&a/spleef start &7- Start a spleef game."));
                player.sendMessage(Msg.format("&a/spleef cancel &7- Cancel a spleef game."));
            }
            player.sendMessage(Msg.format("              &7[ &aby Chrisx97 &7]"));
            return true;
        }

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("join")) {
                //Try to join game if one is waiting for players
                if (!gameManager.gameRunning()) {
                    player.sendMessage(Msg.format("&cThere is no game currently running."));
                    return true;
                }
                if (gameManager.getState() != GameState.WAITING) {
                    player.sendMessage(Msg.format("&cThe current game has already started."));
                    return true;
                }

                if (gameManager.getGame().getPlayers().contains(player.getUniqueId())) {
                    player.sendMessage(Msg.format("&cYou are already in the spleef queue!"));
                    return true;
                }
                gameManager.getGame().getPlayers().add(player.getUniqueId());
                //player.sendMessage(Msg.format("&"));
                Bukkit.broadcastMessage(Msg.format("&c" + player.getName() + " &7has joined the spleef queue. &7(&e" +
                        gameManager.getGame().getPlayers().size() + "&7/&e" + gameManager.getGame().getMaxPlayers() + "&7)"));
                return true;
            }

            if (args[0].equalsIgnoreCase("editor") && player.isOp()) {
                if (gameManager.getArenaEditor().removeFromEditor(player.getUniqueId())) {
                    player.sendMessage(Msg.format("&cExited spleef editor mode."));
                    return true;
                }
                gameManager.getArenaEditor().addToEditor(player.getUniqueId());
                player.sendMessage(Msg.format("&aSuccessfully entered spleef editor mode."));
                return true;
            }

            if (args[0].equalsIgnoreCase("leave")) {
                if (gameManager.getState() == null) {
                    player.sendMessage(Msg.format("&cThere is no game currently running."));
                    return true;
                }
                if (gameManager.getState() != GameState.WAITING) {
                    player.sendMessage(Msg.format("&cUnable to leave, game has already started."));
                    return true;
                }
                if (!gameManager.getGame().getPlayers().contains(player.getUniqueId())) {
                    player.sendMessage(Msg.format("&cYou are not in the current spleef queue."));
                    return true;
                }
                Bukkit.broadcastMessage(Msg.format("&c" + player.getName() + " &7has left the spleef queue. &7(&e" +
                        gameManager.getGame().getPlayers().size() + "&7/&e" + gameManager.getGame().getMaxPlayers() + "&7)"));

                gameManager.getGame().removeFromGame(player.getUniqueId());
            }

            if (args[0].equalsIgnoreCase("cancel") && player.isOp()) {
                if (!gameManager.gameRunning())
                {
                    player.sendMessage(Msg.format("&cThere is no game to cancel."));
                    return true;
                }

                GameState state = gameManager.getState();
                switch (state) {
                    case WAITING:
                        Bukkit.broadcastMessage(Msg.format("&7subcommand for 'WAITING' state not yet implemented"));
                        return true;
                    case PREGAME:
                    case INGAME:
                        gameManager.setState(GameState.GAMEOVER);
                        Bukkit.broadcastMessage(Msg.format("&cThe spleef event has been cancelled by an admin"));
                        return true;
                    case WINNER:
                    case GAMEOVER:
                        player.sendMessage(Msg.format("&cThe game is already ending!"));
                        return true;
                }
                return true;
            }

            if (args[0].equalsIgnoreCase("start") && player.isOp()) {
                if (this.gameManager.gameRunning()) {
                    player.sendMessage(Msg.format("&cThere is already a game running!"));
                    return true;
                }
                this.gameManager.startGame();
                return true;
            }
        }

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args)
    {
        return null;
    }
}
