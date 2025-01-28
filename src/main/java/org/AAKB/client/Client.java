package org.AAKB.client;

import static org.AAKB.constants.ConstantProperties.SERVER_ADDRESS;
import static org.AAKB.constants.ConstantProperties.SERVER_PORT;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import org.AAKB.client.board.Board;

public class Client {
    Controller controller;
    private Board board;
    static Client instance;
    GameManager gm;

    private BufferedReader input;
    private PrintWriter output;
    private Socket socket;

    public Client(Controller controller) {
        Client.instance = this;
        this.controller = controller;
        gm = new GameManager(instance, controller);

        System.out.println("Client init");
    }

    public void startClient() {
        try {
            System.out.println("Trying to connect");
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);

            output = new PrintWriter(socket.getOutputStream());
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            new Thread(new ClientListener(input, instance)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void quit(){
        System.out.println("Quitting");
        if(socket!=null && socket.isConnected()){
            try{socket.close();}catch(IOException e){e.printStackTrace();}
        }
    }

    public void send(String message) {
        output.println(message);
        output.flush();
    }

    static class ClientListener implements Runnable {

        Client instance;
        private BufferedReader input;

        public ClientListener(BufferedReader input, Client instance) {
            this.input = input;
            this.instance = instance;

            System.out.println("Client listener initialized");
        }

        @Override
        public void run() {
            try {
                String response;
                while ((response = input.readLine()) != null) {
                    System.out.println(response);
                    instance.handle(response);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }

    public void handle(String message) {
        if (gm != null) {
            gm.handleServerMessage(message);
        } else {
            System.err.println("GameManager is not initialized.");
        }
    }
    
}
