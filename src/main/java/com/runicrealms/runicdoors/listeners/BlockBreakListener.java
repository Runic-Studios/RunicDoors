package com.runicrealms.runicdoors.listeners;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.config.ConfigSave;
import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorBlock;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void blockPlaceEvent(BlockBreakEvent blockBreakEvent){
        if(blockBreakEvent.getPlayer()==null)return;
        if(RunicDoors.getRunicDoors().getEditors().containsKey(blockBreakEvent.getPlayer().getUniqueId())){
            Door door =RunicDoors.getRunicDoors().getEditors().get(blockBreakEvent.getPlayer().getUniqueId());
            blockBreakEvent.getPlayer().sendMessage("detected block "+blockBreakEvent.getBlock().getType().toString());
            for(int i = 0;i<door.getConnections().size();i++){
                //blockBreakEvent.getPlayer().sendMessage("comparing" + door.getConnections().get(i).getLocation().toString()+"    to     "+blockBreakEvent.getBlock().getLocation().toString());
                if(door.getConnections().get(i).getLocation().equals(blockBreakEvent.getBlock().getLocation())){
                    door.getConnections().remove(i);
                    blockBreakEvent.getPlayer().sendMessage("removed block "+blockBreakEvent.getBlock().getType().toString());
                    ConfigSave.saveNode(door, RunicDoors.getRunicDoors().doorFileConfig);
                    return;
                }
            }

        }
    }
}
