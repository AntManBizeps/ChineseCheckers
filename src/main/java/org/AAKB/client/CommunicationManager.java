package org.AAKB.client;

import static org.AAKB.constants.ConstantProperties.SERVER_ADDRESS;
import static org.AAKB.constants.ConstantProperties.SERVER_PORT;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Handles communication with the server.
 */
class CommunicationManager {
    private Socket socket;
    private BufferedReader input;
    private PrintWriter output;

    /**
     * Creates a communication manager and establishes a connection to the server.
     *
     * @param host the server's hostname or IP address.
     * @param port the port number on which the server is listening.
     * @throws Exception if the connection cannot be established.
     */
    CommunicationManager() throws Exception {
        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            output = new PrintWriter(socket.getOutputStream(), true);
            input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            throw new Exception("Unable to connect to the server.");
        }
    }

    /**
     * Reads a message from the server.
     *
     * @return the message received from the server.
     * @throws Exception if the connection is lost.
     */
    synchronized String readMessage() throws Exception {
        try {
            return input.readLine();
        } catch (Exception e) {
            throw new Exception("Connection to the server was lost.");
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param message the message to send.
     */
    void sendMessage(String message) {
        output.println(message);
    }
}
