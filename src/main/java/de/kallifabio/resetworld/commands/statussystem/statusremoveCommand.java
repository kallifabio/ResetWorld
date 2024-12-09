/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 03.11.2024 um 17:23
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands.statussystem;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.StatusManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class statusremoveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("statusremove")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                /*if (!player.isOp()) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }*/
            }

            if (args.length < 2) {
                sender.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/statusremove <Status> <Player>");
            } else {
                Player player = (Player) sender;
                StatusManager.removeStatusPlayer(args[0], args[1]);
                player.sendMessage(ResetWorld.getPrefix() + "§2Der Spieler §e" + args[1] + " §2ist nicht mehr im Status §e" + args[0]);
            }
        }
        return false;
    }
}
