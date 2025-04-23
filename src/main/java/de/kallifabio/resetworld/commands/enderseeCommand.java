/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.01.2025 um 03:31
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

import java.util.Arrays;
import java.util.List;

public class enderseeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("endersee")) {
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
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §e/endersee <Spieler>");
            }

            if (args.length == 1) {
                Player target = Bukkit.getPlayer(args[0]);
                if (target != null) {
                    player.openInventory(target.getEnderChest());
                } else {
                    player.sendMessage(ResetWorld.getPrefix() + "§cDer Spieler §e" + args[0] + " §cist nicht online.");
                }
            }
        }
        return false;
    }
}
