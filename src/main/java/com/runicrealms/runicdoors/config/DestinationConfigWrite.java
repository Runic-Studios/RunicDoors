package com.runicrealms.runicdoors.config;

import com.runicrealms.runicdoors.RunicDoors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.logging.Level;

public class DestinationConfigWrite {

    public static void writeToConfig(String name, Location location) {
        YamlConfiguration config = RunicDoors.inst().getDestinationFileConfig();
        if (location == null || config == null) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Destination saving error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.inst(), () -> {
            config.set(name + ".world", location.getWorld().getName());
            config.set(name + ".pos.X", location.getX());
            config.set(name + ".pos.Y", location.getY());
            config.set(name + ".pos.Z", location.getZ());
            config.set(name + ".pos.pitch", location.getPitch());
            config.set(name + ".pos.yaw", location.getYaw());
            RunicDoors.inst().saveConfiguration(RunicDoors.inst().getDestinationFile(), RunicDoors.inst().getDoorFileConfig());
        });
    }

    public static void deleteFromConfig(String name) {
        YamlConfiguration config = RunicDoors.inst().getDestinationFileConfig();
        if (config == null) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Destination deleting error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.inst(), () -> {
            config.set(name, null);
            RunicDoors.inst().saveConfiguration(RunicDoors.inst().getDestinationFile(), RunicDoors.inst().getDoorFileConfig());
        });
    }
}
