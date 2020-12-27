package com.runicrealms.runicdoors.doorStuff;

import com.runicrealms.runicdoors.grid.GridBounds;
import com.runicrealms.runicdoors.grid.GridLocation;
import com.runicrealms.runicdoors.grid.NodeGrid;

import java.util.HashMap;
import java.util.Map;

public class DoorHandler {

    private Map<String, Door> nearbyDoors = new HashMap<String, Door>();
    private NodeGrid grid = new NodeGrid(new GridBounds(-4096, -4096, 4096, 4096), (short) 8);

    public void removeNodeFromGrid(Door node) {
        GridLocation location = grid.getGridLocationFromLocation(node.getLocation());
        if (grid.containsElementInGrid(location, node)) {
            grid.removeElementInGrid(location, node);
            return;
        }
        throw new IllegalArgumentException("Door not in grid!");
    }

    public void placeDoorInGrid(Door node) {
        grid.insert(node);
    }
    public void placeDoorsInGrid(Map<String, Door> nodes) {
        for (Door door: nodes.values()) {
                grid.insert(door);
        }
    }
    public NodeGrid getDoorGrid() {
        return grid;
    }

    public Map<String, Door> getNearbyNodes() {
        return nearbyDoors;
    }
}
