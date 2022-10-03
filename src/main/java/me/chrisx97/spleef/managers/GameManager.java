package me.chrisx97.spleef.managers;

import me.chrisx97.spleef.Main;
import me.chrisx97.spleef.game.Game;
import me.chrisx97.spleef.game.GameState;
import me.chrisx97.spleef.listeners.*;
import me.chrisx97.spleef.tasks.GameRunningTask;
import me.chrisx97.spleef.tasks.PregameCountdownTask;
import me.chrisx97.spleef.tasks.RewardWinnerTask;
import me.chrisx97.spleef.tasks.WaitingForPlayersTask;
import me.chrisx97.spleef.utils.ArenaEditorUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;


public class GameManager
{
    private Main plugin;
    private Game game;
    private ArenaEditorUtil arenaEditor;
    private GameState state;
    private PlayerRollbackManager playerRollbackManager;
    private PlayerDamageListener playerDamageListener;
    private PlayerMoveListener playerMoveListener;
    private BlockBreakListener blockBreakListener;
    private PlayerDropListener playerDropListener;
    private EntityHitListener entityHitListener;
    private ProjectileHitListener projectileHitListener;
    private BlockManager blockManager;
    private boolean gameDropItems = false;

    public PlayerRollbackManager getPlayerRollbackManager() {
        return playerRollbackManager;
    }


    public GameManager(Main plugin) {
        this.plugin = plugin;
        this.blockManager = new BlockManager(this);
        this.playerRollbackManager = new PlayerRollbackManager();
        this.arenaEditor = new ArenaEditorUtil(plugin);
        plugin.getServer().getPluginManager().registerEvents(this.arenaEditor, plugin);
    }

    void onDisable() {

        this.game = null;
        this.blockManager.cleanup();
        this.playerMoveListener.onDisable();
        this.blockBreakListener.onDisable();
        this.playerDropListener.onDisable();
        this.playerDamageListener.onDisable();
        this.projectileHitListener.onDisable();
        this.entityHitListener.onDisable();
        this.playerRollbackManager.cleanup();
        this.state = null;
        this.gameDropItems = false;
    }

    public BlockManager getBlockManager() {
        return blockManager;
    }

    public void setState(GameState newState) {
        this.state = newState;
        switch(this.state) {

            case WAITING:
                //countdown waiting for players
                new WaitingForPlayersTask(plugin.getGameManager()).runTaskTimer(plugin, 0, 20); //start countdown, waiting for players
                break;

            case PREGAME:
                //backup player data & teleport everyone into spleef lobby, start countdown for game
                this.playerMoveListener = new PlayerMoveListener(this);
                this.blockBreakListener = new BlockBreakListener(this);
                this.playerDamageListener = new PlayerDamageListener(this);
                this.playerDropListener = new PlayerDropListener(this);
                this.entityHitListener = new EntityHitListener(this);
                this.projectileHitListener = new ProjectileHitListener(this);
                plugin.getServer().getPluginManager().registerEvents(this.playerMoveListener, plugin); //register player move event listener
                plugin.getServer().getPluginManager().registerEvents(this.blockBreakListener, plugin); //register block break event listener
                plugin.getServer().getPluginManager().registerEvents(this.entityHitListener, plugin);
                plugin.getServer().getPluginManager().registerEvents(this.projectileHitListener, plugin);
                plugin.getServer().getPluginManager().registerEvents(this.playerDropListener, plugin);
                plugin.getServer().getPluginManager().registerEvents(this.playerDamageListener, plugin);
                this.game.getPlayers().forEach(uuid -> { //loop through all players in the game
                    Player player = Bukkit.getPlayer(uuid); //player variable
                    player.closeInventory(); //close their inventories
                    this.playerRollbackManager.save(player); //save player locations, levels, hunger, and gamemode
                    player.getInventory().clear(); //clear inventories
                    player.teleport(this.game.getGameSpawn()); //teleport to lobby spawn

                    ///HEAL AND SHIT
                    player.getActivePotionEffects().forEach(potionEffect -> {
                        player.removePotionEffect(potionEffect.getType());
                    });
                    player.setHealth(20);
                    player.setFoodLevel(20);
                    player.setFireTicks(0);
                    player.getActivePotionEffects().clear();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 99999, 0));

                    game.setupGameInventory(uuid); //setup game inventories (spleef shovel?)
                });
                new PregameCountdownTask(plugin.getGameManager()).runTaskTimer(plugin, 0, 20);//start pre-game countdown
                break;

            case INGAME:
                //teleport everyone to spleef spawn & start the game
                game.getAlivePlayers().addAll(game.getPlayers()); //adds all game players to "alive" list
                game.teleportPlayersToGameSpawn();
                new GameRunningTask(plugin.getGameManager()).runTaskTimer(plugin, 0, 20); //start game running task (runs every second)
                break;

            case WINNER:
                //reward the winner(s), restore the arena
                Player player = Bukkit.getPlayer(game.getWinner()); //winner player variable
                if (player != null) {
                    game.message("&c" + player.getName() + " &7has won the spleef event!"); //inform all spleef players who has won
                    if (!plugin.getConfigManager().hasPlayerData(player.getUniqueId().toString())) {
                        plugin.getConfigManager().createPlayerData(player.getUniqueId().toString());
                    }
                    //plugin.getConfigManager().getPlayerData(player.getUniqueId().toString()).
                } else {
                    game.message("&cSomething went wrong, we don't have a winner..."); //something went wrong lmao
                }
                new RewardWinnerTask(this).runTaskTimer(plugin, 0, 20); //reward money, play special fx
                blockManager.resetBlocks(); //Rollback broken blocks
                game.teleportPlayersToGameSpawn();
                break;

            case GAMEOVER:
                //restore player data and end the game.
                this.game.getPlayers().forEach(uuid -> {//loop through all game players
                    playerRollbackManager.restore(Bukkit.getPlayer(uuid), plugin); //restore player locations, levels, hunger, and gamemode
                });

                if (game.getWinner() != null) { //add a win if there is a winner (game being cancelled can cause a no winner case)
                    plugin.getConfigManager().addSpleefWin(game.getWinner().toString());
                }
                //inventory rewards must go here // after restoring!!!!!

                onDisable(); //reset everything so another game can be started
                break;
        }
    }

    public void setSpectating(Player player) {
        this.game.setSpectating(player.getUniqueId());
        player.teleport(this.game.getSpectatorSpawn());
    }


    public void removeFromGame(Player player) {
        this.game.removeFromGame(player.getUniqueId());
    }

    public GameState getState() {
        return state;
    }

    public boolean playerCanDropItems() {
        return (state == GameState.WAITING);
    }

    public boolean gameRunning() {
        return (this.game != null);
    }

    public void startGame() {
        this.playerRollbackManager = new PlayerRollbackManager();
        this.game = new Game(plugin);
        setState(game.getStartingState());
    }

    public void cancelGame() {
        if (!gameRunning()) return;
        if (this.state != GameState.WAITING) {
            this.game.getPlayers().forEach(uuid -> {
                removeFromGame(Bukkit.getPlayer(uuid));
            });
        }
        Bukkit.getScheduler().cancelTasks(plugin);
        Bukkit.broadcastMessage("&cThe game has been cancelled by an admin");
        onDisable();
    }

    public Game getGame() {
        return this.game;
    }

    public boolean canDropItems()
    {
        return gameDropItems;
    }

    public void setDropItems(boolean dropItems)
    {
        this.gameDropItems = dropItems;
    }

    public ArenaEditorUtil getArenaEditor()
    {
        return arenaEditor;
    }
}
