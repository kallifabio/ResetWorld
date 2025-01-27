/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 13.01.2025 um 21:30
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.LastLocationManager;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class broadcastCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("broadcast")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/broadcast <Nachricht>");
            }

            if (args.length > 0) {
                StringBuilder message = new StringBuilder();
                for (String arg : args) {
                    message.append(arg).append(" ");
                }

                String finalMessage = message.toString().trim();

                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(ResetWorld.getPrefix() + "§4-----------§cBroadcast§4-----------");
                Bukkit.broadcastMessage(ResetWorld.getPrefix() + finalMessage);
                Bukkit.broadcastMessage(ResetWorld.getPrefix() + "§4-----------§cBroadcast§4-----------");
                Bukkit.broadcastMessage(" ");

                Bukkit.getOnlinePlayers().forEach(all -> {
                    all.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
                });
            }
        }
        return false;
    }
}
