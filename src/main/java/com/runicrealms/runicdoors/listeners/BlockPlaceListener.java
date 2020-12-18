package com.runicrealms.runicdoors.listeners;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.config.ConfigSave;
import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorBlock;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent blockPlaceEvent){
        if(blockPlaceEvent.getPlayer()==null)return;
        if(RunicDoors.getRunicDoors().getEditors().containsKey(blockPlaceEvent.getPlayer().getUniqueId())){
            Door door =RunicDoors.getRunicDoors().getEditors().get(blockPlaceEvent.getPlayer().getUniqueId());
            if(blockPlaceEvent.getBlock().getBlockData() instanceof Directional){
                Directional data = (Directional)blockPlaceEvent.getBlock().getBlockData();
                door.getConnections().add(new DoorBlock(blockPlaceEvent.getBlock().getType(),blockPlaceEvent.getBlock().getLocation(), data.getFacing()));
            }else {
                door.getConnections().add(new DoorBlock(blockPlaceEvent.getBlock().getType(), blockPlaceEvent.getBlock().getLocation(), BlockFace.SELF));
            }
            ConfigSave.saveNode(door, RunicDoors.getRunicDoors().doorFileConfig);
        }
    }
}
