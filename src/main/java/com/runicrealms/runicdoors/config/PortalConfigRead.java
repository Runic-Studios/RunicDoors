package com.runicrealms.runicdoors.config;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.portal.Portal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

public class PortalConfigRead {

    public static void readPortalsFromConfig(YamlConfiguration portalFileConfig, YamlConfiguration destinationFileConfig) {
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.inst(), () -> {
            Bukkit.getLogger().info("RunicDoors: Loading all portals!");
            for (String key : portalFileConfig.getKeys(false)) {
                Material trigger = Material.AIR;
                if (!portalFileConfig.getString(key + ".triggerblock").equalsIgnoreCase("AIR") && !portalFileConfig.getString(key + ".triggerblock").equals("none")) {
                    trigger = Material.matchMaterial(portalFileConfig.getString(key + ".triggerblock"));
                }
                String destination = portalFileConfig.getString(key + ".destination");
                String regionName = portalFileConfig.getString(key + ".regionName");
                String requiredLevelStr = portalFileConfig.getString(key + ".requiredLevel");
                int requiredLevel = requiredLevelStr != null ? Integer.parseInt(requiredLevelStr) : 0;
                Portal portal = new Portal(destination, key, regionName, trigger, requiredLevel);
                RunicDoors.inst().getPortals().put(key, portal);
            }
            for (String key : destinationFileConfig.getKeys(false)) {
                World world = getWorld(destinationFileConfig, key);
                if (world == null) continue;
                Location location = new Location(world,
                        destinationFileConfig.getDouble(key + ".pos.X"),
                        destinationFileConfig.getDouble(key + ".pos.Y"),
                        destinationFileConfig.getDouble(key + ".pos.Z"));
                location.setYaw((float) destinationFileConfig.getDouble(key + ".pos.yaw"));
                location.setPitch((float) destinationFileConfig.getDouble(key + ".pos.pitch"));
                RunicDoors.inst().getDestinations().put(key, location);
            }
        });
    }

    @Nullable
    private static World getWorld(YamlConfiguration fileConfig, String key) {
        if (fileConfig.getString(key + ".world") == null) {
            Bukkit.broadcastMessage("Null PTR ->" + key);
            return null;
        }
        World world = Bukkit.getServer().getWorld(fileConfig.getString(key + ".world"));
        if (world == null) {
            Bukkit.broadcastMessage(fileConfig.getString(key + ".world") + " Doesn't exist for " + key);
            return null;
        }
        return world;
    }
}
