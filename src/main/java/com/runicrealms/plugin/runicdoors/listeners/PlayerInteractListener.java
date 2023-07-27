package com.runicrealms.plugin.runicdoors.listeners;

import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.door.RegionWrapper;
import com.runicrealms.plugin.runicdoors.utilities.ItemUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {
    @EventHandler
    public void playerInteractStuff(PlayerInteractEvent playerInteractEvent) {
        if (playerInteractEvent.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        playerInteractEvent.getPlayer().getInventory().getItemInMainHand();
        if (ItemUtil.isRegionSelector(playerInteractEvent.getItem())) {
            this.regionStuff(playerInteractEvent);
        }
    }


    private void regionStuff(PlayerInteractEvent playerInteractEvent) {
        if (ItemUtil.isRegionSelector(playerInteractEvent.getPlayer().getInventory().getItemInMainHand())) {

            playerInteractEvent.setCancelled(true);
            if (!RunicDoors.inst().getEditors().containsKey(playerInteractEvent.getPlayer().getUniqueId())) {
                playerInteractEvent.getPlayer().sendMessage("You need to select a door");
                return;
            }

            if (RunicDoors.inst().getRegionTools().containsKey(playerInteractEvent.getPlayer().getUniqueId())) {

                RunicDoors.inst().getRegionTools().get(playerInteractEvent.getPlayer().getUniqueId()).setCorner2(playerInteractEvent.getClickedBlock().getLocation());
            } else {
                RunicDoors.inst().getRegionTools().put(playerInteractEvent.getPlayer().getUniqueId(), new RegionWrapper(playerInteractEvent.getPlayer(), null, playerInteractEvent.getClickedBlock().getLocation(), false));
            }
            playerInteractEvent.getPlayer().sendMessage("New corner set at " + playerInteractEvent.getClickedBlock().getX() + " " + playerInteractEvent.getClickedBlock().getY() + " " + playerInteractEvent.getClickedBlock().getZ());
            //door select stuff here
            return;
        }

    }
}
