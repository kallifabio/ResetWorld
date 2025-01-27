/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 11.09.2024 um 18:56
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.listeners
 */

package de.kallifabio.resetworld.listeners;

import de.kallifabio.resetworld.ResetWorld;
import de.kallifabio.resetworld.enums.PlayerSizeType;
import de.kallifabio.resetworld.managers.*;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scoreboard.*;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class PlayerListener implements Listener {

    Player player;
    private final Random random = new Random();
    private final HashMap<Player, Location> playerLocations = new HashMap<>();

    private final Location platformCenter = new Location(Bukkit.getWorld("world"), -9, 155, 220);
    private final Location platformSpawnLocation = new Location(Bukkit.getWorld("world"), -8, 155, 232, 180.0f, -0.0f);
    private final double platformRadius = 20.0;
    private final Location teleportTriggerLocation = new Location(Bukkit.getWorld("world"), -9, 67, 219);
    private final double teleportPlatformRadius = 11.0;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        player = event.getPlayer();
        event.setJoinMessage(ResetWorld.getPrefix() + "§7" + player.getName() + " §aist jetzt endlich da");
        player.setAllowFlight(false);

        Difficulty difficulty = Difficulty.NORMAL;
        Bukkit.getWorlds().forEach(world -> {
            world.setDifficulty(difficulty);
        });

        updatePlayerTag(player);

        if (MaintenanceManager.isMaintenance() && !player.getName().equals("kallifabio") && !player.getName().equals("BeFizzi")) {
            event.getPlayer().kickPlayer("§4§lDieser Server befindet sich derzeit in Wartung");
        }

        //player.getAttribute(Attribute.GENERIC_SCALE).setBaseValue(PlayerSizeType.NORMAL.getScale());

        /*if (player.getName().equals("kallifabio")) {
            player.getAttribute(Attribute.GENERIC_JUMP_STRENGTH).setBaseValue(PlayerSizeType.NORMAL.getScale());
        }*/

        String status = StatusManager.getStatusPlayer(player.getName());
        List<String> playerInStatus = StatusManager.getStatusPlayerList(status);
        ChatColor color = StatusManager.getStatusColor(status);

        if (playerInStatus.isEmpty()) {
            player.sendMessage(ResetWorld.getPrefix() + "§cDu bist in keinem Status");
        } else {
            for (String playerName : playerInStatus) {
                Player targetPlayer = Bukkit.getPlayer(playerName);
                if (targetPlayer != null) {
                    targetPlayer.sendMessage(ResetWorld.getPrefix() + "§7Du bist in dem Status " + color + status);
                }
            }
        }
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        if (MaintenanceManager.isMaintenance()) {
            String motd = "§l§r        --<§e----- §fMinecraft ChaosCraft §e-----§r>--§r\n" +
                    "§cWartung §7- §eDer Server ist momentan nicht verfügbar";
            event.setMotd(motd);
            event.setMaxPlayers(0);
        } else {
            String motd = "§l§r        --<§e----- §fMinecraft ChaosCraft §e-----§r>--§r\n" +
                    "§l  §c                 [1.21] §fHosted by Fizzi";
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

    @EventHandler
    public void onPlayerKick(PlayerKickEvent event) {
        player = event.getPlayer();
        if (!MaintenanceManager.isMaintenance()) {
            event.setReason(ResetWorld.getPrefix() + "§7Du wurdest gekickt. Es kann sein, dass dein Status geändert wurde");
        }

        Location location = player.getLocation();
        playerLocations.put(player, location);
        LastLocationManager.setLastLocation(player.getName(), location);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        player = event.getEntity();
        if (player.getKiller() != null) {
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

        int currentDeathCount = DeathManager.getDeath(player.getName());

        if (currentDeathCount == -1) { // Annahme: -1 bedeutet, Spieler ist noch nicht in der Config
            currentDeathCount = 0;
        }

        DeathManager.setDeath(player.getName(), currentDeathCount + 1);

    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        Location respawnLocation = event.getRespawnLocation();
        Location worldSpawn = player.getWorld().getSpawnLocation();
        double tolerance = 0.0;

        player.sendMessage("Respawn Location: " + respawnLocation);
        player.sendMessage("World Spawn Location: " + worldSpawn);

        if (respawnLocation.distance(worldSpawn) <= tolerance) {
            player.sendMessage("Respawning at world spawn, teleporting to platform...");
            teleportToPlatform(player);
        }

        Location bedSpawnLocation = player.getBedSpawnLocation();
        player.sendMessage("Bed Spawn Location: " + bedSpawnLocation);

        if (bedSpawnLocation == null) {
            player.sendMessage("No bed spawn, teleporting to platform...");
            teleportToPlatform(player);
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

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        player = event.getPlayer();
        if (isOnPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL || isOnTeleportPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        player = event.getPlayer();
        if (isOnPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL || isOnTeleportPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerPickupItem(PlayerPickupItemEvent event) {
        player = event.getPlayer();
        if (isOnPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL || isOnTeleportPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        player = event.getPlayer();
        if (isOnPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL || isOnTeleportPlatform(player.getLocation()) && player.getGameMode() == GameMode.SURVIVAL) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Location location = player.getLocation();

            if (isOnPlatform(location) || isOnTeleportPlatform(location)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player damaged = (Player) event.getEntity();
            Location location = damaged.getLocation();

            if (isOnPlatform(location) || isOnTeleportPlatform(location)) {
                event.setCancelled(true);
                return;
            }
        }

        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player carrier = (Player) event.getDamager();
            Player target = (Player) event.getEntity();

            if (ResetWorld.getInstance().getCarryingMap().containsKey(carrier.getUniqueId()) && ResetWorld.getInstance().getCarryingMap().get(carrier.getUniqueId()).equals(target.getUniqueId())) {
                // Entferne die Passagier-Verbindung
                carrier.removePassenger(target);

                // Werfe den Spieler weg
                Location loc = carrier.getLocation();
                Vector throwDirection = loc.getDirection().multiply(5).setX(2); // Richtung und Wurfkraft
                target.setVelocity(throwDirection);

                ResetWorld.getInstance().getCarryingMap().remove(carrier.getUniqueId());
            }
        }

    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            Location location = player.getLocation();

            if (isOnPlatform(location) || isOnTeleportPlatform(location)) {
                event.setCancelled(true);
            }
        }

    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        Location location = event.getBlock().getLocation();

        if (isOnPlatform(location) || isOnTeleportPlatform(location)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        Location location = event.getLocation();

        if (isOnPlatform(location) || isOnTeleportPlatform(location)) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onBlockGrow(BlockGrowEvent event) {
        Location location = event.getBlock().getLocation();

        if (isOnPlatform(location) && event.getBlock().getType() == Material.WHEAT ||
                isOnTeleportPlatform(location) && event.getBlock().getType() == Material.WHEAT ||
                isOnPlatform(location) && event.getBlock().getType() == Material.SUGAR_CANE ||
                isOnTeleportPlatform(location) && event.getBlock().getType() == Material.SUGAR_CANE) {
            event.setCancelled(true);
        }

    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.FARMLAND && event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            if (player.getGameMode() == GameMode.SURVIVAL) {
                Location location = block.getLocation();

                if (isOnPlatform(location) || isOnTeleportPlatform(location)) {
                    event.setCancelled(true);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
        player = event.getPlayer();
        if (ResetWorld.getInstance().getPlayerPoses().containsKey(player) && event.isSneaking()) {
            ResetWorld.getInstance().removePose(player);
        }

        if (ResetWorld.getInstance().getCarryingMap().containsKey(player.getUniqueId())) {
            Player target = Bukkit.getPlayer(ResetWorld.getInstance().getCarryingMap().get(player.getUniqueId()));
            if (target != null) {
                player.removePassenger(target);
                ResetWorld.getInstance().getCarryingMap().remove(player.getUniqueId());
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        Player carrier = event.getPlayer();

        // Stelle sicher, dass das Ziel ein Spieler ist
        if (!(event.getRightClicked() instanceof Player)) return;

        Player target = (Player) event.getRightClicked();

        // Spieler kann niemanden tragen, wenn er bereits jemanden trägt
        if (ResetWorld.getInstance().getCarryingMap().containsKey(carrier.getUniqueId())) {
            return;
        }

        // Spieler kann nicht aufgenommen werden, wenn er getragen wird
        if (ResetWorld.getInstance().getCarryingMap().containsValue(target.getUniqueId())) {
            return;
        }

        // Spieler aufnehmen
        ResetWorld.getInstance().getCarryingMap().put(carrier.getUniqueId(), target.getUniqueId());

        // Setze den Zielspieler als Passagier
        carrier.addPassenger(target);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        if (isOnTeleportTrigger(playerLocation)) {
            teleportToPlatform(player);
        }


        //PlatformBorderManager borderManager = new PlatformBorderManager(platformCenter, platformRadius);
        //borderManager.handlePlayerMovement(event);
    }

    private boolean isOnPlatform(Location location) {
        World world = platformCenter.getWorld();
        if (location.getWorld() == null || !location.getWorld().equals(world)) {
            return false;
        }

        double distanceSquared = location.distanceSquared(platformCenter);
        return distanceSquared <= platformRadius * platformRadius;
    }

    private boolean isOnTeleportPlatform(Location location) {
        World world = teleportTriggerLocation.getWorld();
        if (location.getWorld() == null || !location.getWorld().equals(world)) {
            return false;
        }

        double distanceSquared = location.distanceSquared(teleportTriggerLocation);
        return distanceSquared <= teleportPlatformRadius * teleportPlatformRadius;
    }

    private boolean isOnTeleportTrigger(Location location) {
        World world = teleportTriggerLocation.getWorld();
        if (location.getWorld() == null || !location.getWorld().equals(world)) {
            return false;
        }

        return location.getBlockX() == teleportTriggerLocation.getBlockX()
                && location.getBlockY() == teleportTriggerLocation.getBlockY()
                && location.getBlockZ() == teleportTriggerLocation.getBlockZ();
    }

    private void teleportToPlatform(Player player) {
        player.teleport(platformSpawnLocation);
    }

    private void updatePlayerTag(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Objective objective = scoreboard.getObjective("deaths");

        if (objective == null) {
            objective = scoreboard.registerNewObjective("deaths", "dummy", "§4Tod(e)");
            objective.setDisplaySlot(DisplaySlot.BELOW_NAME);
        }

        Score score = objective.getScore(player.getName());
        Bukkit.getScheduler().runTaskTimerAsynchronously(ResetWorld.getInstance(), () -> {
            score.setScore(DeathManager.getDeath(player.getName()));
        }, 20L, 20L);

        player.setScoreboard(scoreboard);

        String status = StatusManager.getStatusPlayer(player.getName());

        if (status != null) {
            ChatColor color = StatusManager.getStatusColor(status);
            String teamName = status.substring(0, Math.min(status.length(), 16));

            Team team = scoreboard.getTeam(teamName);
            if (team == null) {
                team = scoreboard.registerNewTeam(teamName);
                //team.setOption(Team.Option.COLLISION_RULE, Team.OptionStatus.NEVER);
            }

            team.setPrefix(color + "[" + status + "] " + ChatColor.RESET);
            team.addEntry(player.getName());
        }
    }

    public Location getPlatformCenter() {
        return platformCenter;
    }

    public Location getPlatformSpawnLocation() {
        return platformSpawnLocation;
    }

    public double getPlatformRadius() {
        return platformRadius;
    }

    public Location getTeleportTriggerLocation() {
        return teleportTriggerLocation;
    }

    public double getTeleportPlatformRadius() {
        return teleportPlatformRadius;
    }

    public HashMap<Player, Location> getPlayerLocations() {
        return playerLocations;
    }
}
