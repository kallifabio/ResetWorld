/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 07.04.2025 um 16:44
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.managers
 */

package de.kallifabio.resetworld.managers;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class BanManager {

    public static File banFile = new File("plugins/ResetWorld", "ban.yml");
    public static FileConfiguration banData = YamlConfiguration.loadConfiguration(banFile);

    public static File getBanFile() {
        return banFile;
    }

    public static FileConfiguration getBanData() {
        return banData;
    }

    public static void saveBanData() {
        try {
            getBanData().save(banFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println(ChatColor.DARK_RED + "ERROR");
        }
    }

    public static void setPermaBan(String bannedPlayer, String reason, String staffPlayer) {
        getBanData().set("Bans." + bannedPlayer + ".Time", -1);
        getBanData().set("Bans." + bannedPlayer + ".Reason", reason);
        getBanData().set("Bans." + bannedPlayer + ".BannedFrom", staffPlayer);
        saveBanData();

        if (!getBanFile().exists()) {
            try {
                getBanFile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setTempBan(String bannedPlayer, long expiry, String reason, String staffPlayer) {
        getBanData().set("Bans." + bannedPlayer + ".Time", expiry);
        getBanData().set("Bans." + bannedPlayer + ".Reason", reason);
        getBanData().set("Bans." + bannedPlayer + ".BannedFrom", staffPlayer);
        saveBanData();

        if (!getBanFile().exists()) {
            try {
                getBanFile().createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void unbanPlayer(String bannedPlayer) {
        getBanData().set("Bans." + bannedPlayer, null);
        saveBanData();
    }

    public static long getBanTime(String bannedPlayer) {
        return getBanData().getLong("Bans." + bannedPlayer + ".Time");
    }

    public static String getBanReason(String bannedPlayer) {
        return getBanData().getString("Bans." + bannedPlayer + ".Reason");
    }

    public static String getBannedFrom(String bannedPlayer) {
        return getBanData().getString("Bans." + bannedPlayer + ".BannedFrom");
    }

    public static String formatRemainingTime(long expiry) {
        long remaining = expiry - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(remaining);
        long hours = TimeUnit.MILLISECONDS.toHours(remaining) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(remaining) % 60;
        return days + " Tage, " + hours + " Stunden, " + minutes + " Minuten";
    }

    public static long calculateExpiry(String unit, int amount) {
        long currentTime = System.currentTimeMillis();
        switch (unit) {
            case "tag":
            case "tage":
                return currentTime + TimeUnit.DAYS.toMillis(amount);
            case "woche":
            case "wochen":
                return currentTime + TimeUnit.DAYS.toMillis(amount * 7);
            case "monat":
            case "monate":
                return currentTime + TimeUnit.DAYS.toMillis(amount * 30);
            default:
                return -1;
        }
    }
}
