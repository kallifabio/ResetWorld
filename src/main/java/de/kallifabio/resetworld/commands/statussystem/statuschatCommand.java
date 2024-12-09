/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 15.11.2024 um 21:54
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands.statussystem;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class statuschatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("statuschat")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                /*if (!player.isOp()) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }*/
            }

            Player player = (Player) sender;
            String status = StatusManager.getStatusPlayer(player.getName());

            if (status == null) {
                player.sendMessage(ResetWorld.getPrefix() + "§4Du bist keinem Status zugeordnet.");
                return true;
            }

            if (args.length == 0) {
                player.sendMessage(ResetWorld.getPrefix() + "§4Bitte gib eine Nachricht ein, die du im Status-Chat senden möchtest. §cBitte benutze §7/statuschat <Message>");
                return true;
            }

            String message = String.join(" ", args);
            ChatColor color = StatusManager.getStatusColor(status);

            List<String> playersInStatus = StatusManager.getStatusPlayerList(status);
            for (String playerName : playersInStatus) {
                Player targetPlayer = Bukkit.getPlayer(playerName);
                if (targetPlayer != null && targetPlayer.isOnline()) {
                    targetPlayer.sendMessage(color + "[" + status + "-Chat] " + ChatColor.RESET + player.getName() + ": " + message);
                }
            }
            return true;


        }
        return false;
    }
}
