package com.runicrealms.runicdoors.config;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.portal.Portal;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.logging.Level;

public class PortalConfigWrite {

    public static void writeToConfig(Portal portal) {
        String name = portal.getPortalName();
        YamlConfiguration config = RunicDoors.getInstance().getPortalFileConfig();
        if (config == null) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Portal saving error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.getInstance(), () -> {
            // Extra
            if (portal.getTrigger() == null) {
                config.set(name + ".triggerblock", "none");
            } else {
                config.set(name + ".triggerblock", portal.getTrigger().toString());
            }
            config.set(name + ".destination", portal.getDestination());
            config.set(name + ".regionName", portal.getRegionName());
            RunicDoors.getInstance().savePortals();
        });
    }

    public static void deleteFromConfig(String name) {
        YamlConfiguration config = RunicDoors.getInstance().getPortalFileConfig();
        if (config == null) {
            Bukkit.getLogger().log(Level.INFO, "[RunicDoors] Portal deleting error");
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(RunicDoors.getInstance(), () -> {
            config.set(name, null);
            RunicDoors.getInstance().savePortals();
        });
    }
}
