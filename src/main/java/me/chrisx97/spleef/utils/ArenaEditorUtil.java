package me.chrisx97.spleef.utils;

import me.chrisx97.spleef.Main;
import me.chrisx97.spleef.managers.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ArenaEditorUtil implements Listener
{
    private Main plugin;
    private List<UUID> inEditor;
    private ItemStack spawnSetTool = new ItemStack(Material.DIAMOND_SHOVEL);
    private ItemStack spectatorSpawnSetTool = new ItemStack(Material.GOLDEN_SHOVEL);
    private ItemMeta itemMeta;
    private List<String> lore;
    public ArenaEditorUtil(Main plugin) {
        this.plugin = plugin;
        inEditor = new ArrayList<>();
        itemMeta = spawnSetTool.getItemMeta();
        itemMeta.setDisplayName(Msg.format("&f&k&l* &a&lSet Spleef Spawn&f&k&l*"));
        lore = new ArrayList<>();
        lore.add("");
        lore.add(Msg.format("&7Right click to set spleef spawn."));
        lore.add(Msg.format("&7>uses player location<"));
        itemMeta.setLore(lore);
        spawnSetTool.setItemMeta(itemMeta);
        lore.clear();
        itemMeta = spectatorSpawnSetTool.getItemMeta();
        itemMeta.setDisplayName(Msg.format("&f&k&l* &cSet Spectator Spawn &f&k&l*"));
        lore.add("");
        lore.add(Msg.format("&7Right click to set spectator spawn."));
        lore.add(Msg.format("&7>uses player location<"));
        itemMeta.setLore(lore);
        spectatorSpawnSetTool.setItemMeta(itemMeta);
        lore.clear();
        itemMeta = null;
    }

    public void addToEditor(UUID uuid) {
        plugin.getGameManager().getPlayerRollbackManager().saveInventory(Bukkit.getPlayer(uuid));
        inEditor.add(uuid);
        setupEditorInventory(uuid);
    }

    public boolean removeFromEditor(UUID uuid) {
        if (inEditor.remove(uuid)) {
            plugin.getGameManager().getPlayerRollbackManager().restoreInventory(Bukkit.getPlayer(uuid));
            return true;
        }
        return false;
    }

    public void setupEditorInventory(UUID uuid) {

        Player player = Bukkit.getPlayer(uuid);
        player.getInventory().clear();
        player.getInventory().addItem(spawnSetTool);
        player.getInventory().addItem(spectatorSpawnSetTool);
    }

    @EventHandler
    void onInteract(PlayerInteractEvent e) {

        if (!inEditor.contains(e.getPlayer().getUniqueId())) return;
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() != Action.RIGHT_CLICK_AIR) return;
        ItemStack heldItem = e.getPlayer().getInventory().getItemInMainHand();
        if (heldItem.getType() == Material.AIR) return;
        if (!heldItem.hasItemMeta()) return;
        if (!heldItem.getItemMeta().hasDisplayName()) return;
        if (heldItem.getItemMeta().getDisplayName().equals(spawnSetTool.getItemMeta().getDisplayName())) {
            plugin.getConfig().set("Spleef.Spawn-Location", e.getPlayer().getLocation());
            plugin.saveConfig();
            e.getPlayer().sendMessage(Msg.format("&aSpleef spawn has been updated."));
        }
        if (heldItem.getItemMeta().getDisplayName().equals(spectatorSpawnSetTool.getItemMeta().getDisplayName())) {
            plugin.getConfig().set("Spleef.Spectator-Spawn-Location", e.getPlayer().getLocation());
            plugin.saveConfig();
            e.getPlayer().sendMessage(Msg.format("&aSpectator spawn has been updated."));
        }

    }
}
