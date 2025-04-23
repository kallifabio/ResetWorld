/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 09.11.2024 um 03:35
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.MaintenanceManager;
import de.kallifabio.resetworld.managers.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class wartungCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("wartung")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                List<String> allowedUsers = Arrays.asList("kallifabio", "BeFizzi");
                if (!(player.isOp() || allowedUsers.contains(player.getName()))) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            boolean currentStatus = MaintenanceManager.isMaintenance();
            MaintenanceManager.setMaintenance(!currentStatus);

            String status = currentStatus ? "deaktiviert" : "aktiviert";
            sender.sendMessage(ResetWorld.getPrefix() + "§2Wartungsmodus wurde §9" + status);

            if (!currentStatus) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (!player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                        player.kickPlayer("§4§lDer Server befindet sich nun in Wartungsarbeiten");
                    }
                }
            }
            return true;
        }
        return false;
    }
}
