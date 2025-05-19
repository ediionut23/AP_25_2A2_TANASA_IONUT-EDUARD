package org.example;

import org.example.GameServer;

import java.io.*;
import java.net.Socket;

public class ClientThread implements Runnable {
    private Socket socket;
    private GameServer server;

    public ClientThread(Socket socket, GameServer server) {
        this.socket = socket;
        this.server = server;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                if (request.equalsIgnoreCase("stop")) {
                    out.println("Server stopped");
                    server.stop();
                    break;
                } else {
                    out.println("Server received the request: " + request);
                }
            }
        } catch (IOException e) {
            System.out.println("ClientThread error: " + e.getMessage());
        }
    }
}
