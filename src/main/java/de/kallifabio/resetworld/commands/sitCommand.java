/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 14.01.2025 um 02:30
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

public class sitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("sit")) {
            if (sender instanceof Player) {
                /*Player player = (Player) sender;
                if (!player.isOp() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }*/
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                toggleSit(player);
            }

        }
        return false;
    }

    private void toggleSit(Player player) {
        if (ResetWorld.getInstance().getPlayerPoses().containsKey(player)) {
            ResetWorld.getInstance().removePose(player);
        } else {
            Location sitLocation = player.getLocation().clone().add(0, 0.1, 0); // Sitzen in passender Höhe
            ArmorStand armorStand = ResetWorld.getInstance().spawnPoseArmorStand(sitLocation, player, false);
            ResetWorld.getInstance().getPlayerPoses().put(player, armorStand);
        }
    }
}
