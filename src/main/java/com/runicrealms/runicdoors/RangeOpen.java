package com.runicrealms.runicdoors;

import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorHandler;
import com.runicrealms.runicdoors.doorStuff.DoorInteractor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class RangeOpen extends BukkitRunnable {
    @Override
    public void run() {
        for(Player player: Bukkit.getOnlinePlayers()){
            if(RunicDoors.getRunicDoors().getEditors().containsKey(player.getUniqueId()))continue;
            for(Door door:DoorHandler.getDoorGrid().getNearbyNodes(player.getLocation())){
                if(door.getLocation().distanceSquared(player.getLocation())<Math.pow(door.getDistance()+1,2)) {
                    if (!player.hasPermission(door.getPermission())&& !door.getPermission().equals("none")) {
                        Vector direction = player.getLocation().subtract(door.getLocation()).toVector();
                        player.setVelocity(direction.setY(0).multiply(0.5));
                        return;
                    }
                }
                if(door.getLocation().distanceSquared(player.getLocation())<door.getDistance()*door.getDistance()){

                    if(!RunicDoors.getRunicDoors().getOpenDoors().containsKey(door.getId()+"")) {
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
