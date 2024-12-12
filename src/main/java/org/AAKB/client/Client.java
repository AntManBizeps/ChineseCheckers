package org.AAKB.client;

import java.io.*;
import java.net.*;
import java.util.Scanner;

import static org.AAKB.constants.ConstantProperties.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             Scanner scanner = new Scanner(System.in)) {

            System.out.println("Connected to the server");

            while (true) {
                // Odbierz wiadomość od serwera
                String serverMessage = in.readLine();
                if (serverMessage == null) {
                    System.out.println("Connection closed by server.");
                    break;
                }

                System.out.println(serverMessage);

                // Jeśli to kolej gracza, pozwól mu wpisać ruch
                if (serverMessage.startsWith("Your turn")) {
                    System.out.print("Enter your move or type EXIT to quit: ");
                    String move = scanner.nextLine();

                    if ("EXIT".equalsIgnoreCase(move.trim())) {
                        out.println("EXIT");
                        System.out.println("Exiting game...");
                        break;
                    }

                    out.println(move);
                }

                // Obsługa końca gry
                if (serverMessage.equalsIgnoreCase("Game over")) {
                    System.out.println("The game has ended.");
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

