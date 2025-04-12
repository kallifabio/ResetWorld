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

public class baninfoCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("baninfo")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/baninfo <Spieler>");
            }

            if (args.length > 0) {
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(args[0]);

                if (!BanManager.getBanData().contains("Bans." + targetPlayer.getName())) {
                    player.sendMessage(ResetWorld.getPrefix() + "§cDer Spieler hat derzeit keinen Ban");
                    return true;
                }

                String reason = BanManager.getBanReason(targetPlayer.getName());
                long expiry = BanManager.getBanTime(targetPlayer.getName());
                String bannedFrom = BanManager.getBannedFrom(targetPlayer.getName());

                player.sendMessage(" ");
                player.sendMessage(ResetWorld.getPrefix() + "§7Informationen zu Spieler §e" + targetPlayer.getName());
                player.sendMessage(ResetWorld.getPrefix() + "§7Grund: §9" + reason);
                player.sendMessage(ResetWorld.getPrefix() + "§7Dauer: §9" + (expiry == -1 ? "Permanent" : BanManager.formatRemainingTime(expiry)));
                player.sendMessage(ResetWorld.getPrefix() + "§7Von: §9" + bannedFrom);
                player.sendMessage(" ");
            }
        }
        return false;
    }
}
