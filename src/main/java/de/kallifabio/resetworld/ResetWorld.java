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
import de.kallifabio.resetworld.listeners.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class ResetWorld extends JavaPlugin implements Listener {

    private static final String prefix = "§9Chaos§3Craft §8» §r";
    private static final String noPermission = "§cDu hast keine Rechte für diesen Befehl";
    private static ResetWorld instance;

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

        Bukkit.getConsoleSender().sendMessage(prefix + "§4Das Plugin wurde mit Version §e" + getInstance().getDescription().getVersion() + " §4gestoppt");
    }

    private void registerEvents() {
        PluginManager plugin = Bukkit.getPluginManager();

        plugin.registerEvents(new PlayerListener(), this);
        plugin.registerEvents(this, this);
    }

    private void registerCommands() {
        getCommand("statuschat").setExecutor(new statuschatCommand());
        getCommand("status").setExecutor(new statusCommand());
        getCommand("statuscreate").setExecutor(new statuscreateCommand());
        getCommand("statusremove").setExecutor(new statusremoveCommand());
        getCommand("statusset").setExecutor(new statussetCommand());

        getCommand("resetworld").setExecutor(new resetWorldCommand());

        getCommand("playersize").setExecutor(new playersizeCommand());
        getCommand("trade").setExecutor(new tradeCommand());
        getCommand("wartung").setExecutor(new wartungCommand());

    }



}
