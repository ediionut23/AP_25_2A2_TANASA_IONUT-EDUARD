import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

record ImageItem(String name, LocalDate date, List<String> tags, Path location) {
    public ImageItem {
        if (name == null || name.isBlank() || location == null) {
            throw new IllegalArgumentException("Numele și locația imaginii nu pot fi goale.");
        }
    }
}
class ImageException extends Exception {
    public ImageException(String message) {
        super(message);
    }
}

class ImageRepository {
    private final List<ImageItem> images = new ArrayList<>();

    public void addImage(ImageItem image) throws ImageException {
        if (image == null) {
            throw new ImageException("Nu se poate adăuga o imagine null.");
        }
        if (!Files.exists(image.location())) {
            throw new ImageException("Fișierul nu există: " + image.location());
        }
        images.add(image);
    }

    public void displayImage(String name) throws ImageException {
        for (ImageItem img : images) {
            if (img.name().equalsIgnoreCase(name)) {
                File file = img.location().toFile();
                if (!file.exists()) {
                    throw new ImageException("Fișierul nu există: " + file.getAbsolutePath());
                }
                try {
                    Desktop.getDesktop().open(file);
                } catch (Exception e) {
                    throw new ImageException("Eroare la deschiderea imaginii: " + e.getMessage());
                }
                return;
            }
        }
        throw new ImageException("Imaginea nu a fost găsită în colecție: " + name);
    }

    public List<ImageItem> getAllImages() {
        return new ArrayList<>(images);
    }
}
public class Compulsory {
    public static void main(String[] args) {
        ImageRepository repo = new ImageRepository();

        try {
            Path imagePath = Path.of(System.getProperty("user.home"), "Desktop", "img.png");

            ImageItem img1 = new ImageItem("cartof", LocalDate.now(), List.of("cartof", "miau"),
                    imagePath);
            repo.addImage(img1);

            System.out.println("Imagine adăugată: " + img1.name());

            repo.displayImage("CArtof");

        } catch (ImageException e) {
            System.err.println("Eroare: " + e.getMessage());
        }
    }
}
