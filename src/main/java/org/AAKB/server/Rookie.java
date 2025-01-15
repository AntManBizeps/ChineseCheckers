package org.AAKB.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Rookie{
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private AtomicInteger id;

    public Rookie(Socket socket, Integer id) throws Exception {
        this.socket = socket;
        this.id = new AtomicInteger(id);
        try{
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println("Congratulations, you have been connected to the server. Please wait...");
        } catch (Exception e) {
            throw new Exception("Rookie can't establish connection");
        }
    }

    public int askForNumberOfPlayers() {
        if(id.get() == 1){
            while(true){
                out.println("Choose number of players (2, 3, 4, 6): ");
                int input = 0;
                try {
                    input = Integer.parseInt(in.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if(Lobby.validateNumberOfPlayers(input)){
                    return input;
                }
            }
        } else return -1;

    }

    public void sendMessage(String message){
        out.println(message);
    }

    public int getId() {
        return id.get();
    }

    public void closeInOut() {
        try {
            in.close();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public BufferedReader getIn() {
        return in;
    }

    public PrintWriter getOut() {
        return out;
    }
}
