package com.runicrealms.runicdoors.listeners;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.config.ConfigSave;
import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorBlock;
import com.runicrealms.runicdoors.doorStuff.RegionWrapper;
import com.runicrealms.runicdoors.doorStuff.Viewer;
import com.runicrealms.runicdoors.utility.ItemUtil;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Directional;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
        if (blockPlaceEvent.getPlayer() == null) return;
        if(ItemUtil.isRegionSelector(blockPlaceEvent.getItemInHand())) {
            this.regionStuff(blockPlaceEvent);
            return;
        }
        if (!RunicDoors.getRunicDoors().getEditors().containsKey(blockPlaceEvent.getPlayer().getUniqueId())) return;

        //code for region selector

        Door door = RunicDoors.getRunicDoors().getEditors().get(blockPlaceEvent.getPlayer().getUniqueId());

        door.getConnections().add(new DoorBlock(blockPlaceEvent.getBlock().getType(), blockPlaceEvent.getBlock().getLocation(), blockPlaceEvent.getBlock().getBlockData()));


        ConfigSave.saveNode(door, RunicDoors.getRunicDoors().doorFileConfig);
        if(RunicDoors.getRunicDoors().getViewing().contains(door.getId()+"")){
            blockPlaceEvent.getBlock().setType(Material.YELLOW_STAINED_GLASS);
        }
        Viewer.viewBlock(blockPlaceEvent.getBlock(),4, Material.GREEN_STAINED_GLASS);
    }



    private void regionStuff(BlockPlaceEvent blockPlaceEvent) {
        if (ItemUtil.isRegionSelector(blockPlaceEvent.getPlayer().getInventory().getItemInMainHand())) {

            blockPlaceEvent.setCancelled(true);
            if (!RunicDoors.getRunicDoors().getEditors().containsKey(blockPlaceEvent.getPlayer().getUniqueId())){blockPlaceEvent.getPlayer().sendMessage("You need to select a door");return;}

            if(RunicDoors.getRunicDoors().getRegionTools().containsKey(blockPlaceEvent.getPlayer().getUniqueId())){

                RunicDoors.getRunicDoors().getRegionTools().get(blockPlaceEvent.getPlayer().getUniqueId()).setCorner2(blockPlaceEvent.getBlock().getLocation());
            }else{
                RunicDoors.getRunicDoors().getRegionTools().put(blockPlaceEvent.getPlayer().getUniqueId(),new RegionWrapper(blockPlaceEvent.getPlayer(),null,blockPlaceEvent.getBlock().getLocation(),false));
            }
            blockPlaceEvent.getPlayer().sendMessage("New corner set at "+blockPlaceEvent.getBlock().getX()+" "+blockPlaceEvent.getBlock().getY()+" "+blockPlaceEvent.getBlock().getZ());
            //door select stuff here
            return;
        }

    }
}
