package com.runicrealms.runicdoors.door;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.door.animations.QuadCallable;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Door {


    private int knockback = 3;
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
    private String denyMessage;
    private HashMap<UUID, Long> messageCooldown = new HashMap<>();

    public Door(Location location, short id, String permission, Integer distance, ArrayList<DoorBlock> connections, Boolean open, int timeOpenDefault, String animation, String closeAnimation, int speed, String denyMessage, int knockback) {
        this.location = location;
        this.permission = permission;
        this.id = id;
        this.distance = distance;
        this.connections = connections;
        this.open = open;
        this.timeOpen = 0;
        this.timeOpenDefault = timeOpenDefault;
        this.animation = animation;
        this.denyMessage = denyMessage;

        this.closeAnimation = closeAnimation;
        //TODO fix swapped stuuff in the future
        this.swappedConnections = null; //connections;
        this.speed = speed;
        this.knockback = knockback;
    }

    public Door(Location location, short id, String permission, Integer distance, ArrayList<DoorBlock> connections, Boolean open, int timeOpenDefault, String animation, String closeAnimation, int speed, String denyMessage) {
        this.location = location;
        this.permission = permission;
        this.id = id;
        this.distance = distance;
        this.connections = connections;
        this.open = open;
        this.timeOpen = 0;
        this.timeOpenDefault = timeOpenDefault;
        this.animation = animation;
        this.denyMessage = denyMessage;

        this.closeAnimation = closeAnimation;
        //TODO fix swapped stuuff in the future
        this.swappedConnections = null; //connections;
        this.speed = speed;
        this.knockback = 3;
    }

    public void closeForPlayer(Player player) {
        QuadCallable triCallabl = RunicDoors.getRunicDoors().getCloseAnimator().animations.get(this.getCloseAnimation());

        triCallabl.apply(this.getConnections().clone(), null, this, speed);
        this.open = false;
    }

    public void delete() {
        //TODO REMOVE NODE
    }

    public String getAnimation() {
        return this.animation;
    }

    public void setAnimation(String range) {
        this.animation = range;
    }

    public int getAnimationSpeed() {
        return this.speed;
    }

    public String getCloseAnimation() {
        return this.closeAnimation;
    }

    public void setCloseAnimation(String range) {
        this.closeAnimation = range;
    }

    public ArrayList<DoorBlock> getConnections() {
        return this.connections;
    }

    public void setConnections(ArrayList<DoorBlock> connected) {
        this.connections = connected;
    }

    public String getDenyMessage() {
        return denyMessage;
    }

    public void setDenyMessage(String denyMessage) {
        this.denyMessage = denyMessage;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(Integer integer) {
        this.distance = integer;
    }

    public short getId() {
        return this.id;
    }

    public int getKnockback() {
        return knockback;
    }

    public void setKnockback(int knockback) {
        this.knockback = knockback;
    }

    public Location getLocation() {
        return this.location;
    }

    public HashMap<UUID, Long> getMessageCooldown() {
        return messageCooldown;
    }

    public void setMessageCooldown(HashMap<UUID, Long> messageCooldown) {
        this.messageCooldown = messageCooldown;
    }

    public boolean getOpen() {
        return this.open;
    }

    public void setOpen(boolean b) {
        this.open = b;
    }

    public String getPermission() {
        return this.permission;
    }

    public void setPermission(String range) {
        this.permission = range;
    }

    public int getTimeOpen() {
        return timeOpen;
    }

    public void setTimeOpen(int timeOpen) {
        this.timeOpen = timeOpen;
    }

    public int getTimeOpenDefault() {
        return this.timeOpenDefault;
    }

    public void openForPlayer(Player player) {
        QuadCallable callable = RunicDoors.getRunicDoors().getAnimator().animations.get(this.getAnimation());

        callable.apply(this.getConnections().clone(), null, this, speed);

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

    public void setDefaultTime(int number) {
        this.timeOpenDefault = number;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }
}
