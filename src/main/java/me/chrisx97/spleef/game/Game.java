package me.chrisx97.spleef.game;

import me.chrisx97.spleef.Main;
import me.chrisx97.spleef.managers.ConfigManager;
import me.chrisx97.spleef.managers.PlayerRollbackManager;
import me.chrisx97.spleef.utils.FxUtil;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Game
{
    //private ConfigManagerOld configManager;
    private final Main plugin;
    private final int timeWaitingForPlayers = 30;
    private final int timePregameLobby = 10;
    private final int timeRewardingPhase = 10;
    private final int maxPlayers = 8;
    private final int minPlayersToStart = 1;
    private final Material loseBlock = Material.LAVA;
    private final List<UUID> players;
    private UUID winner;
    private final List<UUID> alivePlayers;
    private final List<UUID> spectators;
    private Location gameSpawn;
    private Location spectatorSpawn;
    private final PlayerRollbackManager playerRollbackManager;

    public void setGameSpawn(Location newSpawn) {
        gameSpawn = newSpawn;
    }

    public Location getGameSpawn() {
        return gameSpawn;
    }

    public void setSpectatorSpawn(Location newSpawn) {
        spectatorSpawn = newSpawn;
    }

    public Location getSpectatorSpawn() {
        return spectatorSpawn;
    }

    private final ConfigManager configManager;
    private final ItemStack spleefShovel = new ItemStack(Material.DIAMOND_SHOVEL, 1);
    private final ItemMeta itemMeta = spleefShovel.getItemMeta();
    private final GameState startingState = GameState.WAITING;

    public Game(Main plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>();
        this.alivePlayers = new ArrayList<>();
        this.spectators = new ArrayList<>();
        this.configManager = this.plugin.getConfigManager();
        this.playerRollbackManager = this.plugin.getGameManager().getPlayerRollbackManager();
        assert itemMeta != null;
        itemMeta.setDisplayName(Msg.format("&a&lSpleef Shovel"));
        List<String> lore = new ArrayList<>();
        lore.add("");
        lore.add(Msg.format("&6Use this to dig snow"));
        lore.add(Msg.format("&6during the spleef event."));
        itemMeta.setLore(lore);
        spleefShovel.setItemMeta(itemMeta);
        this.gameSpawn = this.plugin.getConfig().getLocation("Spleef.Spawn-Location");
        this.spectatorSpawn = this.plugin.getConfig().getLocation("Spleef.Spectator-Spawn-Location");
    }

    public List<UUID> getPlayers()
    {
        return players;
    }
    public List<UUID> getAlivePlayers() { return alivePlayers; }

    public GameState getStartingState()
    {
        return startingState;
    }

    public int getTimeWaitingForPlayers()
    {
        return timeWaitingForPlayers;
    }

    public int getMaxPlayers()
    {
        return maxPlayers;
    }

    public int getMinPlayersToStart()
    {
        return minPlayersToStart;
    }

    public int getTimePregameLobby()
    {
        return timePregameLobby;
    }

    public void message(String string) {
        String prefix = "&aSpleef&7>";
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            player.sendMessage(Msg.format(prefix + string));
        }
    }

    public void teleportPlayersToGameSpawn() {
        players.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) return;
            player.teleport(gameSpawn);
        });
    }

    public void sendToSpectatorSpawn(Player player) {
        player.teleport(spectatorSpawn);
    }
    public void removeFromGame(UUID uuid) {
        players.remove(uuid);
        alivePlayers.remove(uuid);
        spectators.remove(uuid);
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        this.playerRollbackManager.restore(player, plugin);
    }

    public void setupGameInventory(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        player.getInventory().addItem(spleefShovel);
    }

    public void setSpectating(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        alivePlayers.remove(player.getUniqueId());
        player.setGameMode(GameMode.SPECTATOR);
        spectators.add(player.getUniqueId());
    }

    public boolean closeToLobby(Player player) {
        if (spectatorSpawn == null){
            return true;
        }

        //avoiding heavy square root call every time a spectator moves...
        return spectatorSpawn.distanceSquared(player.getLocation()) < 5625;
        //if (player.getLocation().distance(spectatorSpawn) < 75) {
        //    return true;
        //}
    }

    public Material getLoseBlock()
    {
        return loseBlock;
    }

    public List<UUID> getSpectators()
    {
        return spectators;
    }

    public int getTimeRewardingPhase()
    {
        return timeRewardingPhase;
    }

    public UUID getWinner()
    {
        return winner;
    }

    public void setWinner(UUID winner)
    {
        this.winner = winner;
    }

    public void doFireworks() {
        for (UUID uuid : players) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            FxUtil.spawnFireworksAroundPlayer(player.getLocation(), 3, 1);
        }
    }
}
