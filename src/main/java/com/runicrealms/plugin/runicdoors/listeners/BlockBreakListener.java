package com.runicrealms.plugin.runicdoors.listeners;

import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.config.ConfigSave;
import com.runicrealms.plugin.runicdoors.door.Door;
import com.runicrealms.plugin.runicdoors.door.RegionWrapper;
import com.runicrealms.plugin.runicdoors.door.Viewer;
import com.runicrealms.plugin.runicdoors.utilities.ItemUtil;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreakListener implements Listener {
    @EventHandler
    public void blockPlaceEvent(BlockBreakEvent blockBreakEvent) {

        //region editor code
        if (blockBreakEvent.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR) {

            if (ItemUtil.isRegionSelector(blockBreakEvent.getPlayer().getInventory().getItemInMainHand())) {
                this.regionStuff(blockBreakEvent);
                return;
            }
        }


        if (!RunicDoors.inst().getEditors().containsKey(blockBreakEvent.getPlayer().getUniqueId()))
            return;

        Door door = RunicDoors.inst().getEditors().get(blockBreakEvent.getPlayer().getUniqueId());

        for (int i = 0; i < door.getConnections().size(); i++) {
            //blockBreakEvent.getPlayer().sendMessage("comparing" + door.getConnections().get(i).getLocation().toString()+"    to     "+blockBreakEvent.getBlock().getLocation().toString());
            if (door.getConnections().get(i).getLocation().equals(blockBreakEvent.getBlock().getLocation())) {
                door.getConnections().remove(i);
                ConfigSave.saveNode(door, RunicDoors.inst().getDoorFileConfig());
                Viewer.viewAirBlock(blockBreakEvent.getBlock(), 8, Material.RED_STAINED_GLASS);
                return;
            }


        }
    }

    private void regionStuff(BlockBreakEvent blockBreakEvent) {

        blockBreakEvent.setCancelled(true);
        if (!RunicDoors.inst().getEditors().containsKey(blockBreakEvent.getPlayer().getUniqueId())) {
            blockBreakEvent.getPlayer().sendMessage("You need to select a door");
            return;
        }

        if (RunicDoors.inst().getRegionTools().containsKey(blockBreakEvent.getPlayer().getUniqueId())) {

            RunicDoors.inst().getRegionTools().get(blockBreakEvent.getPlayer().getUniqueId()).setCorner1(blockBreakEvent.getBlock().getLocation());
        } else {
            RunicDoors.inst().getRegionTools().put(blockBreakEvent.getPlayer().getUniqueId(), new RegionWrapper(blockBreakEvent.getPlayer(), blockBreakEvent.getBlock().getLocation(), null, false));
        }
        blockBreakEvent.getPlayer().sendMessage("New corner set at " + blockBreakEvent.getBlock().getX() + " " + blockBreakEvent.getBlock().getY() + " " + blockBreakEvent.getBlock().getZ());
        //door select stuff here
        return;


    }
}
