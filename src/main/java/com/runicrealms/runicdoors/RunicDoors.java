package com.runicrealms.runicdoors;

import com.runicrealms.libs.acf.PaperCommandManager;
import com.runicrealms.runicdoors.commands.DoorCMD;
import com.runicrealms.runicdoors.config.ConfigLoad;
import com.runicrealms.runicdoors.config.Loader;
import com.runicrealms.runicdoors.door.Door;
import com.runicrealms.runicdoors.door.DoorHandler;
import com.runicrealms.runicdoors.door.RegionWrapper;
import com.runicrealms.runicdoors.door.animations.Animation;
import com.runicrealms.runicdoors.door.animations.CloseAnimation;
import com.runicrealms.runicdoors.listeners.BlockBreakListener;
import com.runicrealms.runicdoors.listeners.BlockPlaceListener;
import com.runicrealms.runicdoors.listeners.PlayerInteractListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public final class RunicDoors extends JavaPlugin {
    private static RunicDoors runicDoors;
    private final Map<UUID, Door> editors = new HashMap<>();
    private final Set<String> viewing = new HashSet<>();
    private final Map<UUID, RegionWrapper> regionTools = new HashMap<>();
    private final Map<String, Door> openDoors = new HashMap<>();
    public File doorConfig;
    public FileConfiguration doorFileConfig;
    private DoorHandler doorHandler;
    private Map<String, Door> doors = new HashMap<>();
    private Animation animator;
    private CloseAnimation closeAnimator;
    private PaperCommandManager manager;

    public static RunicDoors getRunicDoors() {
        return runicDoors;
    }

    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public Animation getAnimator() {
        return this.animator;
    }

    public CloseAnimation getCloseAnimator() {
        return closeAnimator;
    }

    public File getDoorConfig() {
        return doorConfig;
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
        saveDoors();
    }

    /*
        TODO
        Closing animations
         */
    @Override
    public void onEnable() {
        // Plugin startup logic

        runicDoors = this;
        animator = new Animation();
        closeAnimator = new CloseAnimation();
        doorHandler = new DoorHandler();
        doorConfig = new File(getDataFolder(), "DoorStuff.yml");
        doorFileConfig = YamlConfiguration.loadConfiguration(doorConfig);
        ConfigLoad.loadDoors(doorFileConfig);
        saveDoors();
        manager = new PaperCommandManager(this);
        manager.registerCommand(new DoorCMD());
        registerEvents(this, new BlockPlaceListener(), new BlockBreakListener(), new PlayerInteractListener());
        BukkitTask doorrange = new RangeOpen().runTaskTimer(this, 10, 5);
        //gives 5 seconds to load all doors from config, then this runs and closes them 1 every tick!
        BukkitTask loaddoors = new Loader().runTaskLater(this, 100);

        animator.addAnimations();
        closeAnimator.addAnimations();

    }

    public void saveDoors() {
        try {
            doorFileConfig.options().copyDefaults(true);
            doorFileConfig.save(doorConfig);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNodes(Map<String, Door> doors) {
        this.doors = doors;
    }
}
