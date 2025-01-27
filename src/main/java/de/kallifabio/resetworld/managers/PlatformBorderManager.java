/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 13.01.2025 um 01:19
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import de.kallifabio.resetworld.listeners.PlayerListener;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlatformBorderManager {

    private final Location platformCenter;
    private final double platformRadius;
    public static boolean borderActive = true;

    public PlatformBorderManager(Location platformCenter, double platformRadius) {
        this.platformCenter = platformCenter;
        this.platformRadius = platformRadius;
    }

    public boolean isInsideBorder(Location location) {
        World world = platformCenter.getWorld();
        if (location.getWorld() == null || !location.getWorld().equals(world)) {
            return false;
        }

        double distanceSquared = location.distanceSquared(platformCenter);
        return distanceSquared <= platformRadius * platformRadius;
    }

    public void handlePlayerMovement(PlayerMoveEvent event) {
        if (!borderActive) return;

        Player player = event.getPlayer();
        Location to = event.getTo();
        if (to == null) return;

        if (!isInsideBorder(to)) {
            event.setCancelled(true);
            player.sendMessage("Die unsichtbare Border blockiert dich!");
        }
    }

    public static boolean isBorderActive() {
        return borderActive;
    }
}
