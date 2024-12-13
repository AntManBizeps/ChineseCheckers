package org.AAKB.server;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientHandler extends Thread {
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<PrintWriter, Integer> playerIds = new HashMap<>();
    private static int totalPlayers = 0;
    private static int currentPlayers = 0;
    private static boolean gameStarted = false;
    private static int currentTurn = 0;

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private int playerId;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            synchronized (clientWriters) {
                clientWriters.add(out);
            }

            // Handle player setup
            if (totalPlayers == 0) {
                out.println("Enter the number of players:");
                totalPlayers = Integer.parseInt(in.readLine());
                out.println("Waiting for " + totalPlayers + " players to join...");
            }

            synchronized (clientWriters) {
                playerId = currentPlayers + 1; // Assign player ID (1-based)
                playerIds.put(out, playerId);
                currentPlayers++;
                if (currentPlayers == totalPlayers) {
                    out.println("All players connected. Type 'start' to begin the game.");
                }
            }

            while (true) {
                String input = in.readLine();
                if (input == null) break;

                if (input.equalsIgnoreCase("start") && currentPlayers == totalPlayers) {
                    gameStarted = true;
                    broadcast("Game started! Player 1's turn.");
                } else if (gameStarted) {
                    if (playerId == currentTurn + 1) { // Check if it's the player's turn
                        broadcast("Player " + playerId + " moved: " + input);
                        currentTurn = (currentTurn + 1) % currentPlayers;
                        broadcast("Player " + (currentTurn + 1) + "'s turn.");
                    } else {
                        out.println("It's not your turn!");
                    }
                } else {
                    out.println("Game has not started yet.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            synchronized (clientWriters) {
                clientWriters.remove(out);
                playerIds.remove(out);
                currentPlayers--;
                if (currentPlayers == 0) {
                    totalPlayers = 0;
                    gameStarted = false;
                }
            }
        }
    }

    private void broadcast(String message) {
        for (PrintWriter writer : clientWriters) {
            writer.println(message);
        }
    }
}
