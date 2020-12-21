package com.runicrealms.runicdoors.doorStuff;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.utility.EfficientBlock;
import com.runicrealms.runicdoors.utility.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.Set;

public class DoorInteractor {
    public static Door getClosestNode(Player p) {
        //Please run this method ASYNC!!!!
        double closest = -1;
        Door closestNode = null;
        Set<Door> nodes = DoorHandler.getDoorGrid().getNearbyNodes(p.getLocation());
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
    public static void showBlocks(Door door, Material material){
        EfficientBlock.placeMaterial(door.getConnections(),material);
    }
    public static void hideBlocks(Door door){
        EfficientBlock.place(door.getConnections());
        door.setOpen(false);
    }
    public static short newId() {
        int id = new Random().nextInt(10000);
        if (RunicDoors.getRunicDoors().getDoorFileConfig().contains("Nodes." + id) || id == 0) {
            return newId();
        }
        return (short) id;

    }
}
