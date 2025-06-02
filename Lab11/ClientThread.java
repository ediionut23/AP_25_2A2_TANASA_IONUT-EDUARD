package org.example;

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
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            Player player = new Player("Player" + socket.getPort(), socket, out, 5 * 60 * 1000);
            String line;
            while ((line = in.readLine()) != null) {
                String[] parts = line.split(" ");
                String command = parts[0];
                String response;

                GameManager gm = GameManager.getInstance();

                switch (command.toLowerCase()) {
                    case "create":
                        response = gm.createGame(parts[1], player);
                        break;
                    case "join":
                        response = gm.joinGame(parts[1], player);
                        break;
                    case "move":
                        response = gm.submitMove(parts[1], player, parts[2]);
                        break;
                    case "stop":
                        server.stop();
                        response = "Server stopped";
                        break;
                    default:
                        response = "Unknown command.";
                }

                out.println(response);
            }
        } catch (IOException e) {
            System.out.println("ClientThread error: " + e.getMessage());
        }
    }
}