import java.util.*;

class Player extends Thread {
    private final String name;
    private final int playerIndex;
    private final TileBag tileBag;
    private final Board board;
    private final Dictionary dictionary;
    private final TurnManager turnManager;
    private final Random rand = new Random();
    private int score = 0;

    public Player(String name, int index, TileBag tileBag, Board board, Dictionary dictionary, TurnManager turnManager) {
        this.name = name;
        this.playerIndex = index;
        this.tileBag = tileBag;
        this.board = board;
        this.dictionary = dictionary;
        this.turnManager = turnManager;
    }

    @Override
    public void run() {
        List<Tile> hand = tileBag.extractTiles(7);
        while (!hand.isEmpty() && !tileBag.isEmpty()) {
            try {
                // Așteaptă până când este rândul său
                turnManager.waitForTurn(playerIndex);
            } catch (InterruptedException e) {
                break;
            }

            String word = generateWord(hand);
            if (word != null) {
                int points = calculateScore(word, hand);
                score += points;
                board.submitWord(name, word, points);
                hand.addAll(tileBag.extractTiles(word.length()));
            } else {
                hand = tileBag.extractTiles(7);
                System.out.println(name + " passed the turn.");
            }

            turnManager.endTurn();

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                break;
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

    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return name;
    }
}