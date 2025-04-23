/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 07.04.2025 um 16:44
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands.bansystem
 */

package de.kallifabio.resetworld.commands.bansystem;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.BanManager;
import de.kallifabio.resetworld.services.GlpiTicketService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class banCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("ban")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                List<String> allowedUsers = Arrays.asList("kallifabio", "BeFizzi");
                if (!(player.isOp() || allowedUsers.contains(player.getName()))) {
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
                GlpiTicketService.createTicket(targetPlayer.getName(), reason, player.getName(), "Permanent");

                // Falls der Spieler online ist -> kicken
                if (targetPlayer.isOnline()) {
                    Player onlinePlayer = targetPlayer.getPlayer();
                    if (onlinePlayer != null) {
                        onlinePlayer.kickPlayer("§9Chaos§3Craft\n§4Du wurdest permanent gebannt\nGrund: §7" + reason);
                    }
                }

                for (Player staffPlayer : Bukkit.getOnlinePlayers()) {
                    if (staffPlayer.getName().equals("kallifabio") || staffPlayer.getName().equals("BeFizzi")) {
                        staffPlayer.sendMessage(" ");
                        staffPlayer.sendMessage(ResetWorld.getPrefix() + "§2Der Spieler §e" + targetPlayer.getName() + " §2wurde gebannt!");
                        staffPlayer.sendMessage(ResetWorld.getPrefix() + "§2Dauer: §7Permanent");
                        staffPlayer.sendMessage(ResetWorld.getPrefix() + "§2Grund: §7" + reason);
                        staffPlayer.sendMessage(ResetWorld.getPrefix() + "§2Von: §7" + player.getName());
                        staffPlayer.sendMessage(" ");
                    }
                }
            }
        }
        return false;
    }
}
