package org.example;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class GameServer {
    private ServerSocket serverSocket;
    private boolean running = true;

    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);
    }

    public void start() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientThread(clientSocket, this)).start();
            } catch (IOException e) {
                System.out.println("Error accepting connection: " + e.getMessage());
            }
        }
    }

    public void stop() throws IOException {
        running = false;
        serverSocket.close();
        System.out.println("Server stopped.");
    }

    public boolean isRunning() {
        return running;
    }

    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer(1234);
        server.start();
    }
}
