/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 13.01.2025 um 01:23
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.PlatformBorderManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class removeborderCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("removeborder")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            if (!PlatformBorderManager.isBorderActive()) {
                player.sendMessage(ResetWorld.getPrefix() + "§cDie Border wurde bereits entfernt!");
                return true;
            }

            player.sendMessage(ResetWorld.getPrefix() + "§7Die Border wird in 10 Sekunden entfernt...");

            new BukkitRunnable() {
                int countdown = 10;

                @Override
                public void run() {
                    if (countdown > 0) {
                        Bukkit.broadcastMessage(ResetWorld.getPrefix() + "§2Die Border wird in §e" + countdown + " §2Sekunden entfernt!");
                        countdown--;
                    } else {
                        PlatformBorderManager.borderActive = false; // Deaktiviert die Border
                        Bukkit.broadcastMessage(ResetWorld.getPrefix() + "§cDie Border wurde entfernt!");
                        cancel();
                    }
                }
            }.runTaskTimer(ResetWorld.getInstance(), 0L, 20L);
        }
        return false;
    }
}
