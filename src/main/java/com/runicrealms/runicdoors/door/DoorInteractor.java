package com.runicrealms.runicdoors.door;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.utilities.EfficientBlock;
import com.runicrealms.runicdoors.utilities.Messages;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.Set;

public class DoorInteractor {
    public static Door getClosestNode(Player p) {
        //Please run this method ASYNC!!!!
        double closest = -1;
        Door closestNode = null;
        Set<Door> nodes = RunicDoors.getInstance().getDoorHandler().getDoorGrid().getNearbyNodes(p.getLocation());
        for (Door entry : nodes) {
            if (closest == -1) {
                closest = p.getLocation().distanceSquared(entry.getLocation());
                closestNode = entry;
            } else if (p.getLocation().distanceSquared(entry.getLocation()) < closest) {
                closest = p.getLocation().distanceSquared(entry.getLocation());
                closestNode = entry;
            }

        }
        if (closest != -1) {
            Messages.sendActionBarMessage(p, "Closest Node " + closestNode.getId() + "Looped " + nodes.size() + " doors");
        } else {
            Messages.sendActionBarMessage(p, "No doors nearby");
        }
        return closestNode;
    }

    public static void showBlocks(Door door, Material material) {
        EfficientBlock.placeMaterial(door.getConnections(), material);
    }

    public static void hideBlocks(Door door) {
        EfficientBlock.place(door.getConnections());
        door.setOpen(false);
    }

    public static short newId() {
        int id = new Random().nextInt(32766);
        if (RunicDoors.getInstance().getDoorFileConfig().contains("Nodes." + id) || id == 0) {
            return newId();
        }
        return (short) id;

    }
}
