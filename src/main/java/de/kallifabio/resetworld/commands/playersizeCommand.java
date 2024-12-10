/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 10.12.2024 um 02:36
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.enums.PlayerSizeType;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class playersizeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("playersize")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() || !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            Player player = (Player) sender;

            if (args.length == 0) {
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/playersize <SMALL | NORMAL | LARGE>");
                return true;
            }

            try {
                // Größe aus der Enum-Klasse holen
                PlayerSizeType sizeType = PlayerSizeType.valueOf(args[0].toUpperCase());
                changePlayerSize(player, sizeType);
                player.sendMessage(ResetWorld.getPrefix() + "§2Deine Größe wurde auf §7" + sizeType.name() + " §2gesetzt §7(" + (sizeType.getScale() * 100) + "%)");
            } catch (IllegalArgumentException e) {
                player.sendMessage(ResetWorld.getPrefix() + "§cUngültige Größe! Verfügbare Typen: §7SMALL, NORMAL, LARGE");
            }

            return true;
        }
        return false;
    }

    private void changePlayerSize(Player player, PlayerSizeType sizeType) {
        // Attribute ändern (Bewegungsgeschwindigkeit)
        double scale = sizeType.getScale();
        player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(0.1 * scale); // Beispiel: Änderung der Bewegungsgeschwindigkeit
        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(scale);
    }
}
