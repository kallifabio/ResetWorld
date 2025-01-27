/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 13.01.2025 um 02:20
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class maxplayersCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("maxplayers")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/maxplayers <Anzahl>");
            }

            if (args.length == 1) {
                try {
                    int newPlayers = Integer.parseInt(args[0]);
                    ResetWorld.getInstance().getServer().setMaxPlayers(newPlayers);
                    updateServerProperties(newPlayers);
                    player.sendMessage(ResetWorld.getPrefix() + "§2Du hast die Spielergröße des Servers auf §3" + newPlayers + " §2gesetzt");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    private void updateServerProperties(int newMaxPlayers) throws IOException {
        File serverPropertiesFile = new File("server.properties");
        List<String> lines = Files.readAllLines(Paths.get(serverPropertiesFile.toURI()));

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.startsWith("max-players=")) {
                lines.set(i, "max-players=" + newMaxPlayers);
                break;
            }
        }

        Files.write(Paths.get(serverPropertiesFile.toURI()), lines);
    }
}
