/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 07.04.2025 um 16:44
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

public class banCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            if (args.length < 2) {
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/ban <Spieler> <Grund>");
            }

            if (args.length > 1) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);

                StringBuilder reasonBuilder = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    reasonBuilder.append(args[i]).append(" ");
                }
                String reason = reasonBuilder.toString().trim();

                if (BanManager.getBanData().contains("Bans." + targetPlayer.getName())) {
                    player.sendMessage(ResetWorld.getPrefix() + "§cDer Spieler ist bereits gebannt");
                    return true;
                }

                BanManager.setPermaBan(targetPlayer.getName(), reason, player.getName());

                // Falls der Spieler online ist -> kicken
                if (targetPlayer.isOnline()) {
                    Player onlinePlayer = targetPlayer.getPlayer();
                    if (onlinePlayer != null) {
                        onlinePlayer.kickPlayer("§4Du wurdest permanent gebannt\nGrund: §7" + reason);
                    }
                }

                for (Player staffPlayer : Bukkit.getOnlinePlayers()) {
                    if (staffPlayer.getName().equals("kallifabio") || staffPlayer.getName().equals("BeFizzi")) {
                        staffPlayer.sendMessage(ResetWorld.getPrefix() + "§2Der Spieler §e" + targetPlayer.getName() + " §2wurde gebannt! Dauer: §7Permanent, §2Grund: §7" + reason + ", §2Von: §7" + player.getName());
                    }
                }
            }
        }
        return false;
    }
}
