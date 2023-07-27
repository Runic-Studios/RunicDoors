package com.runicrealms.plugin.runicdoors.config;

import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.portal.Portal;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.logging.Level;

public class PortalConfigWrite {

    public static void writeToConfig(Portal portal) {
        String name = portal.getPortalName();
        YamlConfiguration config = RunicDoors.inst().getPortalFileConfig();
        if (config == null) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Portal saving error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.inst(), () -> {
            // Extra
            if (portal.getTrigger() == null) {
                config.set(name + ".triggerblock", "none");
            } else {
                config.set(name + ".triggerblock", portal.getTrigger().toString());
            }
            config.set(name + ".destination", portal.getDestination());
            config.set(name + ".regionName", portal.getRegionName());
            config.set(name + ".requiredLevel", portal.getRequiredLevel());
            RunicDoors.inst().saveConfiguration(RunicDoors.inst().getPortalFile(), RunicDoors.inst().getPortalFileConfig());
        });
    }

    public static void deleteFromConfig(String name) {
        YamlConfiguration config = RunicDoors.inst().getPortalFileConfig();
        if (config == null) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Portal deleting error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.inst(), () -> {
            config.set(name, null);
            RunicDoors.inst().saveConfiguration(RunicDoors.inst().getPortalFile(), RunicDoors.inst().getPortalFileConfig());
        });
    }
}
