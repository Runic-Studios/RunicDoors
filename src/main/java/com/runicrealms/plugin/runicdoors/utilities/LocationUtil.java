package com.runicrealms.plugin.runicdoors.utilities;

import com.runicrealms.plugin.runicdoors.door.DoorBlock;
import com.runicrealms.plugin.runicdoors.door.Viewer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;

public class LocationUtil {
    public static ArrayList<Block> blocksBetweenLocation(Location loc1, Location loc2) {
        ArrayList<Block> blocks = new ArrayList<>();
        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));
        int differenceX = topBlockX - bottomBlockX;
        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));
        int differenceY = topBlockY - bottomBlockY;
        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));
        int differenceZ = topBlockZ - bottomBlockZ;
        for (int x = 0; x <= differenceX; x++) {
            for (int z = 0; z <= differenceZ; z++) {
                for (int y = 0; y <= differenceY; y++) {
                    //probs should air check here but this code is never used
                    blocks.add(loc1.getWorld().getBlockAt(bottomBlockX + x, bottomBlockY + y, bottomBlockZ + z));
                }
            }
        }
        return blocks;
    }

    public static ArrayList<DoorBlock> viewDoorBlocksBetweenLocation(Location loc1, Location loc2, Material material) {
        ArrayList<DoorBlock> blocks = new ArrayList<>();
        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));
        int differenceX = topBlockX - bottomBlockX;
        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));
        int differenceY = topBlockY - bottomBlockY;
        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));
        int differenceZ = topBlockZ - bottomBlockZ;
        for (int x = 0; x <= differenceX; x++) {
            for (int z = 0; z <= differenceZ; z++) {
                for (int y = 0; y <= differenceY; y++) {
                    Block block = loc1.getWorld().getBlockAt(bottomBlockX + x, bottomBlockY + y, bottomBlockZ + z);
                    if (block.getType() == Material.AIR) continue;
                    blocks.add(new DoorBlock(block.getType(), block.getLocation(), block.getBlockData()));

                    Viewer.viewBlock(block, 100, material);
                }
            }
        }
        return blocks;
    }

    public static int countBlocks(Location loc1, Location loc2) {
        int count = 0;
        int topBlockX = (Math.max(loc1.getBlockX(), loc2.getBlockX()));
        int bottomBlockX = (Math.min(loc1.getBlockX(), loc2.getBlockX()));
        int differenceX = topBlockX - bottomBlockX;
        int topBlockY = (Math.max(loc1.getBlockY(), loc2.getBlockY()));
        int bottomBlockY = (Math.min(loc1.getBlockY(), loc2.getBlockY()));
        int differenceY = topBlockY - bottomBlockY;
        int topBlockZ = (Math.max(loc1.getBlockZ(), loc2.getBlockZ()));
        int bottomBlockZ = (Math.min(loc1.getBlockZ(), loc2.getBlockZ()));
        int differenceZ = topBlockZ - bottomBlockZ;
        for (int x = 0; x <= differenceX; x++) {
            for (int z = 0; z <= differenceZ; z++) {
                for (int y = 0; y <= differenceY; y++) {
                    Block block = loc1.getWorld().getBlockAt(bottomBlockX + x, bottomBlockY + y, bottomBlockZ + z);
                    if (block.getType() == Material.AIR) continue;
                    count++;
                }
            }
        }
        return count;
    }
}
