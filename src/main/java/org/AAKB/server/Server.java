package org.AAKB.server;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import static org.AAKB.constants.ConstantProperties.SERVER_PORT;

public class Server {
    public static void main(String[] args) {
        System.out.println("Chinese Checkers Server is running...");
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                new ClientHandler(serverSocket.accept()).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}