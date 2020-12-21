package com.runicrealms.runicdoors.utility;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String colorCode(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
