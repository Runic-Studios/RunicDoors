package com.runicrealms.runicdoors.commands;

import com.runicrealms.libs.acf.BaseCommand;
import com.runicrealms.libs.acf.annotation.*;
import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.config.DestinationConfigWrite;
import com.runicrealms.runicdoors.config.PortalConfigWrite;
import com.runicrealms.runicdoors.portal.Portal;
import com.runicrealms.runicdoors.utilities.LocationUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
@CommandAlias("portal|portals|portalcommand|runicportals|runicportal")
public class PortalCMD extends BaseCommand {
    private static final String PREFIX = ChatColor.RED + "[RunicDoors] ";

    @Subcommand("adddestination|createdestination")
    @CommandCompletion("destinationname|alterra|koldore|")
    @Syntax("<name> &e- create destination by name")
    public void onDestinationCreate(Player player, String id) {
        if (!player.isOp()) {
            player.sendMessage(PREFIX + "No perms");
            return;
        }
        if (RunicDoors.inst().getDestinations().get(id) == null) {
            DestinationConfigWrite.writeToConfig(id, player.getLocation());
            RunicDoors.inst().getDestinations().put(id, player.getLocation());
            player.sendMessage(PREFIX + "Saved " + id + " as a destination");
        } else {
            player.sendMessage(PREFIX + "That Destination is already in our list.");
        }
    }

    @Subcommand("removedestination|deletedestination")
    @CommandCompletion("destinationname|alterra|koldore|")
    @Syntax("<name> &e- select portal by name")
    public void onDestinationRemove(Player player, String id) {
        if (!player.isOp()) {
            player.sendMessage(PREFIX + "No perms");
            return;
        }
        if (RunicDoors.inst().getDestinations().get(id) != null) {
            DestinationConfigWrite.deleteFromConfig(id);
            RunicDoors.inst().getDestinations().remove(id);
            player.sendMessage(PREFIX + "Removed " + id + " as a destination");
        } else {
            player.sendMessage(PREFIX + "That Destination is not in our list.");
        }
    }

    @Subcommand("create")
    @CommandCompletion("name|alterra|koldore destinationname|alterra|koldore|")
    @Syntax("<name> <destination> <requiredLevel> &e- creates portal by name")
    public void onPortalCreate(Player player, String id, String destination, String requiredLevel) {
        if (!player.isOp()) {
            player.sendMessage(PREFIX + "No perms");
            return;
        }
        if (RunicDoors.inst().getDestinations().get(destination) == null) {
            player.sendMessage(PREFIX + "Destination '" + destination + "' does not exist!");
            return;
        }
        if (RunicDoors.inst().getPortals().get(id) != null) {
            player.sendMessage(PREFIX + "Portal '" + id + "' already exists!");
            return;
        }
        if (regionToolCheck(player)) return;
        player.sendMessage(PREFIX + "Creating Basic Portal: " + id);
        Portal portal = new Portal(destination, id, "", Material.AIR, Integer.parseInt(requiredLevel));
        LocationUtil.viewDoorBlocksBetweenLocation(RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner1(), RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner2(), Material.PURPLE_STAINED_GLASS);
        PortalConfigWrite.writeToConfig(portal);
        RunicDoors.inst().getPortals().put(id, portal);
    }

//    @Subcommand("edit|e")
//    @Syntax("<name> &e- exit editing.")
//    public void onPortalEdit(Player player) {
//        if (!player.isOp()) {
//            player.sendMessage(PREFIX + "No perms");
//            return;
//        }
//        if (RunicDoors.getInstance().getGatewayEditorsMap().get(player.getUniqueId()) != null) {
//            player.sendMessage(PREFIX + "No longer editing");
//            RunicDoors.getInstance().getGatewayEditorsMap().remove(player.getUniqueId());
//        } else {
//            player.sendMessage(PREFIX + "You are not in editor mode.");
//        }
//    }

    @Subcommand(("help|h"))
    @CatchUnknown
    @Default
    public void onPortalHelp(Player player) {
        // todo: help command
        player.sendMessage(PREFIX + ChatColor.YELLOW + "Test");
    }

    @Subcommand(("list|ls"))
    public void onPortalList(Player player) {
        player.sendMessage(PREFIX + ChatColor.YELLOW + "Listing all portals:");
        RunicDoors.inst().getPortals().forEach((s, portal) -> player.sendMessage(
                "Name: " + portal.getPortalName() + ", " +
                        "Region: " + portal.getRegionName() + ", " +
                        "Destination: " + portal.getDestination())
        );
    }

    @Subcommand("delete")
    @CommandCompletion("portalname")
    @Syntax("<portal>&e- deletes portal")
    public void onPortalRemove(Player player, String id) {
        if (!player.isOp()) {
            player.sendMessage(PREFIX + "No perms");
            return;
        }
        if (RunicDoors.inst().getPortals().get(id) == null) {
            player.sendMessage(PREFIX + "Portal '" + id + "' doesn't exist!");
            return;
        }
        PortalConfigWrite.deleteFromConfig(id);
        RunicDoors.inst().getPortals().remove(id);
    }

    @Subcommand("setdestination")
    @CommandCompletion("destinationname|alterra|koldore")
    @Syntax("<destination>&e- sets portal destination")
    public void onPortalSetDestination(Player player, String portalId) {
        if (!player.isOp()) {
            player.sendMessage(PREFIX + "No perms");
            return;
        }
        if (!RunicDoors.inst().getEditors().containsKey(player.getUniqueId())) {
            player.sendMessage(PREFIX + "You don't have a portal selected");
            return;
        }
        Portal portal = RunicDoors.inst().getPortals().get(portalId);
        portal.setDestination(portalId);
        PortalConfigWrite.writeToConfig(portal);
    }

    //used letters: s c
    @Subcommand("teleport|tp|go")
    @CommandCompletion("destination|alterra")
    @Syntax("<name> &e- tp by name")
    public void onTeleportByName(Player player, String id) {
        if (!player.isOp()) {
            player.sendMessage(PREFIX + "No perms");
            return;
        }
        if (RunicDoors.inst().getDestinations().get(id) != null) {
            Location selection = RunicDoors.inst().getDestinations().get(id);
            player.teleport(selection);
            player.sendMessage(PREFIX + "Taking you to " + id);
        } else {
            player.sendMessage(PREFIX + "That destination is not in our list.");
        }

    }

    @Subcommand("teleportportal|tpp|goportal")
    @CommandCompletion("portal|alterra")
    @Syntax("<name> &e- tp by name")
    public void onTeleportToPortal(Player player, String id) {
        if (!player.isOp()) {
            player.sendMessage(PREFIX + "No perms");
            return;
        }
        if (RunicDoors.inst().getPortals().get(id) != null) {
//            Location selection = RunicDoors.getInstance().getPortals().get(id).getLocation();
//            player.teleport(selection);
            player.sendMessage(PREFIX + "Taking you to " + id);
        } else {
            player.sendMessage(PREFIX + "That portal is not in our list.");
        }

    }

    private boolean regionToolCheck(Player player) {
        if (!RunicDoors.inst().getRegionTools().containsKey(player.getUniqueId())) {
            player.sendMessage(PREFIX + "You don't have a region selected");
            return true;
        }
        if (RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner1() == null) {
            player.sendMessage(PREFIX + "You don't have a Corner 1");
            return true;
        }
        if (RunicDoors.inst().getRegionTools().get(player.getUniqueId()).getCorner2() == null) {
            player.sendMessage(PREFIX + "You don't have a Corner 2");
            return true;
        }
        return false;
    }
}
