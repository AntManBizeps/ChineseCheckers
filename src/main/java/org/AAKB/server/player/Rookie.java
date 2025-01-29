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
            communicationManager.writeLine("LOBBY Congratulations, you have been connected to the server. Please wait...");
        } catch (Exception e) {
            throw new Exception("FALSE Rookie can't establish connection");
        }
    }

    public String askForNumberOfPlayers() {
        if(id.get() == 1){
            while(true){
                communicationManager.writeLine("CHOOSE Choose number of real players and bots");
                int inputRealPlayers = 0;
                int inputBots = 0;
                try {
                    inputRealPlayers = Integer.parseInt(communicationManager.readLine());
                    inputBots = Integer.parseInt(communicationManager.readLine());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                if(Lobby.validateNumberOfPlayers(inputRealPlayers+inputBots)){
                    return (inputRealPlayers+","+inputBots);
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
