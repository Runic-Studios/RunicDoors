package com.runicrealms.runicdoors.door;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;

public class DoorBlock {
    private final Material material;
    private final Location location;
    private final BlockData blockData;

    public DoorBlock(Material material, Location location, BlockData blockData) {
        this.material = material;
        this.location = location;
        this.blockData = blockData;
    }

    public BlockData getBlockData() {
        return this.blockData;
    }

    public Location getLocation() {
        return this.location;
    }

    public Material getMaterial() {
        return this.material;
    }
}
