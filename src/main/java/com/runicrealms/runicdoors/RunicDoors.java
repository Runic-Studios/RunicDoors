package com.runicrealms.runicdoors;

import com.runicrealms.libs.acf.PaperCommandManager;
import com.runicrealms.runicdoors.commands.DoorCMD;
import com.runicrealms.runicdoors.config.ConfigLoad;
import com.runicrealms.runicdoors.config.Loader;
import com.runicrealms.runicdoors.config.PortalConfigRead;
import com.runicrealms.runicdoors.door.Door;
import com.runicrealms.runicdoors.door.DoorHandler;
import com.runicrealms.runicdoors.door.RegionWrapper;
import com.runicrealms.runicdoors.door.animations.Animation;
import com.runicrealms.runicdoors.door.animations.CloseAnimation;
import com.runicrealms.runicdoors.listeners.ActivationListener;
import com.runicrealms.runicdoors.listeners.BlockBreakListener;
import com.runicrealms.runicdoors.listeners.BlockPlaceListener;
import com.runicrealms.runicdoors.listeners.PlayerInteractListener;
import com.runicrealms.runicdoors.portal.Portal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public final class RunicDoors extends JavaPlugin {
    private static RunicDoors instance;
    private final Map<UUID, Door> editors = new HashMap<>();
    private final Set<String> viewing = new HashSet<>();
    private final Map<UUID, RegionWrapper> regionTools = new HashMap<>();
    private final Map<String, Door> openDoors = new HashMap<>();
    private final Map<String, Portal> portals = new HashMap<>();
    private final Map<String, Location> destinations = new HashMap<>();
    private File portalFile;
    private File destinationFile;
    private File doorFile;
    private FileConfiguration doorFileConfig;
    private DoorHandler doorHandler;
    private Map<String, Door> doors = new HashMap<>();
    private Animation animator;
    private CloseAnimation closeAnimator;
    private YamlConfiguration portalFileConfig;
    private YamlConfiguration destinationFileConfig;
    private PaperCommandManager manager;

    public static RunicDoors inst() {
        return instance;
    }

    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public File getPortalFile() {
        return portalFile;
    }

    public File getDestinationFile() {
        return destinationFile;
    }

    public YamlConfiguration getDestinationFileConfig() {
        return destinationFileConfig;
    }

    public YamlConfiguration getPortalFileConfig() {
        return portalFileConfig;
    }

    public Map<String, Portal> getPortals() {
        return portals;
    }

    public Map<String, Location> getDestinations() {
        return destinations;
    }

    public Animation getAnimator() {
        return this.animator;
    }

    public CloseAnimation getCloseAnimator() {
        return closeAnimator;
    }

    public File getDoorFile() {
        return doorFile;
    }

    public FileConfiguration getDoorFileConfig() {
        return doorFileConfig;
    }

    public DoorHandler getDoorHandler() {
        return doorHandler;
    }

    public Map<String, Door> getDoors() {
        return doors;
    }

    public Map<UUID, Door> getEditors() {
        return editors;
    }

    public PaperCommandManager getManager() {
        return manager;
    }

    public Map<String, Door> getOpenDoors() {
        return openDoors;
    }

    public Map<UUID, RegionWrapper> getRegionTools() {
        return regionTools;
    }

    public Set<String> getViewing() {
        return viewing;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveConfiguration(doorFile, doorFileConfig);
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        animator = new Animation();
        closeAnimator = new CloseAnimation();
        doorHandler = new DoorHandler();
        doorFile = new File(getDataFolder(), "DoorStuff.yml");
        portalFile = new File(getDataFolder(), "portals.yml");
        destinationFile = new File(getDataFolder(), "destinations.yml");
        doorFileConfig = YamlConfiguration.loadConfiguration(doorFile);
        portalFileConfig = YamlConfiguration.loadConfiguration(portalFile);
        ConfigLoad.loadDoors(doorFileConfig);
        portalFileConfig = YamlConfiguration.loadConfiguration(portalFile);
        destinationFileConfig = YamlConfiguration.loadConfiguration(destinationFile);
        PortalConfigRead.readPortalsFromConfig(portalFileConfig, destinationFileConfig);
        saveConfiguration(doorFile, doorFileConfig);
        manager = new PaperCommandManager(this);
        manager.registerCommand(new DoorCMD());
        registerEvents
                (
                        this,
                        new BlockPlaceListener(),
                        new BlockBreakListener(),
                        new PlayerInteractListener(),
                        new ActivationListener()
                );
        new RangeOpen().runTaskTimer(this, 10, 5);
        // Gives a few seconds to load all doors from config, then this runs and closes them 1 every tick!
        new Loader().runTaskLater(this, 10 * 20L);

        animator.addAnimations();
        closeAnimator.addAnimations();

    }

    public void saveConfiguration(File file, FileConfiguration configuration) {
        try {
            configuration.options().copyDefaults(true);
            configuration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setNodes(Map<String, Door> doors) {
        this.doors = doors;
    }
}
