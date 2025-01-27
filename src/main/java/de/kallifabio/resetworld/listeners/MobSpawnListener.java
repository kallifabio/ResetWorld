/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 10.01.2025 um 19:41
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.listeners
 */

package de.kallifabio.resetworld.listeners;

import de.kallifabio.resetworld.ResetWorld;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;

public class MobSpawnListener implements Listener {

    private final Location platformCenter;
    private final double platformRadius;

    public MobSpawnListener() {
        this.platformCenter = new PlayerListener().getPlatformCenter();
        this.platformRadius = new PlayerListener().getPlatformRadius();

        preventMobSpawningOnPlatform();
    }

    public void preventMobSpawningOnPlatform() {
        new BukkitRunnable() {
            @Override
            public void run() {
                World world = platformCenter.getWorld();
                if (world == null) return;

                for (Entity entity : world.getEntities()) {
                    if (!(entity instanceof Player) && isOnPlatform(entity.getLocation())) {
                        if (entity.getType() == EntityType.ZOMBIE ||
                                entity.getType() == EntityType.COW ||
                                entity.getType() == EntityType.SKELETON ||
                                entity.getType() == EntityType.CHICKEN ||
                                entity.getType() == EntityType.CREEPER ||
                                entity.getType() == EntityType.SPIDER ||
                                entity.getType() == EntityType.ENDERMAN ||
                                entity.getType() == EntityType.VILLAGER ||
                                entity.getType() == EntityType.SHEEP ||
                                entity.getType() == EntityType.PIG ||
                                entity.getType() == EntityType.PHANTOM ||
                                entity.getType() == EntityType.WITCH) {
                            entity.remove();
                        }
                    }
                }
            }
        }.runTaskTimer(ResetWorld.getInstance(), 20L, 20L); // Runs every second
    }

    private boolean isOnPlatform(Location location) {
        if (location.getWorld() == null || !location.getWorld().equals(platformCenter.getWorld())) {
            return false;
        }

        double distanceSquared = location.distanceSquared(platformCenter);
        return distanceSquared <= platformRadius * platformRadius;
    }
}
