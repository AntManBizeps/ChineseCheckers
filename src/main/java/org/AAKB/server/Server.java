package org.AAKB.server;

import java.net.*;

import static org.AAKB.constants.ConstantProperties.SERVER_PORT;

public class Server {
    public static void main(String[] args) {
        System.out.println("Chinese Checkers Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {

            Lobby lobby = new Lobby(serverSocket);
            lobby.run();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}