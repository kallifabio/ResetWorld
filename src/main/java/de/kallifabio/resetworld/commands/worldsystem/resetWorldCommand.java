/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.09.2024 um 20:08
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands.worldsystem;

import de.kallifabio.resetworld.ResetWorld;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class resetWorldCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("resetworld")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() || !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            resetWorldAndRestart("world");
            resetWorldAndRestart("world_nether");
            resetWorldAndRestart("world_the_end");
            sender.sendMessage("§2Die Welt wird zurückgesetzt und der Server wird neu gestartet!");
            return true;
        }
        return false;
    }

    /**
     * Resettet die Welt und startet den Server neu.
     *
     * @param worldName Name der Welt
     */
    private void resetWorldAndRestart(String worldName) {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            // Spieler in eine andere Welt teleportieren
            for (Player player : world.getPlayers()) {
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }

            // Welt entladen
            Bukkit.unloadWorld(world, false);

            // Alte Welt-Dateien löschen
            File worldFolder = new File(Bukkit.getWorldContainer(), worldName);
            deleteWorld(worldFolder);

            // Neue Welt erstellen
            WorldCreator wc = new WorldCreator(worldName);
            Bukkit.createWorld(wc);

            // Server neu starten
            restartServer();
        } else {
            Bukkit.getConsoleSender().sendMessage("§cDie Welt mit dem Namen §e" + worldName + " §cexistiert nicht");
        }
    }

    /**
     * Löscht das Verzeichnis der Welt rekursiv.
     *
     * @param path Datei oder Verzeichnis, das gelöscht werden soll
     * @return true, wenn die Datei/der Ordner erfolgreich gelöscht wurde
     */
    private boolean deleteWorld(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteWorld(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        return path.delete();
    }

    /**
     * Führt einen Serverneustart durch, indem der Befehl "/restart" ausgeführt wird.
     * Stelle sicher, dass ein Restart-Skript in der Spigot-Konfiguration vorhanden ist.
     */
    private void restartServer() {
        Bukkit.getConsoleSender().sendMessage("§cServer wird neu gestartet...");
        // Spigot hat den /restart-Befehl, wenn ein Restart-Skript konfiguriert ist
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
    }
}
