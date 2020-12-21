package com.runicrealms.runicdoors.doorStuff;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;

public class DoorBlock {
    private final Material material;
    private final Location location;
    private final BlockData blockData;
    public DoorBlock(Material material, Location location, BlockData blockData){
        this.material = material;
        this.location = location;
        this.blockData = blockData;
    }

    public Location getLocation() {
        return this.location;
    }
    public Material getMaterial(){
        return this.material;
    }

    public BlockData getBlockData() {
        return this.blockData;
    }
}
