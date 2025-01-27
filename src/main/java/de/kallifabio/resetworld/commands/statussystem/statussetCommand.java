/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 03.11.2024 um 14:32
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands.statussystem;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class statussetCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("statusset")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                /*if (!player.isOp()) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }*/
            }

            if (args.length < 2) {
                sender.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/statusset <Status> <Player>");
            } else {
                Player player = (Player) sender;
                Player targetPlayer = Bukkit.getPlayer(args[1]);
                if (targetPlayer != null) {
                    StatusManager.setStatusPlayer(args[0], args[1]);
                    player.sendMessage(ResetWorld.getPrefix() + "§2Der Spieler §e" + args[1] + " §2ist jetzt im Status §e" + args[0]);
                    targetPlayer.kickPlayer(args[1]);
                } else {
                    player.sendMessage(ResetWorld.getPrefix() + "§cDer Spieler §e" + args[1] + " §cist nicht online");
                }
            }
        }
        return false;
    }
}
