/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 09.11.2024 um 03:29
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class MaintenanceManager {

    public static File maintenancefile = new File("plugins/ResetWorld", "maintenance.yml");
    public static FileConfiguration maintenancedata = YamlConfiguration.loadConfiguration(maintenancefile);

    public static File getMaintenancefile() {
        return maintenancefile;
    }

    public static FileConfiguration getMaintenancedata() {
        return maintenancedata;
    }

    public static void saveMaintenanceData() {
        try {
            getMaintenancedata().save(maintenancefile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChatColor.DARK_RED + "§lERROR");
        }
    }

    public static void setMaintenance(boolean enabled) {
        getMaintenancedata().set("Maintenance.enabled", enabled);
        saveMaintenanceData();

        if (!getMaintenancefile().exists()) {
            try {
                getMaintenancefile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static boolean isMaintenance() {
        return getMaintenancedata().getBoolean("Maintenance.enabled", false);
    }
}
