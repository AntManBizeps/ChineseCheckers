package org.AAKB.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static org.AAKB.server.ClientHandlerHelper.*;


public class ClientHandler extends Thread {
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static Map<PrintWriter, Integer> playerIds = new HashMap<>();
    private static AtomicInteger totalPlayers = new AtomicInteger(0);
    private static AtomicInteger currentPlayers = new AtomicInteger(0);
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

            out.println("You have been connected to the server successfully...");

            synchronized (clientWriters) {
                playerId = currentPlayers.incrementAndGet(); // Assign player ID (1-based), increment currentPlayers by 1
                playerIds.put(out, playerId);
            }

            // Handle player setup
            while(totalPlayers.get() == 0) {
                out.println("Enter the correct number of players:");
                int givenNumber = 0;
                try {
                    givenNumber = Integer.parseInt(in.readLine());
                } catch (NumberFormatException e) {
                }
                if(validateNumberOfPlayers(givenNumber)){
                    totalPlayers.set(givenNumber);
                }
            }

            if (currentPlayers.get() == totalPlayers.get()) {
                broadcast("All players connected. Type 'start' to begin the game.");
            } else {
                broadcast("Waiting for " + (totalPlayers.get() - currentPlayers.get()) + " more players to join...");
            }

            while (true) {
                String input = in.readLine();
                if (input == null) break;

                if (input.equalsIgnoreCase("start") && currentPlayers.get() == totalPlayers.get()) {
                    gameStarted = true;
                    broadcast("Game started! Player 1's turn.");
                } else if (gameStarted) {
                    if (playerId == currentTurn + 1) { // Check if it's the player's turn
                        broadcast("Player " + playerId + " moved: " + input);
                        currentTurn = (currentTurn + 1) % currentPlayers.get();
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
                currentPlayers.decrementAndGet();
                if (currentPlayers.get() == 0) {
                    totalPlayers.set(0);
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
