/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 13.01.2025 um 02:10
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.PlatformBorderManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class pingCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ping")) {
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
                player.sendMessage(ResetWorld.getPrefix() + "§6Du hast einen Ping von §9" + player.getPing() + "ms");
            }

            if (args.length == 1) {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer != null) {
                    player.sendMessage(ResetWorld.getPrefix() + "§9" + targetPlayer.getName() + " §6hat einen Ping von §9" + targetPlayer.getPing() + "ms");
                } else {
                    player.sendMessage(ResetWorld.getPrefix() + "§cDer Spieler ist nicht online");
                }
            }
        }
        return false;
    }
}
