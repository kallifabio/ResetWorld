/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 07.11.2024 um 01:26
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.listeners
 */

package de.kallifabio.resetworld.listeners;

import de.kallifabio.resetworld.ResetWorld;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class TradeSessionListener implements Listener {

    private final Player player1;
    private final Player player2;
    private final Inventory inventory1;
    private final Inventory inventory2;

    private boolean player1Ready = false;
    private boolean player2Ready = false;
    private boolean tradeCompleted = false;
    private boolean tradeStarted = false;

    private BukkitRunnable expirationTask;

    public TradeSessionListener(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;

        this.inventory1 = Bukkit.createInventory(null, 9, "§7Handel mit §e" + player2.getName());
        this.inventory2 = Bukkit.createInventory(null, 9, "§7Handel mit §e" + player1.getName());

        // "Bereit"-Button hinzufügen
        ItemStack readyButton = new ItemStack(Material.GREEN_WOOL);
        ItemMeta meta = readyButton.getItemMeta();
        if (meta != null) {
            meta.setDisplayName("§2Bereit");
            readyButton.setItemMeta(meta);
        }

        // "Bereit"-Button in den letzten Slot legen
        inventory1.setItem(8, readyButton);
        inventory2.setItem(8, readyButton);

        Bukkit.getPluginManager().registerEvents(this, Bukkit.getPluginManager().getPlugin("ResetWorld"));

        startExpirationTimer();
    }

    private void startExpirationTimer() {
        expirationTask = new BukkitRunnable() {
            @Override
            public void run() {
                if (!tradeStarted) {
                    player1.sendMessage(ResetWorld.getPrefix() + "§4Deine Handelsanfrage an §e" + player2.getName() + " §4ist abgelaufen.");
                    player2.sendMessage(ResetWorld.getPrefix() + "§4Die Handelsanfrage von §e" + player1.getName() + " §4ist abgelaufen.");
                    unregisterListeners();
                }
            }
        };

        // Ablauf nach 3 Minuten (3 * 60 * 20 Ticks)
        expirationTask.runTaskLater(Bukkit.getPluginManager().getPlugin("ResetWorld"), 3 * 60 * 20);
    }

    // Handelsinventare für beide Spieler öffnen
    public void openTradeInventories() {
        tradeStarted = true;
        expirationTask.cancel();
        player1.openInventory(inventory1);
        player2.openInventory(inventory2);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        // Wenn der Spieler den "Bereit"-Button klickt
        if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.GREEN_WOOL) {
            if (player.equals(player1)) {
                player1Ready = !player1Ready;
                player.sendMessage(player1Ready ? ResetWorld.getPrefix() + "§2Du bist jetzt bereit." : ResetWorld.getPrefix() + "§4Du hast deine Bereitschaft zurückgezogen.");
            } else if (player.equals(player2)) {
                player2Ready = !player2Ready;
                player.sendMessage(player2Ready ? ResetWorld.getPrefix() + "§2Du bist jetzt bereit." : ResetWorld.getPrefix() + "§4Du hast deine Bereitschaft zurückgezogen.");
            }
            event.setCancelled(true);  // Verhindert, dass der "Bereit"-Button bewegt wird

            // Handel abschließen, wenn beide Spieler bereit sind
            if (player1Ready && player2Ready) {
                completeTrade();
            }
            return;
        }

        // Verhindert, dass der "Bereit"-Button bewegt wird
        if (event.getSlot() == 8) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        // Nur abbrechen, wenn der Handel noch nicht abgeschlossen ist und ein Spieler das Inventar schließt
        if (!tradeCompleted && (event.getPlayer().equals(player1) || event.getPlayer().equals(player2))) {
            cancelTrade();
        }
    }

    // Handel abschließen und Items übertragen
    private void completeTrade() {
        if (tradeCompleted) return; // Sicherstellen, dass der Handel nicht doppelt abgeschlossen wird
        tradeCompleted = true;

        // Übertragen der Items von player1 zu player2 und umgekehrt
        transferItems(inventory1, player2);
        transferItems(inventory2, player1);

        player1.sendMessage(ResetWorld.getPrefix() + "§2Handel abgeschlossen!");
        player2.sendMessage(ResetWorld.getPrefix() + "§2Handel abgeschlossen!");

        // Schließe Inventare für beide Spieler
        player1.closeInventory();
        player2.closeInventory();

        // Leeren der Inventare, um doppelte Rückgabe zu verhindern
        inventory1.clear();
        inventory2.clear();
    }

    // Handel abbrechen und Items an die Spieler zurückgeben
    private void cancelTrade() {
        if (tradeCompleted) return; // Falls der Handel bereits abgeschlossen wurde, nichts tun
        tradeCompleted = true;

        // Rückgabe der Items an player1 und player2
        returnItems(inventory1, player1);
        returnItems(inventory2, player2);

        player1.sendMessage(ResetWorld.getPrefix() + "§4Handel abgebrochen. §7Deine Items wurden zurückgegeben.");
        player2.sendMessage(ResetWorld.getPrefix() + "§4Handel abgebrochen. §7Deine Items wurden zurückgegeben.");

        // Leeren der Inventare, um doppelte Rückgabe zu verhindern
        inventory1.clear();
        inventory2.clear();
    }

    // Hilfsfunktion zum Übertragen von Items
    private void transferItems(Inventory from, Player to) {
        for (ItemStack item : from.getContents()) {
            if (item != null && item.getType() != Material.GREEN_WOOL) {
                to.getInventory().addItem(item);
            }
        }
    }

    // Hilfsfunktion zum Zurückgeben von Items
    private void returnItems(Inventory from, Player to) {
        for (ItemStack item : from.getContents()) {
            if (item != null && item.getType() != Material.GREEN_WOOL) {
                to.getInventory().addItem(item);
            }
        }
    }

    private void unregisterListeners() {
        InventoryCloseEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
    }
}
