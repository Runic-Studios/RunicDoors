package com.runicrealms.runicdoors.doorStuff;

import com.runicrealms.runicdoors.RunicDoors;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.scheduler.BukkitRunnable;

public class Viewer {
    public static void viewBlock(Block block, int timeMillis,Material material){
        final BlockData blockData = block.getBlockData();
        block.getLocation().getBlock().setType(material, false);
        new BukkitRunnable() {
            @Override
            public void run() {
            block.setBlockData(blockData);
            }
        }.runTaskLater(RunicDoors.getRunicDoors(), timeMillis);
    }

    public static void viewAirBlock(Block block, int timeMillis, Material material) {
        new BukkitRunnable() {
            @Override
            public void run() {
                block.getLocation().getBlock().setType(material, false);
            }
        }.runTaskLater(RunicDoors.getRunicDoors(), 0);
        new BukkitRunnable() {
            @Override
            public void run() {
                block.setType(Material.AIR);
            }
        }.runTaskLater(RunicDoors.getRunicDoors(), timeMillis);
    }
}
