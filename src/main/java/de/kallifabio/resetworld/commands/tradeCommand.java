/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 07.11.2024 um 01:26
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.commands
 */

package de.kallifabio.resetworld.commands;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.listeners.TradeSessionListener;
import de.kallifabio.resetworld.managers.StatusManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class tradeCommand implements CommandExecutor {

    private final Map<UUID, UUID> tradeRequests = new HashMap<>();
    private final Map<UUID, TradeSessionListener> activeTrades = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("trade")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                /*if (!player.isOp()) {
                    player.sendMessage(ResetWorld.getNoPermission());
                    return true;
                }*/
            }

            Player player = (Player) sender;

            if (args.length != 1) {
                player.sendMessage(ResetWorld.getPrefix() + "§cBitte benutze §7/trade <Spieler>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);
            if (target == null || !target.isOnline()) {
                player.sendMessage(ResetWorld.getPrefix() + "§4Spieler wurde nicht gefunden.");
                return true;
            }

            if (target.equals(player)) {
                player.sendMessage(ResetWorld.getPrefix() + "§4Du kannst nicht mit dir selbst handeln.");
                return true;
            }

            if (getTradeRequests().containsKey(target.getUniqueId()) && getTradeRequests().get(target.getUniqueId()).equals(player.getUniqueId())) {
                player.sendMessage(ResetWorld.getPrefix() + "§2Handel mit §e" + target.getName() + " §2akzeptiert!");
                target.sendMessage(ResetWorld.getPrefix() + "§e" + player.getName() + " §2hat deine Handelsanfrage angenommen!");
                startTradeSession(player, target);
                getTradeRequests().remove(target.getUniqueId());
            } else {
                player.sendMessage(ResetWorld.getPrefix() + "§7Handelsanfrage an §e" + target.getName() + " §7gesendet.");
                target.sendMessage(ResetWorld.getPrefix() + "§e" + player.getName() + " §7möchte mit dir handeln. §cAkzeptiere mit /trade " + player.getName());
                getTradeRequests().put(player.getUniqueId(), target.getUniqueId());
            }
            return true;


        }
        return false;
    }

    public void startTradeSession(Player player1, Player player2) {
        TradeSessionListener session = new TradeSessionListener(player1, player2);
        activeTrades.put(player1.getUniqueId(), session);
        activeTrades.put(player2.getUniqueId(), session);
        session.openTradeInventories();
    }

    public Map<UUID, UUID> getTradeRequests() {
        return tradeRequests;
    }

    public Map<UUID, TradeSessionListener> getActiveTrades() {
        return activeTrades;
    }
}
