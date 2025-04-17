import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

class TileBag {
    private final List<Tile> tiles = new ArrayList<>();
    private final Random rand = new Random();

    public TileBag() {
        for (char c = 'A'; c <= 'Z'; c++) {
            for (int i = 0; i < 50; i++) {
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