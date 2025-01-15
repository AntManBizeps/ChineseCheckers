package org.AAKB.server.player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CommunicationManager {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;

    public CommunicationManager(Socket socket, BufferedReader in, PrintWriter out) throws Exception {
        this.socket = socket;
        this.in = in;
        this.out = out;
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
