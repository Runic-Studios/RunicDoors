package com.runicrealms.plugin.runicdoors.door.animations;

import com.runicrealms.plugin.runicdoors.RunicDoors;
import com.runicrealms.plugin.runicdoors.door.Door;
import com.runicrealms.plugin.runicdoors.door.DoorBlock;
import com.runicrealms.plugin.runicdoors.utilities.EfficientBlock;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.data.AnaloguePowerable;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CloseAnimation {
    public Map<String, QuadCallable<ArrayList<DoorBlock>, ArrayList<DoorBlock>, Door, Integer>> animations = new HashMap<>();

    public void addAnimations() {

        createParticleAnimation("WARP", Particle.DRAGON_BREATH, Sound.BLOCK_END_PORTAL_FRAME_FILL);

        createParticleAnimation("SMOKE", Particle.SMOKE_LARGE, Sound.BLOCK_REDSTONE_TORCH_BURNOUT);

        createParticleAnimation("VILLAGER", Particle.VILLAGER_HAPPY, Sound.ENTITY_VILLAGER_CELEBRATE);

        createParticleAnimation("BUBBLE", Particle.WATER_WAKE, Sound.AMBIENT_UNDERWATER_ENTER);

        createParticleAnimation("LAVA", Particle.DRIP_LAVA, Sound.BLOCK_LAVA_POP);

        animations.put("BASIC", (doorBlocks, doorBlocks2, door, time) -> {
            for (DoorBlock b : door.getConnections()) {
                b.getLocation().getBlock().setType(b.getMaterial());
                if (b.getBlockData() != null) {
                    b.getLocation().getBlock().setBlockData(b.getBlockData());
                }
            }
        });
        animations.put("OPTIMIZED", (doorBlocks, doorBlocks2, door, time) -> {
            EfficientBlock.place(doorBlocks);
        });
        animations.put("INVERTBASIC", (doorBlocks, doorBlocks2, door, time) -> {

            for (DoorBlock b : doorBlocks) {
                b.getLocation().getBlock().setType(Material.AIR, false);
            }
        });
        animations.put("REMOVEPOWER", (doorBlocks, doorBlocks2, door, time) -> {

            for (DoorBlock b : doorBlocks) {
                if (b.getLocation().getBlock() instanceof AnaloguePowerable) {
                    AnaloguePowerable powerable = (AnaloguePowerable) b.getLocation().getBlock().getBlockData();
                    powerable.setPower(0);
                    b.getLocation().getBlock().setBlockData(powerable, false);
                }
            }
        });
        animations.put("EXPLODE", (doorBlocks, doorBlocks2, door, time) -> {
            for (DoorBlock b : doorBlocks) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        b.getLocation().getBlock().setType(b.getMaterial(), false);
                        b.getLocation().getBlock().setBlockData(b.getBlockData());
                    }
                }.runTaskLater(RunicDoors.inst(), 4);
            }
            door.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_LARGE, door.getLocation().clone().add(0.5, 1.5, 0.5), 2);
            door.getLocation().getWorld().playSound(door.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1, 0);

        });

        animations.put("FADE", (doorBlocks, doorBlocks2, door, time) -> {
            int i = 1;
            for (DoorBlock b : doorBlocks) {

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (door.getOpen()) {
                            this.cancel();
                            return;
                        }
                        b.getLocation().getBlock().setType(b.getMaterial(), false);
                        b.getLocation().getBlock().setBlockData(b.getBlockData());
                        door.getLocation().getWorld().playSound(door.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 0);
                    }
                }.runTaskLater(RunicDoors.inst(), i * time);
                i++;
            }
        });
        animations.put("RANDOM", (doorBlocks, doorBlocks2, door, time) -> {
            int i = 1;
            Collections.shuffle(doorBlocks);
            for (DoorBlock b : doorBlocks) {

                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (door.getOpen()) {
                            this.cancel();
                            return;
                        }
                        b.getLocation().getBlock().setType(b.getMaterial(), false);
                        b.getLocation().getBlock().setBlockData(b.getBlockData());
                        door.getLocation().getWorld().playSound(door.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 0);
                    }
                }.runTaskLater(RunicDoors.inst(), i * time);
                i++;
            }
        });
        animations.put("PARTICLE", (doorBlocks, doorBlocks2, door, time) -> {
            for (DoorBlock b : doorBlocks) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (door.getOpen()) {
                            this.cancel();
                            return;
                        }
                        b.getLocation().getBlock().setType(b.getMaterial(), false);
                        b.getLocation().getBlock().setBlockData(b.getBlockData());
                    }
                }.runTaskLater(RunicDoors.inst(), 4);
                b.getLocation().getWorld().spawnParticle(Particle.BLOCK_CRACK, b.getLocation().clone().add(0.5, 0.5, 0.5), 5, 0.1, 0.1, 0.1, 0.1, b.getBlockData());

                door.getLocation().getWorld().playSound(door.getLocation(), Sound.BLOCK_STONE_BREAK, 1, 0);
            }
        });
        RunicDoors.inst().getManager().getCommandCompletions().registerCompletion("@CloseAnimations", c -> animations.keySet());

    }

    private void createParticleAnimation(String title, Particle particle, Sound sound) {
        animations.put(title, (doorBlocks, doorBlocks2, door, time) -> {
            for (DoorBlock b : doorBlocks) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (door.getOpen()) {
                            this.cancel();
                            return;
                        }
                        b.getLocation().getBlock().setType(b.getMaterial(), false);
                        b.getLocation().getBlock().setBlockData(b.getBlockData());
                    }
                }.runTaskLater(RunicDoors.inst(), 4);
                b.getLocation().getWorld().spawnParticle(particle, b.getLocation().clone().add(0.5, 0.5, 0.5), 8, 0.1, 0.1, 0.1, 0.1);
            }
            door.getLocation().getWorld().playSound(door.getLocation(), sound, 1, 1);
        });
    }
}
