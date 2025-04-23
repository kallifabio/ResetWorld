/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 13.01.2025 um 02:35
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.LastLocationManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class lastlocationCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("lastlocation")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                List<String> allowedUsers = Arrays.asList("kallifabio", "BeFizzi");
                if (!(player.isOp() || allowedUsers.contains(player.getName()))) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/lastlocation <Spieler>");
            }

            if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                String formattedLocation = LastLocationManager.getFormattedLocation(targetPlayer.getName());
                player.sendMessage(ResetWorld.getPrefix() + "§7Die letzte gespeicherte Location von §e" + targetPlayer.getName() + " §7war \n" + formattedLocation);
            }
        }
        return false;
    }
}
