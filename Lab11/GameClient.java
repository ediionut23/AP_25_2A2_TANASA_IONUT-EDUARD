package org.example;
import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class GameClient {
    public static void main(String[] args) {
        String serverAddress = "localhost";
        int port = 1234;

        try (
                Socket socket = new Socket(serverAddress, port);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Connected to the server");

            String input;
            while (true) {
                System.out.print("Command: ");
                input = scanner.nextLine();

                if (input.equalsIgnoreCase("exit")) {
                    break;
                }

                out.println(input);
                String response = in.readLine();
                System.out.println("Server: " + response);
            }

        } catch (IOException e) {
            System.out.println("Client error: " + e.getMessage());
        }
    }
}
