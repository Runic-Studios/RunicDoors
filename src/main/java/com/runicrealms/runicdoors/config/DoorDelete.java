package com.runicrealms.runicdoors.config;

import com.runicrealms.runicdoors.RunicDoors;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class DoorDelete {
    public static void deleteNode(String id, FileConfiguration config) {
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.getInstance(), () -> config.set("Doors." + id, null));
    }
}
