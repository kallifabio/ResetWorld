/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 03.11.2024 um 21:10
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class WorldManager {

    public static File worldfile = new File("plugins/ResetWorld", "worlds.yml");
    public static FileConfiguration worlddata = YamlConfiguration.loadConfiguration(worldfile);

    public static File getWorldfile() {
        return worldfile;
    }

    public static FileConfiguration getWorlddata() {
        return worlddata;
    }

    public static void saveWorldData() {
        try {
            getWorlddata().save(worldfile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChatColor.DARK_RED + "§lERROR");
        }
    }
}
