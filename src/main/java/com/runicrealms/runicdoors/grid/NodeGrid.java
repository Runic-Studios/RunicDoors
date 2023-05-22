package com.runicrealms.runicdoors.grid;

import com.runicrealms.runicdoors.door.Door;
import org.bukkit.Location;

import java.util.HashSet;
import java.util.Set;

public class NodeGrid extends Grid<Door> {
    private short blocksPerBox;

    public NodeGrid(GridBounds bounds, short blocksPerBox) {
        super(bounds);
        this.blocksPerBox = blocksPerBox;
    }

    public GridLocation getGridLocationFromLocation(Location location) {
        return new GridLocation(this,
                (short) (location.getBlockX() / this.blocksPerBox),
                (short) (location.getBlockZ() / this.blocksPerBox));
    }

    public NodeGrid getInstance() {
        return this;
    }

    public Set<Door> getNearbyNodes(Location location) {
        return this.getSurroundingElements(this.getGridLocationFromLocation(location));
    }

    public Set<Door> getNearbyNodes(Location location, String permission) {
        Set<Door> sorted = new HashSet<>();
        for (Door d : this.getSurroundingElements(this.getGridLocationFromLocation(location))) {
            if (d.getPermission().equals(permission)) {
                sorted.add(d);
            }
        }
        return sorted;
    }

    public void insert(Door node) {
        this.insert(this.getGridLocationFromLocation(node.getLocation()), node);
    }

}
