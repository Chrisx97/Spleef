package me.chrisx97.spleef.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.Vector;

public class FxUtil
{
    public static void spawnFireworksAroundPlayer(Location playerLocation, int radius, int amount) {
        for (int i = 0; i < amount; i++) {
            Location newLocation = playerLocation.add(new Vector(Math.random()-0.5, 0, Math.random()-0.5).multiply(radius));
            Firework firework = (Firework) playerLocation.getWorld().spawnEntity(newLocation, EntityType.FIREWORK);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();
            fireworkMeta.setPower(1);
            fireworkMeta.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
            firework.setFireworkMeta(fireworkMeta);
        }
    }
}
