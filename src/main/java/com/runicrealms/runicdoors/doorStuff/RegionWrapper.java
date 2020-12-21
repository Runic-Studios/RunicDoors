package com.runicrealms.runicdoors.doorStuff;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class RegionWrapper {
    private final Player player;
    private Location corner1;
    private Location corner2;



    private ArrayList<Block> blocks;
    private boolean active;
    public RegionWrapper(Player player,Location corner1,Location corner2,boolean active){
        this.player = player;
        this.corner1 = corner1;
        this.corner2 = corner2;
        this.active = false;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Location getCorner1() {
        return this.corner1;
    }

    public void setCorner1(Location corner1) {
        this.corner1 = corner1;
    }

    public Location getCorner2() {
        return this.corner2;
    }

    public void setCorner2(Location corner2) {
        this.corner2 = corner2;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public ArrayList<Block> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(ArrayList<Block> blocks) {
        this.blocks = blocks;
    }
}
