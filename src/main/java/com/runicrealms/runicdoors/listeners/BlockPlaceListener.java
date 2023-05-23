package com.runicrealms.runicdoors.listeners;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.config.ConfigSave;
import com.runicrealms.runicdoors.door.Door;
import com.runicrealms.runicdoors.door.DoorBlock;
import com.runicrealms.runicdoors.door.RegionWrapper;
import com.runicrealms.runicdoors.door.Viewer;
import com.runicrealms.runicdoors.utilities.ItemUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlaceListener implements Listener {
    @EventHandler
    public void blockPlaceEvent(BlockPlaceEvent blockPlaceEvent) {
        if (blockPlaceEvent.getPlayer() == null) return;
        if (ItemUtil.isRegionSelector(blockPlaceEvent.getItemInHand())) {
            this.regionStuff(blockPlaceEvent);
            return;
        }
        if (!RunicDoors.getInstance().getEditors().containsKey(blockPlaceEvent.getPlayer().getUniqueId()))
            return;

        //code for region selector

        Door door = RunicDoors.getInstance().getEditors().get(blockPlaceEvent.getPlayer().getUniqueId());

        door.getConnections().add(new DoorBlock(blockPlaceEvent.getBlock().getType(), blockPlaceEvent.getBlock().getLocation(), blockPlaceEvent.getBlock().getBlockData()));


        ConfigSave.saveNode(door, RunicDoors.getInstance().doorFileConfig);
        if (RunicDoors.getInstance().getViewing().contains(door.getId() + "")) {
            blockPlaceEvent.getBlock().setType(Material.YELLOW_STAINED_GLASS);
        }
        Viewer.viewBlock(blockPlaceEvent.getBlock(), 4, Material.GREEN_STAINED_GLASS);
    }


    private void regionStuff(BlockPlaceEvent blockPlaceEvent) {
        if (ItemUtil.isRegionSelector(blockPlaceEvent.getPlayer().getInventory().getItemInMainHand())) {

            blockPlaceEvent.setCancelled(true);
            if (!RunicDoors.getInstance().getEditors().containsKey(blockPlaceEvent.getPlayer().getUniqueId())) {
                blockPlaceEvent.getPlayer().sendMessage("You need to select a door");
                return;
            }

            if (RunicDoors.getInstance().getRegionTools().containsKey(blockPlaceEvent.getPlayer().getUniqueId())) {

                RunicDoors.getInstance().getRegionTools().get(blockPlaceEvent.getPlayer().getUniqueId()).setCorner2(blockPlaceEvent.getBlock().getLocation());
            } else {
                RunicDoors.getInstance().getRegionTools().put(blockPlaceEvent.getPlayer().getUniqueId(), new RegionWrapper(blockPlaceEvent.getPlayer(), null, blockPlaceEvent.getBlock().getLocation(), false));
            }
            blockPlaceEvent.getPlayer().sendMessage("New corner set at " + blockPlaceEvent.getBlock().getX() + " " + blockPlaceEvent.getBlock().getY() + " " + blockPlaceEvent.getBlock().getZ());
            //door select stuff here
            return;
        }

    }
}
