package com.runicrealms.plugin.runicdoors.config;

import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.door.Door;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class Loader extends BukkitRunnable {
    @Override
    public void run() {
        int i = 0;
        for (Door door : RunicDoors.inst().getDoors().values()) {
            BukkitTask run = new BukkitRunnable() {
                @Override
                public void run() {
                    door.closeForPlayer(null);
                }
            }.runTaskLater(RunicDoors.inst(), i);

            i++;
        }
        this.cancel();
    }
}
