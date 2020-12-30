package com.runicrealms.runicdoors;

import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorHandler;
import com.runicrealms.runicdoors.doorStuff.DoorInteractor;
import com.runicrealms.runicdoors.utility.EfficientBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RangeOpen extends BukkitRunnable {
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId())) continue;
            for (Door door : RunicDoors.getRunicDoors().getDoorHandler().getDoorGrid().getNearbyNodes(player.getLocation())) {
                //create a variable since its used twice
                double distance = door.getLocation().distanceSquared(player.getLocation());
                //we skip opening the door if the player doesn't have perms
                if (rejectPlayer(door, player, distance)) continue;


                if (distance < door.getDistance() * door.getDistance()) {

                    if (!RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId() + "")) {
                        door.setOpen(true);
                        //always set open first
                        door.openForPlayer(player);

                    }
                    door.setTimeOpen(door.getTimeOpenDefault());
                }
            }
        }
    }

    public boolean rejectPlayer(Door door, Player player, Double distance) {
        if (distance > Math.pow(door.getDistance(), 2)) return false;
        if (player.hasPermission(door.getPermission()) || door.getPermission().equals("none")) return false;
        //fling player away
        Vector direction = player.getLocation().subtract(door.getLocation()).toVector();
        player.setVelocity(direction.multiply(0.5));
        //if the doors open set some blocks to prevent them passing through
        if (door.getOpen()) {
            EfficientBlock.placeClientSideMaterial(door.getConnections(), Material.BARRIER, player,Particle.BARRIER);
        }
        return true;


    }
}
