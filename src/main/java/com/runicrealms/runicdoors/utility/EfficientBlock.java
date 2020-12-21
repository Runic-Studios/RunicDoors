package com.runicrealms.runicdoors.utility;

import com.runicrealms.runicdoors.RunicDoors;
import com.runicrealms.runicdoors.doorStuff.DoorBlock;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class EfficientBlock {
    public static void place(ArrayList<DoorBlock> blocks){
        int max = 10000;
        int delay = 5;
        if(blocks.size()<max){
            for(DoorBlock b:blocks){
                b.getLocation().getBlock().setBlockData(b.getBlockData());
            }
            return;
        }
        for(int i = 0;i<blocks.size()/max;i++){
            int finalI = i;

            new BukkitRunnable(){
                public void run(){
                    for(int loop = finalI *max;loop<blocks.size();loop++ ){
                        blocks.get(loop).getLocation().getBlock().setBlockData(blocks.get(loop).getBlockData(),false);
                    }
                }
            }.runTaskLater(RunicDoors.getRunicDoors(), delay*i);
        }
    }
    public static void placeMaterial(ArrayList<DoorBlock> blocks,Material material){
        int max = 10000;
        int delay = 5;
        if(blocks.size()<max){
            for(DoorBlock b:blocks){
                b.getLocation().getBlock().setType(material,false);
            }
            return;
        }
        for(int i = 0;i<blocks.size()/max;i++){
            int finalI = i;

            new BukkitRunnable(){
                public void run(){
                    for(int loop = finalI *max;loop<blocks.size();loop++ ){
                        blocks.get(loop).getLocation().getBlock().setType(material,false);
                    }
                }
            }.runTaskLater(RunicDoors.getRunicDoors(), delay*i);
        }
    }
    public static void remove(ArrayList<DoorBlock> blocks){
        int max = 10000;
        int delay = 1;
        if(blocks.size()<max){
            for(DoorBlock b:blocks){

                b.getLocation().getBlock().setType(b.getMaterial(),false);
                b.getLocation().getBlock().setBlockData(b.getBlockData());
            }
            return;
        }
        for(int i = 0;i<blocks.size()/max;i++){
            int finalI = i;
            new BukkitRunnable(){
                public void run(){
                    for(int loop = finalI *max;loop<blocks.size();loop++ ){
                        blocks.get(loop).getLocation().getBlock().setType(Material.AIR,false);
                    }
                }
            }.runTaskLater(RunicDoors.getRunicDoors(), delay*i);
        }
    }

    public static void placeClientSideMaterial(ArrayList<DoorBlock> blocks, Material material, Player player){
        int max = 10000;
        int delay = 5;
        BlockData blockData =  Bukkit.createBlockData(material);
        if(blocks.size()<max){
            for(DoorBlock b:blocks){
                player.sendBlockChange(b.getLocation(),blockData);
            }
            return;
        }
        //optimized version for larger block changes
        for(int i = 0;i<blocks.size()/max;i++){
            int finalI = i;

            new BukkitRunnable(){
                public void run(){
                    for(int loop = finalI *max;loop<blocks.size();loop++ ){
                        player.sendBlockChange(blocks.get(loop).getLocation(),blockData);
                    }
                }
            }.runTaskLater(RunicDoors.getRunicDoors(), delay*i);
        }
    }
    public static void placeClientSideMaterial(ArrayList<DoorBlock> blocks, Material material, Player player, Particle particle){
        int max = 10000;
        int delay = 5;
        BlockData blockData =  Bukkit.createBlockData(material);
        if(blocks.size()<max){
            for(DoorBlock b:blocks){
                player.sendBlockChange(b.getLocation(),blockData);
                player.spawnParticle(particle,b.getLocation().clone().add(.5,0.5,.5),1);
            }
            return;
        }
        //optimized version for larger block changes
        for(int i = 0;i<blocks.size()/max;i++){
            int finalI = i;

            new BukkitRunnable(){
                public void run(){
                    for(int loop = finalI *max;loop<blocks.size();loop++ ){
                        player.sendBlockChange(blocks.get(loop).getLocation(),blockData);

                        player.spawnParticle(particle,blocks.get(loop).getLocation().clone().add(.5,0.5,.5),1);
                    }
                }
            }.runTaskLater(RunicDoors.getRunicDoors(), delay*i);
        }
    }
}
