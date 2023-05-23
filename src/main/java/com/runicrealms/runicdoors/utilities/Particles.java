package com.runicrealms.runicdoors.utilities;

import com.runicrealms.runicdoors.RunicDoors;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class Particles {
    public static void createParticle(Location l, Color c, int size) {
        l.getWorld().spawnParticle(Particle.REDSTONE, l, 1, new Particle.DustOptions(c, 1));
    }

    public static void createParticle(Location l, Particle p, int size) {
        if (p.equals(Particle.REDSTONE)) {
            l.getWorld().spawnParticle(Particle.REDSTONE, l, 1, new Particle.DustOptions(Color.RED, 1));
        } else {
            l.getWorld().spawnParticle(p, l, size);
        }
    }

    public static void drawSphere(Location l, double r, Color c, double resolution) {
        for (double phi = 0; phi <= Math.PI; phi += Math.PI / resolution) {
            for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / resolution * 2) {
                double x = r * Math.cos(theta) * Math.sin(phi);
                double y = r * Math.cos(phi) + 1.5;
                double z = r * Math.sin(theta) * Math.sin(phi);

                l.add(x, y, z);
                createParticle(l, c, 1);
                l.subtract(x, y, z);
            }
        }
    }

    public static void drawLine(Location point1, Location point2, double space, Color c) {
        World world = point1.getWorld();
        if (point2.getWorld().equals(world)) {
            //Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
            double distance = point1.distance(point2);
            Vector p1 = point1.toVector();
            Vector p2 = point2.toVector();
            Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
            double length = 0;
            for (; length < distance; p1.add(vector)) {
                createParticle(new Location(world, p1.getX(), p1.getY(), p1.getZ()), c, 1);
                length += space;
            }

        }

    }

    public static void drawPinPoint(Location location, Color ballColor, Color stickColor) {
        BukkitTask task = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {

                if (i > 20) this.cancel();
                i++;
                Particles.drawSphere(location, 0.5, ballColor, 5);
                Particles.drawLine(location, new Location(location.getWorld(), location.getX(), location.getY() + 2, location.getZ()), .3, stickColor);
            }
        }.runTaskTimer(RunicDoors.inst(), 0, 5);
    }

    public static void drawCube(Location l, Color c) {
        for (Integer integer : Arrays.<Integer>asList(new Integer[]{Integer.valueOf(45), Integer.valueOf(-45), Integer.valueOf(135), Integer.valueOf(-135)})) {
            for (Integer integer1 : Arrays.<Integer>asList(new Integer[]{Integer.valueOf(45), Integer.valueOf(-45)})) {
                Location location = l.clone();
                double d1 = location.getPitch() - location.getPitch() * 0.5D;
                double d2 = (integer.intValue() > 0) ? (location.getPitch() * 0.45D) : -(location.getPitch() * 0.45D);
                double d3 = (location.getYaw() + integer.intValue()) + ((d1 < 0.0D) ? -d2 : d2);
                location.setPitch((float) d1 + integer1.intValue());
                location.setYaw((float) d3);
                location.add(location.getDirection().multiply(0.3D));
                createParticle(location, c, 1);
            }
        }
    }

    public static void drawLine(Location point1, Location point2, double space, double gradient, Color c, Color c2) {
        World world = point1.getWorld();
        if (point2.getWorld().equals(world)) {
            //Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
            double distance = point1.distance(point2);
            Vector p1 = point1.toVector();
            Vector p2 = point2.toVector();
            Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
            double length = 0;
            for (; length < distance; p1.add(vector)) {
                double ratio = length / distance;
                int red = (int) Math.abs((ratio * c2.getRed()) + ((1 - ratio) * c.getRed()));
                int green = (int) Math.abs((ratio * c2.getGreen()) + ((1 - ratio) * c.getGreen()));
                int blue = (int) Math.abs((ratio * c2.getBlue()) + ((1 - ratio) * c.getBlue()));
                Color stepColor = Color.fromRGB(red, green, blue);
                createParticle(new Location(world, p1.getX(), p1.getY(), p1.getZ()), stepColor, 1);
                length += space;
            }

        }

    }

    public void drawLine(Location point1, Location point2, double space) {
        World world = point1.getWorld();
        if (point2.getWorld().equals(world)) {
            //Validate.isTrue(point2.getWorld().equals(world), "Lines cannot be in different worlds!");
            double distance = point1.distance(point2);
            Vector p1 = point1.toVector();
            Vector p2 = point2.toVector();
            Vector vector = p2.clone().subtract(p1).normalize().multiply(space);
            double length = 0;
            for (; length < distance; p1.add(vector)) {
                world.spawnParticle(Particle.REDSTONE, new Location(world, p1.getX(), p1.getY(), p1.getZ()), 1, new Particle.DustOptions(Color.RED, 1));
                length += space;
            }

        }

    }
}

