package com.runicrealms.runicdoors.doorStuff;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;

public class DoorBlock {
    private final Material material;
    private final Location location;
    private final BlockFace blockFace;
    public DoorBlock(Material material, Location location, BlockFace blockFace){
        this.material = material;
        this.location = location;
        this.blockFace = blockFace;
    }

    public Location getLocation() {
        return this.location;
    }
    public Material getMaterial(){
        return this.material;
    }

    public BlockFace getBlockFace() {
        return this.blockFace;
    }
}
