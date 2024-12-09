/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 03.11.2024 um 03:18
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class StatusManager {

    public static File statusfile = new File("plugins/ResetWorld", "status.yml");
    public static FileConfiguration statusdata = YamlConfiguration.loadConfiguration(statusfile);

    public static File getStatusfile() {
        return statusfile;
    }

    public static FileConfiguration getStatusdata() {
        return statusdata;
    }

    public static void saveStatusData() {
        try {
            getStatusdata().save(statusfile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChatColor.DARK_RED + "§lERROR");
        }
    }

    public static void createStatus(String name, String color) {
        getStatusdata().set("Status." + name, name);
        getStatusdata().set("Status." + name + ".Color", color);
        saveStatusData();

        if (!getStatusfile().exists()) {
            try {
                getStatusfile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setStatusPlayer(String name, String player) {
        List<String> players = getStatusdata().getStringList("Status." + name + ".Players");

        if (!players.contains(player)) {
            players.add(player);
        }

        getStatusdata().set("Status." + name + ".Players", players);
        saveStatusData();
    }

    public static void removeStatusPlayer(String name, String player) {
        List<String> players = getStatusdata().getStringList("Status." + name + ".Players");

        players.remove(player);

        getStatusdata().set("Status." + name + ".Players", players);
        saveStatusData();
    }

    public static String getStatusPlayer(String playerName) {
        Set<String> statuses = getStatusdata().getConfigurationSection("Status").getKeys(false);

        for (String status : statuses) {
            List<String> players = getStatusdata().getStringList("Status." + status + ".Players");
            if (players.contains(playerName)) {
                return status;
            }
        }

        return null;
    }

    public static List<String> getStatusPlayerList(String status) {
        return getStatusdata().getStringList("Status." + status + ".Players");
    }

    public static Set<String> getStatus() {
        return getStatusdata().getConfigurationSection("Status").getKeys(false);
    }

    public static ChatColor getStatusColor(String status) {
        String colorname = getStatusdata().getString("Status." + status + ".Color");
        return colorname != null ? ChatColor.valueOf(colorname.toUpperCase()) : ChatColor.WHITE;
    }
}
