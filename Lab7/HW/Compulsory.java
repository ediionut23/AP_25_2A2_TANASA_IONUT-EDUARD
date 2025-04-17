import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;





public class Compulsory {
    public static void main(String[] args) {
        System.out.println("Current working directory: " + System.getProperty("user.dir"));

        TileBag bag = new TileBag();
        Board board = new Board();
        Dictionary dict = new Dictionary("words.txt");
        TurnManager turnManager = new TurnManager(2);

        List<Player> players = List.of(
                new Player("cartof1", 0, bag, board, dict, turnManager),
                new Player("cartof2", 1, bag, board, dict, turnManager)
        );

        TimeKeeper timeKeeper = new TimeKeeper(10, new ArrayList<>(players));
        timeKeeper.start();

        for (Player p : players) p.start();
        for (Player p : players) {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        Player winner = players.get(0);
        for (Player p : players) {
            if (p.getScore() > winner.getScore()) {
                winner = p;
            }
        }

        System.out.println("Winner: " + winner.getPlayerName() + " with " + winner.getScore() + " points!");
        System.out.println("Game over!");
    }
}
