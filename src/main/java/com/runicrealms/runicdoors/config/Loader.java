package com.runicrealms.runicdoors.config;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.door.Door;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Loader extends BukkitRunnable {
    @Override
    public void run() {
        int i = 0;
        for (Door door : RunicDoors.getRunicDoors().getDoors().values()) {
            BukkitTask run = new BukkitRunnable() {
                @Override
                public void run() {
                    door.closeForPlayer(null);
                }
            }.runTaskLater(RunicDoors.getRunicDoors(), i);

            i++;
        }
        this.cancel();
        Bukkit.broadcastMessage("Loaded and closed all doors!");
    }
}
