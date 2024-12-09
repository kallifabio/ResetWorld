/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 03.11.2024 um 18:35
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class DeathManager {

    public static File deathfile = new File("plugins/ResetWorld", "death.yml");
    public static FileConfiguration deathdata = YamlConfiguration.loadConfiguration(deathfile);

    public static File getDeathfile() {
        return deathfile;
    }

    public static FileConfiguration getDeathdata() {
        return deathdata;
    }

    public static void saveDeathData() {
        try {
            getDeathdata().save(deathfile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChatColor.DARK_RED + "§lERROR");
        }
    }

    public static void setDeath(String player, int death) {
        getDeathdata().set("Deaths." + player, death);
        saveDeathData();

        if (!getDeathfile().exists()) {
            try {
                getDeathfile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static Integer getDeath(String player) {
        return getDeathdata().getInt("Deaths." + player);
    }
}
