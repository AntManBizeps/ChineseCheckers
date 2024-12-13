package org.AAKB.client;

import java.io.*;
import java.net.*;

import static org.AAKB.constants.ConstantProperties.*;

public class Client {
    public static void main(String[] args) {
        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in))) {

            new Thread(() -> {
                try {
                    String response;
                    while ((response = in.readLine()) != null) {
                        System.out.println(response);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            String userInputLine;
            while ((userInputLine = userInput.readLine()) != null) {
                out.println(userInputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

