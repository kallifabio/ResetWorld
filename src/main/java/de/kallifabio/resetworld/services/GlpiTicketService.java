/**
 * Erstellt von Gamer_Kidd_LP | kallifabio
 * am 15.04.2025 um 00:10
 * Projektname: ResetWorld
 * Packagename: de.kallifabio.resetworld.services
 */

package de.kallifabio.resetworld.services;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.kallifabio.resetworld.ResetWorld;
import org.bukkit.Bukkit;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class GlpiTicketService {

    private static final String GLPI_URL = "https://ticket.chaoscraft.net/apirest.php";
    private static final String APP_TOKEN = "tmGT5YXqPtkDxelNwFHPz1PvlBLj0yRNZaw0dj4l";
    private static final String USERNAME = "glpi";
    private static final String PASSWORD = "glpi";

    public static void createTicket(String player, String reason, String staffPlayer, String duration) {
        Bukkit.getScheduler().runTaskAsynchronously(ResetWorld.getInstance(), () -> {
            try {
                // === Session erstellen ===
                HttpURLConnection conn = (HttpURLConnection) new URL(GLPI_URL + "/initSession").openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("App-Token", APP_TOKEN);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String loginJson = "{\"login\":\"" + USERNAME + "\",\"password\":\"" + PASSWORD + "\"}";
                try (OutputStream os = conn.getOutputStream()) {
                    os.write(loginJson.getBytes(StandardCharsets.UTF_8));
                }

                String response = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                        .lines().collect(Collectors.joining());

                String sessionToken = JsonParser.parseString(response).getAsJsonObject().get("session_token").getAsString();

                // === Ticket erstellen ===
                conn = (HttpURLConnection) new URL(GLPI_URL + "/Ticket").openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("App-Token", APP_TOKEN);
                conn.setRequestProperty("Session-Token", sessionToken);
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);

                String title = "Spieler gebannt: " + player;
                String content = "Spieler: " + player + "\nGrund: " + reason + "\nDauer: " + duration + "\nTeammitglied: " + staffPlayer;

                JsonObject ticketJson = new JsonObject();
                JsonObject input = new JsonObject();
                input.addProperty("name", title);
                input.addProperty("content", content);
                input.addProperty("status", 1); // Offen

                ticketJson.add("input", input);

                try (OutputStream os = conn.getOutputStream()) {
                    os.write(ticketJson.toString().getBytes(StandardCharsets.UTF_8));
                }

                String ticketResponse = new BufferedReader(new InputStreamReader(conn.getInputStream()))
                        .lines().collect(Collectors.joining());

                Bukkit.getConsoleSender().sendMessage("GLPI-Ticket erstellt: " + ticketResponse);

            } catch (Exception e) {
                Bukkit.getConsoleSender().sendMessage("Fehler beim Erstellen des GLPI-Tickets: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
