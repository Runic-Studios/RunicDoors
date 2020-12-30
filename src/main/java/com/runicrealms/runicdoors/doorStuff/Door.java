package com.runicrealms.runicdoors.doorStuff;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.doorStuff.animations.QuadCallable;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

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

    private String closeAnimation;

    private int speed;
    private String sound;

    public Door(Location location, short id, String permission, Integer distance, ArrayList<DoorBlock> connections, Boolean open, int timeOpenDefault,String animation,String closeAnimation,int speed) {
        this.location = location;
        this.id = id;
        this.permission = permission;
        this.distance = distance;
        this.connections = connections;
        this.open = open;
        this.timeOpen = 0;
        this.timeOpenDefault = timeOpenDefault;
        this.animation = animation;
        this.sound = sound;

        this.closeAnimation = closeAnimation;
        //TODO fix swapped stuuff in the future
        this.swappedConnections =null; //connections;
        this.speed = speed;
    }

    public void openForPlayer(Player player) {
        QuadCallable callable =RunicDoors.getRunicDoors().getAnimator().animations.get(this.getAnimation());

        callable.apply(this.getConnections().clone(),null,this,speed);

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
        QuadCallable triCallabl =RunicDoors.getRunicDoors().getCloseAnimator().animations.get(this.getCloseAnimation());

        triCallabl.apply(this.getConnections().clone(),null,this,speed);
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
        return this.animation;
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

    public String getCloseAnimation() {return this.closeAnimation;
    }

    public void setCloseAnimation(String range) {
        this.closeAnimation = range;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getAnimationSpeed() {return this.speed;
    }

}
