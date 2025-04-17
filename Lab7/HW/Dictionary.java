import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

class Dictionary {
    private final Set<String> words = ConcurrentHashMap.newKeySet();

    public Dictionary(String filename) {
        try (Scanner scanner = new Scanner(new File(filename))) {
            System.out.println("Reading dictionary from: " + filename);
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine().trim().toUpperCase());
            }
        } catch (FileNotFoundException e) {
            System.err.println("Dictionary file not found at: " + filename);
            e.printStackTrace();
        }
    }

    public boolean isValid(String word) {
        return words.contains(word.toUpperCase());
    }
}