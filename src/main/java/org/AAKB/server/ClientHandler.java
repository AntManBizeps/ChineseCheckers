//package org.AAKB.server;
//
//import org.AAKB.server.board.Move;
//import org.AAKB.server.player.CommunicationManager;
//
//import java.net.*;
//
//
//public class ClientHandler extends AbstractClientHandler {
//    private CommunicationManager communicationManager;
//    private int playerId;
//    private static Object lock = new Object();
//
//    public ClientHandler(Socket socket) throws Exception {
//        communicationManager = new CommunicationManager(socket);
//        synchronized (clientHandlers) {
//            clientHandlers.add(this);
//        }
//    }
//
//    @Override
//    public void run() {
//        try {
//            welcomeAndWait();
//
//            communicationManager.writeLine("Game started! Player 1's turn.");
//            while (true) {
//                String input = communicationManager.readLine();
//                if (input == null) break;
//
//                if (gameStarted) {
//                    if (playerId == currentTurn + 1 && input.startsWith("MOVE")) { // Check if it's the player's turn
//                        tryToMove(input);
//
//                    } else {
//                        communicationManager.writeLine("It's not your turn!");
//                    }
//                } else {
//                    communicationManager.writeLine("Game has not started yet.");
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                communicationManager.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            synchronized (clientHandlers) {
//                clientHandlers.remove(this);
//                playerIds.remove(this);
//                currentPlayers.decrementAndGet();
//                if (currentPlayers.get() == 0) {
//                    totalPlayers.set(0);
//                    gameStarted = false;
//                }
//            }
//        }
//    }
//
//    private boolean tryToMove(String input) throws Exception {
//        Move move = InputInterpeter.getMoveFromString(input);
//        if (move == null) {
//            communicationManager.writeLine("Incorrect number of coordinates");
//            return false;
//        }
//        broadcast("Player " + playerId + " moved: " + input);
//        nextTurn();
//        return true;
//
//    }
//
//
//
//    private void nextTurn() throws Exception {
//        currentTurn = (currentTurn + 1) % currentPlayers.get();
//        broadcast("Player " + (currentTurn + 1) + "'s turn.");
//    }
//
//    public void sendMessage(String message) throws Exception {
//        communicationManager.writeLine(message);
//    }
//
//    public String readMessage() throws Exception {
//        return communicationManager.readLine();
//    }
//
//    private void broadcast(String message) throws Exception {
//        synchronized (clientHandlers) {
//            for (ClientHandler client : clientHandlers) {
//                client.sendMessage(message);
//            }
//        }
//    }
//
//    private void welcomeAndWait(){
//        try {
//            communicationManager.writeLine("You have been connected to the server successfully...");
//
//            synchronized (clientHandlers) {
//                playerId = currentPlayers.incrementAndGet(); // Assign player ID (1-based), increment currentPlayers by 1
//                playerIds.put(this, playerId);
//            }
//
//            // Handle player setup
//            while (totalPlayers.get() == 0) {
//                communicationManager.writeLine("Enter the number of players(2, 3, 4, 6):");
//                int givenNumber = 0;
//                try {
//                    givenNumber = Integer.parseInt(communicationManager.readLine());
//                } catch (NumberFormatException e) {
//                }
//                if (validateNumberOfPlayers(givenNumber)) {
//                    totalPlayers.set(givenNumber);
//                    broadcast("Game for " + totalPlayers.get() + " players has been chosen.");
//                }
//            }
//
//            if (currentPlayers.get() == totalPlayers.get()) {
//                synchronized(lock) {
//                    lock.notifyAll();
//                }
//                gameStarted = true;
//            } else {
//                broadcast("Waiting for " + (totalPlayers.get() - currentPlayers.get()) + " more players to join...");
//                waitingForPlayers();
//            }
//            communicationManager.writeLine("All players connected. Let's start the game!");
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private void waitingForPlayers(){
//        synchronized (lock) {
//            try {
//                lock.wait();
//            } catch (InterruptedException e) {
//                Thread.currentThread().interrupt();
//            }
//        }
//    }
//}
//
//
