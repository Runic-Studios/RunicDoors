package com.runicrealms.runicdoors.doorStuff.animations;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.doorStuff.Door;
import com.runicrealms.runicdoors.doorStuff.DoorBlock;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Animation {
    public Map<String, TriCallable<ArrayList<DoorBlock>, ArrayList<DoorBlock>, Door>> animations = new HashMap<>();

    public void addAnimations() {
        animations.put("BASIC", new TriCallable<ArrayList<DoorBlock>, ArrayList<DoorBlock>, Door>() {
            @Override
            public void apply(ArrayList<DoorBlock> doorBlocks, ArrayList<DoorBlock> doorBlocks2, Door door) {

                for (DoorBlock b : doorBlocks) {
                    b.getLocation().getBlock().setType(Material.AIR, false);
                }
            }
        });
        animations.put("FADE", new TriCallable<ArrayList<DoorBlock>, ArrayList<DoorBlock>, Door>() {
            @Override
            public void apply(ArrayList<DoorBlock> doorBlocks, ArrayList<DoorBlock> doorBlocks2, Door door) {
                int i = 1;
                for (DoorBlock b : doorBlocks) {

                    BukkitTask runnable = new BukkitRunnable() {
                        @Override
                        public void run() {
                            b.getLocation().getBlock().setType(Material.AIR, false);
                            door.getLocation().getWorld().playSound(door.getLocation(), Sound.BLOCK_STONE_BREAK,1,0);
                        }
                    }.runTaskLater(RunicDoors.getRunicDoors(), i);
                    i++;
                }
            }
        });
        animations.put("PARTICLE", new TriCallable<ArrayList<DoorBlock>, ArrayList<DoorBlock>, Door>() {
            @Override
            public void apply(ArrayList<DoorBlock> doorBlocks, ArrayList<DoorBlock> doorBlocks2, Door door) {
                for (DoorBlock b : doorBlocks) {
                    b.getLocation().getBlock().setType(Material.AIR, false);
                    b.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation().clone().add(0.5, 0.5, 0.5), 5, 0.1, 0.1, 0.1, 0.1, Bukkit.createBlockData(b.getMaterial()));
                }
                door.getLocation().getWorld().playSound(door.getLocation(), Sound.BLOCK_STONE_BREAK,1,0);
            }
        });
        animations.put("SWAP", new TriCallable<ArrayList<DoorBlock>, ArrayList<DoorBlock>, Door>() {
            @Override
            public void apply(ArrayList<DoorBlock> doorBlocks, ArrayList<DoorBlock> doorBlocks2, Door door) {

            }
        });
        animations.put("EXPLODE", new TriCallable<ArrayList<DoorBlock>, ArrayList<DoorBlock>, Door>() {
            @Override
            public void apply(ArrayList<DoorBlock> doorBlocks, ArrayList<DoorBlock> doorBlocks2, Door door) {
                for (DoorBlock b : doorBlocks) {
                    b.getLocation().getBlock().setType(Material.AIR, false);
                }
                door.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, door.getLocation().clone().add(0.5, 1.5, 0.5), 2);
                door.getLocation().getWorld().playSound(door.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE,1,0);

            }
        });
    }
}
