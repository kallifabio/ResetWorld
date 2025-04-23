/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 07.04.2025 um 16:45
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands.bansystem
 */

package de.kallifabio.resetworld.commands.bansystem;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.BanManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class unbanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("unban")) {
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
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/unban <Spieler>");
            }

            if (args.length > 0) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);

                if (!BanManager.getBanData().contains("Bans." + targetPlayer.getName())) {
                    player.sendMessage(ResetWorld.getPrefix() + "§cDer Spieler hat derzeit keinen Ban");
                    return true;
                }

                BanManager.unbanPlayer(targetPlayer.getName());

                for (Player staffPlayer : Bukkit.getOnlinePlayers()) {
                    if (staffPlayer.getName().equals("kallifabio") || staffPlayer.getName().equals("BeFizzi")) {
                        staffPlayer.sendMessage(ResetWorld.getPrefix() + "§2Der Spieler §e" + targetPlayer.getName() + " §2wurde entbannt");
                    }
                }
            }
        }
        return false;
    }
}
