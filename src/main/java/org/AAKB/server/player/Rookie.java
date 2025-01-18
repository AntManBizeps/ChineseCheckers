package org.AAKB.server.player;

import org.AAKB.server.main.Lobby;

import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Rookie{

    private AtomicInteger id;

    private final CommunicationManager communicationManager;

    public Rookie(Socket socket, Integer id) throws Exception {
        this.id = new AtomicInteger(id);
        try{
            this.communicationManager = new CommunicationManager(socket);
            communicationManager.writeLine("Congratulations, you have been connected to the server. Please wait...");
        } catch (Exception e) {
            throw new Exception("Rookie can't establish connection");
        }
    }

    public int askForNumberOfPlayers() {
        if(id.get() == 1){
            while(true){
                communicationManager.writeLine("Choose number of players (2, 3, 4, 6): ");
                int input = 0;
                try {
                    input = Integer.parseInt(communicationManager.readLine());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(Lobby.validateNumberOfPlayers(input)){
                    return input;
                }
            }
        } else return -1;

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
