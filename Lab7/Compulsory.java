import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

class Tile {
    char letter;
    int points;

    public Tile(char letter, int points) {
        this.letter = letter;
        this.points = points;
    }

    @Override
    public String toString() {
        return letter + "(" + points + ")";
    }
}

class TileBag {
    private final List<Tile> tiles = new ArrayList<>();
    private final Random rand = new Random();

    public TileBag() {
        for (char c = 'A'; c <= 'Z'; c++) {
            for (int i = 0; i < 10; i++) {
                int points = rand.nextInt(10) + 1;
                tiles.add(new Tile(c, points));
            }
        }
        Collections.shuffle(tiles);
    }

    public synchronized List<Tile> extractTiles(int count) {
        List<Tile> extracted = new ArrayList<>();
        for (int i = 0; i < count && !tiles.isEmpty(); i++) {
            extracted.add(tiles.remove(0));
        }
        return extracted;
    }

    public synchronized boolean isEmpty() {
        return tiles.isEmpty();
    }
}

class Dictionary {
    private final Set<String> words = new HashSet<>(Arrays.asList(
            "CARTOF", "WORLDOFCARTOFI", "HELLO", "WORLD", "JAVA", "PROGRAMMING", "MIAU", "CAT", "DOG", "BIRD", "FISH", "COMPUTER", "GAME", "SCRABBLE", "TILE", "POINTS"));

    public boolean isValid(String word) {
        return words.contains(word.toUpperCase());
    }
}

class Board {
    public synchronized void submitWord(String playerName, String word, int score) {
        System.out.println(playerName + " submitted word '" + word + "' for " + score + " points.");
    }
}

class Player extends Thread {
    private final String name;
    private final TileBag tileBag;
    private final Board board;
    private final Dictionary dictionary;
    private final Random rand = new Random();
    private int score = 0;

    public Player(String name, TileBag tileBag, Board board, Dictionary dictionary) {
        this.name = name;
        this.tileBag = tileBag;
        this.board = board;
        this.dictionary = dictionary;
    }

    @Override
    public void run() {
        List<Tile> hand = tileBag.extractTiles(7);
        while (!hand.isEmpty()) {
            String word = generateWord(hand);
            if (word != null) {
                int points = calculateScore(word, hand);
                score += points;
                board.submitWord(name, word, points);
                hand.addAll(tileBag.extractTiles(word.length()));
            } else {
                // Discard and extract new tiles
                hand = tileBag.extractTiles(7);
                System.out.println(name + " passed the turn.");
            }

            if (tileBag.isEmpty()) break;

            try {
                Thread.sleep(200); // simulate thinking
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(name + " finished with score: " + score);
    }

    private String generateWord(List<Tile> hand) {
        StringBuilder sb = new StringBuilder();
        for (Tile t : hand) sb.append(t.letter);
        char[] chars = sb.toString().toCharArray();
        Arrays.sort(chars);

        for (int len = 7; len >= 2; len--) {
            List<String> permutations = generatePermutations(chars, len);
            for (String perm : permutations) {
                if (dictionary.isValid(perm)) return perm;
            }
        }

        return null;
    }

    private int calculateScore(String word, List<Tile> hand) {
        int total = 0;
        List<Tile> used = new ArrayList<>();

        for (char c : word.toCharArray()) {
            for (Tile t : hand) {
                if (!used.contains(t) && t.letter == c) {
                    total += t.points;
                    used.add(t);
                    break;
                }
            }
        }

        hand.removeAll(used);
        return total;
    }

    private List<String> generatePermutations(char[] chars, int len) {
        Set<String> result = new HashSet<>();
        permute(chars, 0, len, result);
        return new ArrayList<>(result);
    }

    private void permute(char[] arr, int l, int len, Set<String> result) {
        if (l == len) {
            result.add(new String(arr, 0, len));
        } else {
            for (int i = l; i < arr.length; i++) {
                swap(arr, l, i);
                permute(arr, l + 1, len, result);
                swap(arr, l, i);
            }
        }
    }

    private void swap(char[] arr, int i, int j) {
        char tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
}

public class Compulsory {
    public static void main(String[] args) {
        TileBag bag = new TileBag();
        Board board = new Board();
        Dictionary dict = new Dictionary();

        List<Player> players = List.of(
                new Player("cartof1", bag, board, dict),
                new Player("cartof2", bag, board, dict));

        for (Player p : players) {
            p.start();
        }

        for (Player p : players) {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Game over!");
    }
}
