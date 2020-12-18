package com.runicrealms.runicdoors;

import co.aikar.commands.BukkitCommandManager;
import co.aikar.commands.PaperCommandManager;
import com.runicrealms.runicdoors.commands.DoorCommand;
import com.runicrealms.runicdoors.config.ConfigLoad;
import com.runicrealms.runicdoors.config.Loader;
import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.animations.Animation;
import com.runicrealms.runicdoors.listeners.BlockBreakListener;
import com.runicrealms.runicdoors.listeners.BlockPlaceListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class RunicDoors extends JavaPlugin {
    private static RunicDoors runicDoors;
    private Map<String, Door> doors = new HashMap<>();

    private Map<String, Door> openDoors = new HashMap<>();
    public  File doorConfig;
    private Animation animator;
    public File getDoorConfig() {
        return doorConfig;
    }

    public FileConfiguration getDoorFileConfig() {
        return doorFileConfig;
    }

    public  FileConfiguration doorFileConfig;
    private PaperCommandManager manager;
    private Map<UUID,Door> editors = new HashMap<>();
    /*
    TODO
    "/opendoor id"
    Add command to open and close doors with time parameter,
    so that quest writers can open specific doors.
    TODO
    "/door view"
    add view subcommand to highlight all parts of a door
    that automagically reverts back after a few seconds
    TODO
    push to gitlab so others can review code
     */
    @Override
    public void onEnable() {
        // Plugin startup logic
        runicDoors = this;
        animator = new Animation();
        animator.addAnimations();
        doorConfig = new File(getDataFolder(), "DoorStuff.yml");
        doorFileConfig = YamlConfiguration.loadConfiguration(doorConfig);
        ConfigLoad.loadDoors(doorFileConfig);
        saveDoors();
        manager =  new PaperCommandManager(this);
        manager.registerCommand(new DoorCommand());
        registerEvents(this,new BlockPlaceListener(),new BlockBreakListener());
        BukkitTask doorrange = new RangeOpen().runTaskTimer(this,10,5);
        //gives 5 seconds to load all doors from config, then this runs and closes them 1 every tick!
        BukkitTask loaddoors = new Loader().runTaskLater(this,100);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        saveDoors();
    }

    public void setNodes(Map<String, Door> doors) {
        this.doors = doors;
    }
    public static RunicDoors getRunicDoors() {
        return runicDoors;
    }
    public Map<String, Door> getDoors() {
        return doors;
    }
    public void saveDoors() {
        try {
            doorFileConfig.options().copyDefaults(true);
            doorFileConfig.save(doorConfig);
            Bukkit.broadcastMessage("SAVING DOORS");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void registerEvents(org.bukkit.plugin.Plugin plugin, Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public Map<UUID, Door> getEditors() {
        return editors;
    }

    public Map<String, Door> getOpenDoors() {
        return openDoors;
    }

    public Animation getAnimator() {
        return this.animator;
    }
}
