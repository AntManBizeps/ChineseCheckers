package org.AAKB.server.player;

import org.AAKB.server.board.ClassicBoardFactory;
import org.AAKB.server.board.Move;
import org.AAKB.server.main.Lobby;
import org.AAKB.server.movement.ClassicMovementStrategy;
import org.AAKB.server.movement.GameMaster;

import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Rookie implements Runnable {

    private AtomicInteger id;

    Map<Integer, String> boardStates = new HashMap<Integer, String>();

    private final CommunicationManager communicationManager;

    public Rookie(Socket socket, Integer id) throws Exception  {
        this.id = new AtomicInteger(id);
        try{
            this.communicationManager = new CommunicationManager(socket);
            communicationManager.writeLine("LOBBY Congratulations, you have been connected to the server. Please wait...");
        } catch (Exception e) {
            throw new Exception("FALSE Rookie can't establish connection");
        }
    }

    public void setBoardStates(Map<Integer, String> boardStates) {
        this.boardStates = boardStates;
    }

    @Override
    public void run() {
        int counter = 1;
        communicationManager.writeLine(boardStates.get(1));
        while(counter < boardStates.size()) {
            try {
                String input = communicationManager.readLine();
                if (input == null) break;
                if(input.startsWith("REDO")) {
                    counter++;
                    communicationManager.writeLine(boardStates.get(counter));
                } else if(input.startsWith("UNDO")) {
                    if (counter < 2) {
                        continue;
                    }
                    counter--;
                    communicationManager.writeLine(boardStates.get(counter));
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
        communicationManager.writeLine("GAME OVER ");


    }

    public String askForNumberOfPlayers() {
        if(id.get() == 1){
            while(true){
                communicationManager.writeLine("CHOOSE Choose number of real players and bots");
                int inputPlayers = 0;
                int inputBots = 0;
                try {
                    String input = communicationManager.readLine();
                    if(input.startsWith("START")){
                        inputPlayers = Integer.parseInt(input.substring(6,7));
                        inputBots = Integer.parseInt(input.substring(8,9));
                    }
                    if(input.startsWith("LOAD")){
                        return "LOAD";
                    }

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(Lobby.validateNumberOfPlayers(inputPlayers)){
                    return ((inputPlayers-inputBots)+","+inputBots);
                }
            }
        } else return "";

    }

    public void sendMessage(String message){
        communicationManager.writeLine(message);
    }

    public int getId() {
        return id.get();
    }

    public CommunicationManager getCommunicationManager() {
        return communicationManager;
    }



}
