package com.runicrealms.runicdoors.config;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorBlock;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.logging.Level;

public class ConfigSave {
    public static void saveNode(Door node, ConfigurationSection config) {
        if(node==null||config==null){
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Door saving error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.getRunicDoors(), new Runnable() {
            @Override
            public void run() {

                config.set("Doors"+ "." + node.getId() + ".location.world", node.getLocation().getWorld().getName());
                config.set("Doors" + "." + node.getId() + ".location.x", node.getLocation().getBlockX());
                config.set("Doors" + "." + node.getId() + ".location.y", node.getLocation().getBlockY());
                config.set("Doors" + "." + node.getId() + ".location.z", node.getLocation().getBlockZ());
                config.set("Doors" + "." + node.getId() + ".distance", node.getDistance());
                config.set("Doors" + "." + node.getId() + ".permission", node.getPermission());
                config.set("Doors" + "." + node.getId() + ".animation", node.getAnimation());

                config.set("Doors" + "." + node.getId() + ".timeOpen", node.getTimeOpenDefault());
                int i = 0;
                config.set("Doors." + node.getId() + ".blocks",null);
                for(DoorBlock doorBlock:node.getConnections()){
                    i++;
                    config.set("Doors." + node.getId() + ".blocks."+i+".location.world",node.getConnections().get(i-1).getLocation().getWorld().getName());
                    config.set("Doors." + node.getId() + ".blocks."+i+".location.x",node.getConnections().get(i-1).getLocation().getX());
                    config.set("Doors." + node.getId() + ".blocks."+i+".location.y",node.getConnections().get(i-1).getLocation().getY());

                    config.set("Doors." + node.getId() + ".blocks."+i+".location.z",node.getConnections().get(i-1).getLocation().getZ());
                    config.set("Doors." + node.getId() + ".blocks."+i+".material",node.getConnections().get(i-1).getMaterial().toString());
                    config.set("Doors." + node.getId() + ".blocks."+i+".face",node.getConnections().get(i-1).getBlockFace().toString());
                }

                RunicDoors.getRunicDoors().saveDoors();
            }
        });
    }
}
