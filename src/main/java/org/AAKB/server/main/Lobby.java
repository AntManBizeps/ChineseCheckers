package org.AAKB.server.main;

import org.AAKB.server.player.Rookie;

import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Lobby {

    private static final ArrayList<Integer> allowedNumberOfPlayers = new ArrayList<>(Arrays.asList(2,3,4,6));

    private final ArrayList<Rookie> rookieList = new ArrayList<>();

    private final AtomicInteger totalPlayers = new AtomicInteger(0);

    private final AtomicInteger currentPlayers = new AtomicInteger(0);

    private final AtomicInteger realPlayers = new AtomicInteger(0);

    private final AtomicInteger botPlayers = new AtomicInteger(0);

    private final ServerSocket serverSocket;

    public Lobby(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void run(){
        try {
            while((totalPlayers.get() == 0 && currentPlayers.get() <6) || (totalPlayers.get() != 0 && currentPlayers.get() < realPlayers.get()) || totalPlayers.get() > 7) {
                addRookie();
                if(totalPlayers.get() != 0 & currentPlayers.get() != realPlayers.get()) {
                    broadcast("LOBBY Waiting for " + (realPlayers.get() - currentPlayers.get()) + " player(s) to jon...");
                }
            }
            if(totalPlayers.get() > 0 && totalPlayers.get() < 7) {
                broadcast("LOBBY All players have been join, preparing the game...");
                createNewGame(rookieList);
            } else if(totalPlayers.get() > 7) {
                new Replay(rookieList);
            }


        } catch (Exception e) {
            broadcast("LOBBY Error: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void createNewGame(ArrayList<Rookie> rookieList) throws Exception {
        new Game(rookieList, botPlayers.get());
    }


    private void addRookie() throws Exception {
        Rookie rookie = new Rookie(serverSocket.accept(), currentPlayers.incrementAndGet());
        synchronized (rookieList) {
            rookieList.add(rookie);
            System.out.println("added client");
            String numberOfPlayers = rookie.askForNumberOfPlayers();
            if(!numberOfPlayers.isEmpty()) {
                int numberOfRealPlayers = Integer.parseInt(numberOfPlayers.substring(0,1));
                int numberOfBots = Integer.parseInt(numberOfPlayers.substring(2,3));
                System.out.println(numberOfPlayers);
                totalPlayers.addAndGet(numberOfRealPlayers+numberOfBots);
                botPlayers.addAndGet(numberOfBots);
                realPlayers.addAndGet(numberOfRealPlayers);
                broadcast("LOBBY Game for "+ totalPlayers.get() + " player(s) has been chosen.");
            }
            if(totalPlayers.get() != 0 & rookie.getId() != 1) {
                rookie.sendMessage("LOBBY Game for "+ totalPlayers.get() + " player(s) has been chosen.");
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
            try {
                rookie.getCommunicationManager().close();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
