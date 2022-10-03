package me.chrisx97.spleef.managers;

import me.chrisx97.spleef.utils.Msg;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import java.util.ArrayList;
import java.util.List;

public class BlockManager
{
    private GameManager gameManager;
    private List<Material> allowedToBreak;

    private List<Block> blocksBroken;

    public BlockManager(GameManager gameManager) {
        this.gameManager = gameManager;
        blocksBroken = new ArrayList<>();
        allowedToBreak = new ArrayList<>();
        allowedToBreak.add(Material.SNOW_BLOCK);
    }

    public void cleanup() {
        blocksBroken.clear();
    }

    public boolean canBreak(Material material) {
        return allowedToBreak.contains(material);
    }

    //public List<Material> getAllowedToBreak()
    //{
    //    return allowedToBreak;
    //}

    public List<Block> getBlocksBroken() {
        return blocksBroken;
    }

    public void resetBlocks() {
        blocksBroken.forEach(b -> {
            b.setType(Material.SNOW_BLOCK);
            b.getState().update();
        });
    }
}
