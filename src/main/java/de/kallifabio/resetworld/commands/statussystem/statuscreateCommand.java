/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 03.11.2024 um 02:48
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands.statussystem;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.managers.StatusManager;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class statuscreateCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("statuscreate")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (!player.isOp() || !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }
            }

            if (args.length < 2) {
                sender.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/statuscreate <Name> <Color>");
            } else {
                String name = args[0];
                String colorInput = args[1].toUpperCase();

                try {
                    ChatColor color = ChatColor.valueOf(colorInput);
                    Player player = (Player) sender;
                    player.sendMessage(ResetWorld.getPrefix() + "§2Dein Status wurde erstellt: " + color + name);
                    StatusManager.createStatus(name, colorInput);
                } catch (IllegalArgumentException e) {
                    sender.sendMessage(ResetWorld.getPrefix() + "§4Ungültige Farbe. Verfügbare Farben: ");
                    for (ChatColor color : ChatColor.values()) {
                        if (color.isColor()) {
                            sender.sendMessage(color + color.name());
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String alias, String[] args) {
        if (args.length == 2) {
            List<String> colorOptions = new ArrayList<>();
            for (ChatColor color : ChatColor.values()) {
                if (color.isColor()) {
                    colorOptions.add(color.name().toLowerCase());
                }
            }
            return colorOptions;
        }
        return null;
    }
}
