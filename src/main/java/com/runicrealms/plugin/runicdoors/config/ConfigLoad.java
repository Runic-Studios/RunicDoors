package com.runicrealms.plugin.runicdoors.config;

import com.runicrealms.plugin.runicdoors.door.Door;
import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.door.DoorBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public class ConfigLoad {
    public static void loadDoors(FileConfiguration config) {
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.inst(), new Runnable() {
            @Override
            public void run() {
                Map<String, Door> nodes = new HashMap<String, Door>();
                if (!config.contains("Doors")) {
                    return;
                }
                ConfigurationSection nodeSection = config.getConfigurationSection("Doors");
                for (String key : nodeSection.getKeys(false)) {
                    try {
                        ConfigurationSection nodeSubSection = config.getConfigurationSection("Doors." + key);
                        ConfigurationSection nodeBlockSection = config.getConfigurationSection("Doors." + key + ".blocks");
                        ArrayList<DoorBlock> blocks = new ArrayList<>();
                        for (String block : nodeBlockSection.getKeys(false)) {
                            Material material;
                            if (Material.getMaterial(nodeBlockSection.getString(block + ".material")) != null) {
                                material = Material.getMaterial(nodeBlockSection.getString(block + ".material"));
                            } else {
                                material = Material.SPONGE;
                            }
                            Location location = new Location(
                                    Bukkit.getWorld(nodeBlockSection.getString(block + ".location.world")),
                                    Double.parseDouble(nodeBlockSection.getString(block + ".location.x")),
                                    Double.parseDouble(nodeBlockSection.getString(block + ".location.y")),
                                    Double.parseDouble(nodeBlockSection.getString(block + ".location.z")));
                            BlockData data = Bukkit.createBlockData(nodeBlockSection.getString(block + ".data"));

                            blocks.add(new DoorBlock(material, location, data));
                        }
                        Location loc = new Location(
                                Bukkit.getWorld(nodeSubSection.getString("location.world")),
                                Double.parseDouble(nodeSubSection.getString("location.x")),
                                Double.parseDouble(nodeSubSection.getString("location.y")),
                                Double.parseDouble(nodeSubSection.getString("location.z")));
                        int knockback = 3;
                        if (nodeSubSection.contains("knockback")) {
                            knockback = nodeSubSection.getInt("knockback");
                        }
                        Door node = new Door(
                                loc,
                                (short) Integer.parseInt(key),
                                nodeSubSection.getString("permission"),
                                nodeSubSection.getInt("distance"),
                                blocks, false, nodeSubSection.getInt("timeOpen"), nodeSubSection.getString("animation"), nodeSubSection.getString("closeanimation"), nodeSubSection.getInt("animationspeed"), nodeSubSection.getString("denymessage"), knockback);
                        //NodeHandler.placeNodeInGrid(node);//puts nodes in one at a time
                        nodes.put(key, node);
                        Bukkit.broadcastMessage(node.getConnections().size() + "");
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                        Bukkit.broadcastMessage("Error loading a door!");
                    }
                }


                RunicDoors.inst().getDoorHandler().placeDoorsInGrid(nodes);
                RunicDoors.inst().setNodes(nodes);
                Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Doors have been loaded!");
            }
        });
    }
    /**/
}
