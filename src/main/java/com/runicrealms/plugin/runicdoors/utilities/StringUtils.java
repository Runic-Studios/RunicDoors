package com.runicrealms.plugin.runicdoors.utilities;

import org.bukkit.ChatColor;

public class StringUtils {
    public static String colorCode(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

}
