/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.09.2024 um 18:56
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.listeners
 */

package de.kallifabio.resetworld.listeners;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.enums.PlayerSizeType;
import de.kallifabio.resetworld.managers.DeathManager;
import de.kallifabio.resetworld.managers.LastLocationManager;
import de.kallifabio.resetworld.managers.MaintenanceManager;
import de.kallifabio.resetworld.managers.StatusManager;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.*;

import java.util.HashMap;
import java.util.Random;

public class PlayerListener implements Listener {

    Player player;
    private final Random random = new Random();
    private final HashMap<Player, Location> playerLocations = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        player = event.getPlayer();
        event.setJoinMessage(ResetWorld.getPrefix() + "§7" + player.getName() + " §aist jetzt endlich da");
        player.setAllowFlight(true);
        if (player.getName().equalsIgnoreCase("kallifabio")) {
            player.setGameMode(GameMode.SPECTATOR);
        } else {
            player.setGameMode(GameMode.SURVIVAL);
        }

        Difficulty difficulty = Difficulty.HARD;
        Bukkit.getWorlds().forEach(world -> {
            world.setDifficulty(difficulty);
        });

        updatePlayerTag(player);

        if (MaintenanceManager.isMaintenance() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
            event.getPlayer().kickPlayer("§4§lDieser Server befindet sich derzeit in Wartung");
        }

        player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(PlayerSizeType.NORMAL.getScale());
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (MaintenanceManager.isMaintenance()) {
            String motd = "§7--<§e----- §fMinecraft ChaosCraft §e-----§7>--\n" +
                    "§cWartung §7- §eDer Server ist momentan nicht verfügbar";
            event.setMotd(motd);
            event.setMaxPlayers(0);
        } else {
            String motd = "§7--<§e----- §fMinecraft ChaosCraft §e-----§7>--\n" +
                    "§c[1.21] §fHosted by Fizzi";
            event.setMotd(motd);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        player = event.getPlayer();
        event.setQuitMessage(ResetWorld.getPrefix() + "§7" + player.getName() + " §cist für heute offline");

        Location location = player.getLocation();
        playerLocations.put(player, location);
        LastLocationManager.setLastLocation(player.getName(), location);
    }

    /*@EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        player = event.getPlayer();
        if (!MaintenanceManager.isMaintenance()) {
            event.setReason(ResetWorld.getPrefix() + "§7Du wurdest gekickt. Es kann sein, dass dein Status geändert wurde");
        }

    }*/

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() != null) {
            if (random.nextDouble() <= 0.1) { // 10% Chance
                ItemStack playerHead = new ItemStack(Material.PLAYER_HEAD, 1);
                SkullMeta skullMeta = (SkullMeta) playerHead.getItemMeta();

                if (skullMeta != null) {
                    skullMeta.setOwningPlayer(event.getEntity());
                    skullMeta.setDisplayName("§6" + event.getEntity().getName() + "'s §7Kopf");
                    playerHead.setItemMeta(skullMeta);
                }

                event.getDrops().add(playerHead);
                event.getEntity().getKiller().sendMessage(ResetWorld.getPrefix() + "§2Du hast den Kopf von §7" + event.getEntity().getName() + " §2erhalten!");
                event.getEntity().getKiller().playSound(event.getEntity().getLocation(), Sound.ENTITY_FIREWORK_ROCKET_LAUNCH, 1, 1);
            }
        }

        int count = 0;
        Player player = event.getEntity();

        if (event.getEntity().isDead()) {
            count++;
            DeathManager.setDeath(player.getName(), count);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        player = event.getPlayer();
        String status = StatusManager.getStatusPlayer(player.getName());
        String message = event.getMessage();

        String formattedMessage = player.getName() + ": " + message;

        if (status != null) {
            ChatColor color = StatusManager.getStatusColor(status);
            event.setFormat(color + "[" + status + "] " + ChatColor.RESET + formattedMessage);
        }
    }

    private void updatePlayerTag(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective("deaths");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("deaths", "dummy", "§4Tod(e)");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }

        Score score = objective.getScore(player.getName());
        score.setScore(DeathManager.getDeath(player.getName()));

        player.setScoreboard(scoreboard);

        String status = StatusManager.getStatusPlayer(player.getName());

        if (status != null) {
            ChatColor color = StatusManager.getStatusColor(status);
            String teamName = status.substring(0, Math.min(status.length(), 16));

            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
            }

            team.setPrefix(color + "[" + status + "] " + ChatColor.RESET);
            team.addEntry(player.getName());
        }
    }
}
