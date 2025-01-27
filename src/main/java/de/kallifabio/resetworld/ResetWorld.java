/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.09.2024 um 18:35
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld
 */

package de.kallifabio.resetworld;

import de.kallifabio.resetworld.commands.*;
import de.kallifabio.resetworld.commands.statussystem.*;
import de.kallifabio.resetworld.commands.worldsystem.resetWorldCommand;
import de.kallifabio.resetworld.listeners.MobSpawnListener;
import de.kallifabio.resetworld.listeners.PlayerListener;
import de.kallifabio.resetworld.managers.PlatformBorderManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;

import java.util.HashMap;
import java.util.UUID;

public class ResetWorld extends JavaPlugin implements Listener {

    private static final String prefix = "§9Chaos§3Craft §8» §r";
    private static final String noPermission = "§cDu hast keine Rechte für diesen Befehl";
    private static ResetWorld instance;

    private final HashMap<Player, ArmorStand> playerPoses = new HashMap<>();
    private final HashMap<UUID, UUID> carryingMap = new HashMap<>();

    public static ResetWorld getInstance() {
        return instance;
    }

    public static String getPrefix() {
        return prefix;
    }

    public static String getNoPermission() {
        return noPermission;
    }

    @Override
    public void onEnable() {
        instance = this;
        registerEvents();
        registerCommands();

        Bukkit.getConsoleSender().sendMessage(prefix + "§2Das Plugin wurde mit Version §e" + getInstance().getDescription().getVersion() + " §2gestartet");
    }

    @Override
    public void onDisable() {
        playerPoses.values().forEach(ArmorStand::remove);
        playerPoses.clear();
        carryingMap.clear();

        Bukkit.getConsoleSender().sendMessage(prefix + "§4Das Plugin wurde mit Version §e" + getInstance().getDescription().getVersion() + " §4gestoppt");
    }

    private void registerEvents() {
        PluginManager plugin = Bukkit.getPluginManager();

        plugin.registerEvents(new PlayerListener(), this);
        plugin.registerEvents(new MobSpawnListener(), this);
    }

    private void registerCommands() {
        getCommand("statuschat").setExecutor(new statuschatCommand());
        getCommand("status").setExecutor(new statusCommand());
        getCommand("statuscreate").setExecutor(new statuscreateCommand());
        getCommand("statusremove").setExecutor(new statusremoveCommand());
        getCommand("statusset").setExecutor(new statussetCommand());

        ///getCommand("resetworld").setExecutor(new resetWorldCommand());

        getCommand("broadcast").setExecutor(new broadcastCommand());
        getCommand("endersee").setExecutor(new enderseeCommand());
        getCommand("fly").setExecutor(new flyCommand());
        getCommand("invsee").setExecutor(new invseeCommand());
        getCommand("lastlocation").setExecutor(new lastlocationCommand());
        getCommand("maxplayers").setExecutor(new maxplayersCommand());
        getCommand("ping").setExecutor(new pingCommand());
        //getCommand("playersize").setExecutor(new playersizeCommand());
        getCommand("removeborder").setExecutor(new removeborderCommand());
        getCommand("sit").setExecutor(new sitCommand());
        getCommand("trade").setExecutor(new tradeCommand());
        getCommand("wartung").setExecutor(new wartungCommand());

    }

    public ArmorStand spawnPoseArmorStand(Location location, Player player, boolean laying) {
        World world = location.getWorld();
        if (world == null) return null;

        // ArmorStand erstellen
        ArmorStand armorStand = world.spawn(location, ArmorStand.class);
        armorStand.setInvisible(true);
        armorStand.setGravity(false);
        armorStand.setMarker(true);
        armorStand.setInvulnerable(true);
        armorStand.setRotation(player.getLocation().getYaw(), 0);

        // Spieler auf den ArmorStand setzen
        armorStand.addPassenger(player);

        return armorStand;
    }

    public void removePose(Player player) {
        ArmorStand armorStand = playerPoses.get(player);
        if (armorStand != null) {
            armorStand.remove();
        }

        playerPoses.remove(player);
    }

    public HashMap<Player, ArmorStand> getPlayerPoses() {
        return playerPoses;
    }

    public HashMap<UUID, UUID> getCarryingMap() {
        return carryingMap;
    }
}
