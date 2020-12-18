package com.runicrealms.runicdoors.doorStuff;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.utility.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.material.Directional;

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
        for(DoorBlock b:door.getConnections()){
            b.getLocation().getBlock().setType(material);
        }
    }
    public static void hideBlocks(Door door){
        for(DoorBlock b:door.getConnections()){
            b.getLocation().getBlock().setType(b.getMaterial());
            if (b.getLocation().getBlock() instanceof Directional) {
                Directional directional = (Directional) b.getLocation().getBlock();
                directional.setFacingDirection(b.getBlockFace());
            }
        }
    }
    public static short newId() {
        int id = new Random().nextInt(10000);
        if (RunicDoors.getRunicDoors().getDoorFileConfig().contains("Nodes." + id) || id == 0) {
            return newId();
        }
        return (short) id;

    }
}
