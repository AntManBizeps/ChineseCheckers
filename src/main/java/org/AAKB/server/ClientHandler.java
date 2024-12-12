package org.AAKB.server;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private Server server;
    private Player player;

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        this.server = server;
        try {
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                Message message = (Message) in.readObject();
                handleMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
            disconnect();
        }
    }

    private void handleMessage(Message message) {
        switch (message.getType()) {
            case "join":
                String playerName = message.getContent();
                this.player = new Player(playerName, server.getPlayerCount() + 1);
                server.addPlayer(this);
                break;

            case "start":
                if (server.isGameReadyToStart(this)) {
                    server.startGame();
                } else {
                    sendMessage(new Message("error", "Game cannot be started yet."));
                }
                break;

            case "move":
                if (server.isPlayerTurn(this)) {
                    server.broadcastMessage(message);
                    server.nextTurn();
                } else {
                    sendMessage(new Message("error", "It's not your turn."));
                }
                break;

            default:
                sendMessage(new Message("error", "Unknown message type."));
        }
    }

    public void sendMessage(Message message) {
        try {
            out.writeObject(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Player getPlayer() {
        return player;
    }

    private void disconnect() {
        try {
            socket.close();
            server.removePlayer(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}