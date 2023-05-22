package com.runicrealms.runicdoors;

import com.runicrealms.runicdoors.door.Door;
import com.runicrealms.runicdoors.utilities.EfficientBlock;
import com.runicrealms.runicdoors.utilities.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RangeOpen extends BukkitRunnable {
    public boolean rejectPlayer(Door door, Player player, Double distance) {
        if (distance > Math.pow(door.getDistance(), 2)) return false;
        if (player.hasPermission(door.getPermission()) || door.getPermission().equals("none"))
            return false;
        //fling player away
        Vector direction = player.getLocation().subtract(door.getLocation()).toVector();
        player.setVelocity(direction.multiply((float) 0.1 * door.getKnockback()));
        //if the doors open set some blocks to prevent them passing through
        if (door.getOpen()) {
            EfficientBlock.placeClientSideMaterial(door.getConnections(), Material.BARRIER, player, Particle.BARRIER);
        }

        //don't do all the calculating if there's no message to display
        if (door.getDenyMessage() == null) return true;
        if (door.getDenyMessage().equals("none")) return true;
        //check if players are in cooldown
        //if not then add them
        if (!door.getMessageCooldown().containsKey(player.getUniqueId())) {
            door.getMessageCooldown().put(player.getUniqueId(), System.currentTimeMillis());
            player.sendMessage(StringUtils.colorCode(door.getDenyMessage()));
            return true;
        }
        //has a player been away from the door for 5 seconds?
        if (door.getMessageCooldown().get(player.getUniqueId()) + 5000 < System.currentTimeMillis()) {
            door.getMessageCooldown().put(player.getUniqueId(), System.currentTimeMillis());
            //don't display message
            return true;
        }
        //display message if a player has been away from the door for over 5 seconds
        player.sendMessage(StringUtils.colorCode(door.getDenyMessage()));

        return true;


    }

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
}
