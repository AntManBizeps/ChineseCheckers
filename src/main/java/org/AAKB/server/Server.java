package org.AAKB.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import static org.AAKB.constants.ConstantProperties.SERVER_PORT;

public class Server {
    private List<ClientHandler> clientHandlers;
    private Game game;

    public Server() {
        this.clientHandlers = new ArrayList<>();
        this.game = new Game();
    }

    public void start() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            System.out.println("Server is running on port " + SERVER_PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(socket, this);
                clientHandlers.add(clientHandler);
                new Thread(clientHandler).start();
            }
        }
    }

    public synchronized void addPlayer(ClientHandler clientHandler) {
        Player player = clientHandler.getPlayer();
        game.addPlayer(player);
        System.out.println("Player joined: " + player.getName());
    }

    public synchronized void removePlayer(ClientHandler clientHandler) {
        clientHandlers.remove(clientHandler);
        Player player = clientHandler.getPlayer();
        if (player != null) {
            game.getPlayers().remove(player);
            System.out.println("Player left: " + player.getName());
        }
    }

    public synchronized boolean isGameReadyToStart(ClientHandler clientHandler) {
        return !game.isGameStarted() && game.getPlayers().size() > 1 && clientHandlers.contains(clientHandler);
    }

    public synchronized void startGame() {
        game.startGame();
        broadcastMessage(new Message("gameStarted", "The game has started!"));
    }

    public synchronized boolean isPlayerTurn(ClientHandler clientHandler) {
        return clientHandler.getPlayer().equals(game.getCurrentPlayer());
    }

    public synchronized void nextTurn() {
        game.nextTurn();
        Player currentPlayer = game.getCurrentPlayer();
        broadcastMessage(new Message("nextTurn", "It's " + currentPlayer.getName() + "'s turn."));
    }

    public synchronized void broadcastMessage(Message message) {
        for (ClientHandler handler : clientHandlers) {
            handler.sendMessage(message);
        }
    }

    public synchronized int getPlayerCount() {
        return game.getPlayers().size();
    }

    public static void main(String[] args) throws Exception {
        new Server().start();
    }
}