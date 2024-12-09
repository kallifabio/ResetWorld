/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 09.12.2024 um 23:54
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import org.bukkit.ChatColor;
import org.bukkit.Location;
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
        getLastlocationdata().set("LastLocation", player);
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


}
