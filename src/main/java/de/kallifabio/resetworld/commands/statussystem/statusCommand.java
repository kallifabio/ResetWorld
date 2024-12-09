/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 03.11.2024 um 02:48
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

import java.util.Set;

public class statusCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("status")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                /*if (!player.isOp()) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }*/
            }

            Set<String> statuses = StatusManager.getStatus();

            if (statuses.isEmpty()) {
                sender.sendMessage(ResetWorld.getPrefix() + "§4Es ist kein Status vorhanden");
            } else {
                sender.sendMessage(ResetWorld.getPrefix() + "§2Dieser Status ist verfügbar:");
                for (String status : statuses) {
                    sender.sendMessage("§e- " + status);
                }
            }


        }
        return false;
    }
}
