package com.runicrealms.plugin.runicdoors.config;

import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.door.Door;
import com.runicrealms.plugin.runicdoors.door.DoorBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Level;

public class ConfigSave {
    public static void saveNode(Door node, ConfigurationSection config) {
        if (node == null || config == null) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Door saving error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.inst(), new Runnable() {
            @Override
            public void run() {

                config.set("Doors" + "." + node.getId() + ".location.world", node.getLocation().getWorld().getName());
                config.set("Doors" + "." + node.getId() + ".location.x", node.getLocation().getX());
                config.set("Doors" + "." + node.getId() + ".location.y", node.getLocation().getY());
                config.set("Doors" + "." + node.getId() + ".location.z", node.getLocation().getZ());
                config.set("Doors" + "." + node.getId() + ".distance", node.getDistance());
                config.set("Doors" + "." + node.getId() + ".knockback", node.getKnockback());
                config.set("Doors" + "." + node.getId() + ".permission", node.getPermission());
                config.set("Doors" + "." + node.getId() + ".animation", node.getAnimation());
                config.set("Doors" + "." + node.getId() + ".closeanimation", node.getCloseAnimation());
                config.set("Doors" + "." + node.getId() + ".animationspeed", node.getAnimationSpeed());
                config.set("Doors" + "." + node.getId() + ".timeOpen", node.getTimeOpenDefault());
                config.set("Doors" + "." + node.getId() + ".denymessage", node.getDenyMessage());
                int i = 0;
                config.set("Doors." + node.getId() + ".blocks", null);
                for (DoorBlock ignored : node.getConnections()) {
                    i++;
                    config.set("Doors." + node.getId() + ".blocks." + i + ".location.world", node.getConnections().get(i - 1).getLocation().getWorld().getName());
                    config.set("Doors." + node.getId() + ".blocks." + i + ".location.x", node.getConnections().get(i - 1).getLocation().getX());
                    config.set("Doors." + node.getId() + ".blocks." + i + ".location.y", node.getConnections().get(i - 1).getLocation().getY());

                    config.set("Doors." + node.getId() + ".blocks." + i + ".location.z", node.getConnections().get(i - 1).getLocation().getZ());
                    config.set("Doors." + node.getId() + ".blocks." + i + ".material", node.getConnections().get(i - 1).getMaterial().toString());
                    config.set("Doors." + node.getId() + ".blocks." + i + ".data", node.getConnections().get(i - 1).getBlockData().getAsString());
                }

                RunicDoors.inst().saveConfiguration(RunicDoors.inst().getDoorFile(), RunicDoors.inst().getDoorFileConfig());
            }
        });
    }
}
