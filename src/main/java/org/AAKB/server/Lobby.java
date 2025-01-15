package org.AAKB.server;

import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Lobby {

    private static final ArrayList<Integer> allowedNumberOfPlayers = new ArrayList<>(Arrays.asList(2,3,4,6));

    private final ArrayList<Rookie> rookieList = new ArrayList<>();

    private final AtomicInteger totalPlayers = new AtomicInteger(0);

    private final AtomicInteger currentPlayers = new AtomicInteger(0);

    private final ServerSocket serverSocket;

    public Lobby(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run(){
        try {
            while((totalPlayers.get() == 0 && currentPlayers.get() <6) || (totalPlayers.get() != 0 && currentPlayers.get() < totalPlayers.get())) {
                addRookie();
                if(totalPlayers.get() != 0 & currentPlayers.get() != totalPlayers.get()) {
                    broadcast("LOBBY: Waiting for " + (totalPlayers.get() - currentPlayers.get()) + " player(s) to jon...");
                }
            }
            broadcast("LOBBY: All players have been join, preparing the game...");
            createNewGame(rookieList);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewGame(ArrayList<Rookie> rookieList) throws Exception {
        new Game(rookieList, totalPlayers.get());
    }


    private void addRookie() throws Exception {
        Rookie rookie = new Rookie(serverSocket.accept(), currentPlayers.incrementAndGet());
        synchronized (rookieList) {
            rookieList.add(rookie);
            int numberOfPlayers = rookie.askForNumberOfPlayers();
            if(numberOfPlayers != -1) {
                totalPlayers.addAndGet(numberOfPlayers);
                broadcast("CHOOSE: Game for "+ totalPlayers.get() + " player(s) has been chosen.");
            }
            if(totalPlayers.get() != 0 & rookie.getId() != 1) {
                rookie.sendMessage("LOBBY: Game for "+ totalPlayers.get() + " player(s) has been chosen.");
            }
        }
    }

    public static boolean validateNumberOfPlayers(int number){

        return allowedNumberOfPlayers.contains(number);
    }

    private void broadcast(String message){
        for(Rookie rookie: rookieList) {
            rookie.sendMessage(message);
        }
    }

    private void closeAllInOut(){
        for(Rookie rookie: rookieList) {
            rookie.closeInOut();
        }
    }
}
