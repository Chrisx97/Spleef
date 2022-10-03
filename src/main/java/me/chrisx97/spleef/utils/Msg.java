package me.chrisx97.spleef.utils;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class Msg
{
    public static String format(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
}
