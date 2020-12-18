package com.runicrealms.runicdoors.doorStuff;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.doorStuff.animations.TriCallable;
import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.block.data.Directional;

import java.util.ArrayList;

public class Door {
    private String nodeName;
    private Location location;
    private String permission;
    private short id;
    private int distance;
    private ArrayList<DoorBlock> connections;

    private ArrayList<DoorBlock> swappedConnections;
    private Boolean open;


    private int timeOpen;
    private int timeOpenDefault;
    private String animation;
    public Door(Location location, short id, String permission, Integer distance, ArrayList<DoorBlock> connections, Boolean open, int timeOpenDefault,String animation) {
        this.location = location;
        this.id = id;
        this.permission = permission;
        this.distance = distance;
        this.connections = connections;
        this.open = open;
        this.timeOpen = 0;
        this.timeOpenDefault = timeOpenDefault;
        this.animation = animation;
        //TODO fix swapped stuuff in the future
        this.swappedConnections = connections;
    }

    public void openForPlayer(Player player) {
        TriCallable triCallable =RunicDoors.getRunicDoors().getAnimator().animations.get(this.getAnimation());

        triCallable.apply(this.getConnections(),null,this);

        RunicDoors.getRunicDoors().getOpenDoors().put(this.getId() + "", this);
        BukkitTask runlater = new BukkitRunnable() {
            @Override
            public void run() {
                if (!open) this.cancel();
                if (timeOpen <= 0) {
                    closeForPlayer(null);
                    RunicDoors.getRunicDoors().getOpenDoors().remove(getId() + "");
                    this.cancel();
                }
                timeOpen--;

            }
        }.runTaskTimer(RunicDoors.getRunicDoors(), 20, 20);
    }

    public void closeForPlayer(Player player) {
        for (DoorBlock b : this.getConnections()) {
            b.getLocation().getBlock().setType(b.getMaterial());
            if (b.getBlockFace() != BlockFace.SELF) {
                Directional directional = (Directional) b.getLocation().getBlock().getBlockData();
                directional.setFacing(b.getBlockFace());
                b.getLocation().getBlock().setBlockData(directional);
            }
        }
        this.open = false;
    }

    public void delete() {
        //TODO REMOVE NODE
    }

    public String getPermission() {
        return this.permission;
    }

    public Location getLocation() {
        return this.location;
    }

    public short getId() {
        return this.id;
    }

    public int getDistance() {
        return this.distance;
    }

    public ArrayList<DoorBlock> getConnections() {
        return this.connections;
    }

    public void setConnections(ArrayList<DoorBlock> connected) {
        this.connections = connected;
    }

    public int getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(int timeOpen) {
        this.timeOpen = timeOpen;
    }

    public boolean getOpen() {
        return this.open;
    }

    public void setOpen(boolean b) {
        this.open = b;
    }

    public int getTimeOpenDefault() {
        return this.timeOpenDefault;
    }

    public String getAnimation() {
        return animation;
    }

    public void setDistance(Integer integer) {
        this.distance = integer;
    }

    public void setDefaultTime(int number) {
        this.timeOpenDefault = number;
    }

    public void setAnimation(String range) {
        this.animation = range;
    }

    public void setPermission(String range) {
        this.permission = range;
    }
}
