package com.runicrealms.plugin.runicdoors.listeners;

import com.runicrealms.plugin.RunicCore;
import com.runicrealms.plugin.player.CombatType;
import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.portal.Portal;
import net.raidstone.wgevents.events.RegionEnteredEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

/**
 * This listener uses WG regions to detect doors/portals and teleport the player
 *
 * @author H1dden, Skyfallin
 */
public class ActivationListener implements Listener {

    /**
     * Checks if the player has entered the region of a portal entrance/exit
     *
     * @param player        to check
     * @param regionEntered region that the player has entered
     */
    private void checkPortals(Player player, String regionEntered) {
        // Disallow portals for editors
        if (RunicDoors.inst().getEditors().containsKey(player.getUniqueId()))
            return;
        Portal foundPortal = null;
        for (Portal portal : RunicDoors.inst().getPortals().values()) {
            String regionName = portal.getRegionName();
            if (regionEntered.equalsIgnoreCase(regionName)) {
                // Portal region found!
                foundPortal = portal;
                break; // Exit the loop once we find a match
            }
        }
        if (foundPortal == null) return;
        // PvP combat
        // TODO: uses deprecated method, needs fix
        if (RunicCore.getCombatAPI().isInCombat(player.getUniqueId())
                && RunicCore.getCombatAPI().getCombatType(player.getUniqueId()) == CombatType.PLAYER) {
            flingPlayer(player);
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5f, 1.0f);
            player.sendMessage(ChatColor.RED + "You cannot use that in PvP Combat!");
            return;
        }
        // Level requirement
        if (player.getLevel() < foundPortal.getRequiredLevel()) {
            flingPlayer(player);
            player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXTINGUISH_FIRE, 0.5f, 1.0f);
            player.sendMessage(ChatColor.RED + "You must reach a higher level to use this portal!");
            return;
        }
        // Play particle, then teleport, then play particle again
        playPortalEffect(player);
        try {
            player.teleport(RunicDoors.inst().getDestinations().get(foundPortal.getDestination()));
        } catch (Exception e) {
            Bukkit.getLogger().severe("RunicDoors failed to find destination for portal!");
            e.printStackTrace();
        }
        playPortalEffect(player);
    }

    private void flingPlayer(Player player) {
        // Fling player away
        Vector direction = player.getLocation().getDirection().multiply(-2); // Get the opposite of the direction the player is looking
        player.setVelocity(direction);

    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onRegionEntered(RegionEnteredEvent event) {
        if (event.isCancelled()) return;
        Player player = event.getPlayer();
        if (player == null) return;
        checkPortals(player, event.getRegionName());
    }

    private void playPortalEffect(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
        player.getWorld().spawnParticle(Particle.REDSTONE, player.getLocation().add(0, 1, 0),
                10, 0.75f, 0.75f, 0.75f, new Particle.DustOptions(Color.FUCHSIA, 5));
    }

}
