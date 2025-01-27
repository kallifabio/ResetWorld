/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 09.12.2024 um 23:54
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import de.kallifabio.resetworld.ResetWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

public class LastLocationManager {

    public static File lastlocationfile = new File("plugins/ResetWorld", "lastlocation.yml");
    public static FileConfiguration lastlocationdata = YamlConfiguration.loadConfiguration(lastlocationfile);

    public static File getLastlocationfile() {
        return lastlocationfile;
    }

    public static FileConfiguration getLastlocationdata() {
        return lastlocationdata;
    }

    public static void saveLastLocationData() {
        try {
            getLastlocationdata().save(lastlocationfile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChatColor.DARK_RED + "§lERROR");
        }
    }

    public static void setLastLocation(String player, Location location) {
        getLastlocationdata().set("LastLocation." + player + ".World", location.getWorld().getName());
        getLastlocationdata().set("LastLocation." + player + ".X", location.getX());
        getLastlocationdata().set("LastLocation." + player + ".Y", location.getY());
        getLastlocationdata().set("LastLocation." + player + ".Z", location.getZ());
        getLastlocationdata().set("LastLocation." + player + ".Yaw", location.getYaw());
        getLastlocationdata().set("LastLocation." + player + ".Pitch", location.getPitch());
        saveLastLocationData();

        if (!getLastlocationfile().exists()) {
            try {
                getLastlocationfile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Location getLastLocation(String player) {
        World world = Bukkit.getWorld(getLastlocationdata().getString("LastLocation." + player + ".World"));
        double x = getLastlocationdata().getDouble("LastLocation." + player + ".X");
        double y = getLastlocationdata().getDouble("LastLocation." + player + ".Y");
        double z = getLastlocationdata().getDouble("LastLocation." + player + ".Z");
        Location location = new Location(world, x, y, z);
        location.setYaw(getLastlocationdata().getInt("LastLocation." + player + ".Yaw"));
        location.setPitch(getLastlocationdata().getInt("LastLocation." + player + ".Pitch"));
        return location;
    }

    public static String getFormattedLocation(String player) {
        Location location = getLastLocation(player);
        if (location == null) {
            return "Die gespeicherte Location des Spielers ist nicht verfügbar.";
        }
        return ResetWorld.getPrefix() + "§eWelt: " + location.getWorld().getName() + "\n" + ResetWorld.getPrefix() +
                "§eX: " + location.getX() + "\n" + ResetWorld.getPrefix() +
                "§eY: " + location.getY() + "\n" + ResetWorld.getPrefix() +
                "§eZ: " + location.getZ() + "\n" + ResetWorld.getPrefix() +
                "§eYaw: " + location.getYaw() + "\n" + ResetWorld.getPrefix() +
                "§ePitch: " + location.getPitch();
    }

}
