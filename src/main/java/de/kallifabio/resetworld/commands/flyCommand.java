/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.01.2025 um 02:35
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.enums.PlayerSizeType;
import de.kallifabio.resetworld.managers.MaintenanceManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class flyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("fly")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            switch (args.length) {
                case 0:
                    if (!player.getAllowFlight()) {
                        player.setAllowFlight(true);
                        player.setFlying(true);
                        player.sendMessage(ResetWorld.getPrefix() + "§aFlugmodus aktiviert");
                    } else {
                        player.setAllowFlight(false);
                        player.setFlying(false);
                        player.sendMessage(ResetWorld.getPrefix() + "§cFlugmodus deaktiviert");
                    }
                    break;
                case 1:
                    try {
                        Player targetPlayer = Bukkit.getPlayer(args[0]);
                        if (targetPlayer != null) {
                            if (!targetPlayer.getAllowFlight()) {
                                targetPlayer.setAllowFlight(true);
                                targetPlayer.setFlying(true);
                                player.sendMessage(ResetWorld.getPrefix() + "§aFlugmodus für §e" + targetPlayer.getName() + " §aaktiviert");
                                targetPlayer.sendMessage(ResetWorld.getPrefix() + "§aFlugmodus aktiviert");
                            } else {
                                targetPlayer.setAllowFlight(false);
                                targetPlayer.setFlying(false);
                                player.sendMessage(ResetWorld.getPrefix() + "§cFlugmodus für §e" + targetPlayer.getName() + " §cdeaktiviert");
                                targetPlayer.sendMessage(ResetWorld.getPrefix() + "§cFlugmodus deaktiviert");
                            }
                        } else {
                            player.sendMessage(ResetWorld.getPrefix() + "§cDer Spieler §e" + args[0] + " §cist nicht online");
                        }
                    } catch (IllegalArgumentException e) {
                        player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §e/fly <Spieler>");
                    }
                    break;
            }
        }
        return false;
    }
}
