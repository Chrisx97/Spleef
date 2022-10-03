package me.chrisx97.spleef.managers;

import me.chrisx97.spleef.Main;
import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

public class PlayerRollbackManager
{
    private final Map<UUID, Location> previousLocationMap = new HashMap<>();
    private final Map<UUID, GameMode> previousGamemodeMap = new HashMap<>();
    private final HashMap<UUID, ItemStack[]> previousInventory = new HashMap<>();
    private final Map<UUID, Integer> previousHungerValue = new HashMap<>();
    private final Map<UUID, Integer> previousLevelMap = new HashMap<>();
    private final Map<UUID, Collection<PotionEffect>> previousPotionEffects = new HashMap<>();



    public void cleanup() {
        previousLocationMap.clear();
        previousGamemodeMap.clear();
        previousInventory.clear();
        previousHungerValue.clear();
        previousLevelMap.clear();
        previousPotionEffects.clear();
    }
    public void save(Player player) {
        previousLocationMap.put(player.getUniqueId(), player.getLocation());
        previousGamemodeMap.put(player.getUniqueId(), player.getGameMode());
        previousInventory.put(player.getUniqueId(), player.getInventory().getContents());
        previousHungerValue.put(player.getUniqueId(), player.getFoodLevel());
        previousLevelMap.put(player.getUniqueId(), player.getLevel());
        previousPotionEffects.put(player.getUniqueId(), player.getActivePotionEffects());
    }

    public void saveInventory(Player player) {
        previousInventory.put(player.getUniqueId(), player.getInventory().getContents());
    }

    public void restoreInventory(Player player) {
        player.getInventory().clear();
        player.getInventory().setContents(previousInventory.get(player.getUniqueId()));
        player.updateInventory();
    }

    public void restore(Player player, Main plugin) {

        player.getInventory().clear();
        player.getActivePotionEffects().forEach(potionEffect -> {
            player.removePotionEffect(potionEffect.getType());
        });
        player.addPotionEffects(previousPotionEffects.get(player.getUniqueId()));
        player.getInventory().setContents(previousInventory.get(player.getUniqueId()));
        player.updateInventory();
        GameMode previousGameMode = previousGamemodeMap.get(player.getUniqueId());
        if (previousGameMode != null)
            player.setGameMode(previousGameMode);
        Location previousLocation = previousLocationMap.get(player.getUniqueId());
        if (previousLocation != null)
            player.teleport(previousLocation);
        player.setFoodLevel(previousHungerValue.getOrDefault(player.getUniqueId(), 20));
        player.setLevel(previousLevelMap.getOrDefault(player.getUniqueId(), 0));

        previousHungerValue.remove(player.getUniqueId());
        previousLocationMap.remove(player.getUniqueId());
        previousInventory.remove(player.getUniqueId());
        previousGamemodeMap.remove(player.getUniqueId());
        previousLevelMap.remove(player.getUniqueId());
        previousPotionEffects.remove(player.getUniqueId());

        player.sendMessage(Msg.format("&aAttempted to restore your data."));

        if (plugin == null) return;
        Bukkit.getScheduler().runTaskLater(plugin, () -> player.setFireTicks(0), 2);
    }
}
