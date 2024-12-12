package org.AAKB.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import org.AAKB.server.Message;

import static org.AAKB.constants.ConstantProperties.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Enter your name:");
            String name = scanner.nextLine();
            out.writeObject(new Message("join", name));

            while (true) {
                Message serverMessage = (Message) in.readObject();
                if ("gameStarted".equals(serverMessage.getType())) {
                    System.out.println("Game has started!");
                } else if ("move".equals(serverMessage.getType())) {
                    System.out.println("Move received: " + serverMessage.getContent());
                }

                if (serverMessage == null || gameIsOver(serverMessage)) {
                    break;
                }

                System.out.println("Your turn! Enter your move:");
                String move = scanner.nextLine();
                out.writeObject(new Message("move", move));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean gameIsOver(Message message) {
        return "gameOver".equals(message.getType());
    }
}

