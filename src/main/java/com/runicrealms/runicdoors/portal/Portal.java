package com.runicrealms.runicdoors.portal;

import org.bukkit.Material;

public class Portal {
    private String destination;
    private String portalName;
    private String regionName;
    private Material trigger;
    private int requiredLevel;

    /**
     * Creates a portal object which teleports a player to another location
     *
     * @param destination   a string identifier of the destination in destinations.yml
     * @param portalName    a string identifier of the portal
     * @param regionName    a string identifier of the trigger region
     * @param trigger       a type of material (usually air) that triggers the portal
     * @param requiredLevel of the player to access the portal
     */
    public Portal(String destination, String portalName, String regionName, Material trigger, int requiredLevel) {
        this.destination = destination;
        this.portalName = portalName;
        this.regionName = regionName != null ? regionName : "";
        this.trigger = trigger;
        this.requiredLevel = requiredLevel;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPortalName() {
        return portalName;
    }

    public void setPortalName(String portalName) {
        this.portalName = portalName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public Material getTrigger() {
        return trigger;
    }

    public void setTrigger(Material trigger) {
        this.trigger = trigger;
    }
}
