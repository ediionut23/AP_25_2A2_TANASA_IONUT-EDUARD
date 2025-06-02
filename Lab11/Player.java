package org.example;

import java.io.PrintWriter;
import java.net.Socket;

public class Player {
    private String name;
    private Socket socket;
    private PrintWriter out;
    private long remainingTime;

    public Player(String name, Socket socket, PrintWriter out, long startingTime) {
        this.name = name;
        this.socket = socket;
        this.out = out;
        this.remainingTime = startingTime;
    }

    public String getName() {
        return name;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public void deductTime(long time) {
        this.remainingTime -= time;
    }
}