package org.AAKB.server.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationManager {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public CommunicationManager(Socket socket) throws Exception {
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream(), true);
    }

    public String readLine() throws Exception {
        return in.readLine();
    }

    public void writeLine(String message) {
        out.println(message);
    }

    public void close() throws Exception {
        in.close();
        out.close();
        socket.close();
    }
}
