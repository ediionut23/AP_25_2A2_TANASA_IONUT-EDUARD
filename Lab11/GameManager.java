package org.example;

import java.util.HashMap;
import java.util.Map;

public class GameManager {
    private static GameManager instance;
    private Map<String, HexGame> games = new HashMap<>();

    private GameManager() {}

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public String createGame(String gameId, Player player) {
        if (games.containsKey(gameId)) return "Game already exists.";
        games.put(gameId, new HexGame(player));
        return "Game created with ID: " + gameId;
    }

    public String joinGame(String gameId, Player player) {
        HexGame game = games.get(gameId);
        if (game == null) return "Game not found.";
        if (game.isFull()) return "Game is full.";
        boolean joined = game.join(player);
        return joined ? "Joined game: " + gameId : "Could not join game.";
    }

    public String submitMove(String gameId, Player player, String move) {
        HexGame game = games.get(gameId);
        if (game == null) return "Game not found.";
        return game.submitMove(player, move);
    }
}