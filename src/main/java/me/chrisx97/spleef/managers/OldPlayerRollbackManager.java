package me.chrisx97.spleef.managers;

import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class OldPlayerRollbackManager
{
    private GameManager gameManager;
    private List<UUID> playersToRestore;
    private HashMap<UUID, PlayerInventory> playerInventories;

    public OldPlayerRollbackManager(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerInventories = new HashMap<>();
        this.playersToRestore = new ArrayList<>();
    }

    void backupPlayers() {
        playersToRestore.addAll(gameManager.getGame().getPlayers());
        for (UUID uuid : playersToRestore) {
            Player player = Bukkit.getPlayer(uuid);
            if (player == null) continue;
            playerInventories.put(uuid, player.getInventory());
        }
    }

    public void restorePlayers() {
        for (UUID uuid : playersToRestore) { //loop through all players that need to be restored
            Player player = Bukkit.getPlayer(uuid); //get the player instance from UUID in list
            if (player == null) continue; //ignore and continue loop if player is null (logged out and not removed from game?);
            player.getInventory().clear(); //clear player inventory
            player.getInventory().setContents(playerInventories.get(player.getUniqueId()).getContents());
            player.getInventory().setArmorContents(playerInventories.get(player.getUniqueId()).getArmorContents());
            player.sendMessage(Msg.format("&aAttempted to restore player data")); //notify player that we attempted to restore their pre-game data
        }
        cleanup(); //scrub data after we finish restoring players
    }

    private void cleanup()
    {
        playersToRestore.clear(); //clear list of players to restore
        playerInventories.clear(); //clear hashmap that is currently storing old inventories
    }

    //public void restorePlayer(UUID uuid) {
    //    Player player = Bukkit.getPlayer(uuid);
    //    player.getInventory().clear();
    //    //sloppy method for restoring inventory...
    //    //playerInventories.get(uuid).forEach(itemStack -> {
    //    //    player.getInventory().addItem(itemStack);
    //    //});
    //    PlayerInventory inventoryToRestore = playerInventories.get(uuid);
//
    //    inventoryToRestore.forEach(itemStack -> {
    //        player.getInventory().addItem(itemStack);
    //    });
    //    player.sendMessage(Msg.format("&aAttempting to restore player data"));
    //}
}
