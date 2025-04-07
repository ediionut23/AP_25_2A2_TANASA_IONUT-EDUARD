import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import freemarker.template.*;

record ImageItem(String name, Date date, List<String> tags, String location) implements Serializable {}

class ImageNotFoundException extends Exception {
    public ImageNotFoundException(String message) {
        super(message);
    }
}

class InvalidCommandException extends Exception {
    public InvalidCommandException(String message) {
        super(message);
    }
}

class ImageRepository {
    private List<ImageItem> images = new ArrayList<>();
    private final String storageFile = "images.json";

    public void addImage(ImageItem image) {
        images.add(image);
    }

    public void removeImage(String name) throws ImageNotFoundException {
        boolean removed = images.removeIf(img -> img.name().equals(name));
        if (!removed) {
            throw new ImageNotFoundException("Image not found: " + name);
        }
    }

    public void updateImage(String name, ImageItem newImage) throws ImageNotFoundException {
        boolean updated = false;
        for (int i = 0; i < images.size(); i++) {
            if (images.get(i).name().equals(name)) {
                images.set(i, newImage);
                updated = true;
                break;
            }
        }
        if (!updated) {
            throw new ImageNotFoundException("Image not found: " + name);
        }
    }

    public void saveToFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File(storageFile), images);
    }

    public void loadFromFile() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File(storageFile);
        if (file.exists()) {
            images = mapper.readValue(file, new TypeReference<List<ImageItem>>() {});
        }
    }

    public void displayImage(String name) throws ImageNotFoundException, IOException {
        for (ImageItem img : images) {
            if (img.name().equals(name)) {
                Desktop.getDesktop().open(new File(img.location()));
                return;
            }
        }
        throw new ImageNotFoundException("Image not found: " + name);
    }

    public List<ImageItem> getImages() {
        return images;
    }

    public void generateReport() throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("templates")); // <-- schimbă aici dacă vrei altă locație
        cfg.setDefaultEncoding("UTF-8");
        Template template = cfg.getTemplate("report.ftl");

        Map<String, Object> data = new HashMap<>();
        data.put("images", images);

        FileWriter writer = new FileWriter("report.html");
        template.process(data, writer);
        writer.close();

        Desktop.getDesktop().browse(new File("report.html").toURI());
    }
}

interface Command {
    void execute() throws Exception;
}

class AddImageCommand implements Command {
    private final ImageRepository repository;
    private final ImageItem image;

    public AddImageCommand(ImageRepository repository, ImageItem image) {
        this.repository = repository;
        this.image = image;
    }

    @Override
    public void execute() {
        repository.addImage(image);
    }
}

class RemoveImageCommand implements Command {
    private final ImageRepository repository;
    private final String imageName;

    public RemoveImageCommand(ImageRepository repository, String imageName) {
        this.repository = repository;
        this.imageName = imageName;
    }

    @Override
    public void execute() throws ImageNotFoundException {
        repository.removeImage(imageName);
    }
}

class UpdateImageCommand implements Command {
    private final ImageRepository repository;
    private final String imageName;
    private final ImageItem newImage;

    public UpdateImageCommand(ImageRepository repository, String imageName, ImageItem newImage) {
        this.repository = repository;
        this.imageName = imageName;
        this.newImage = newImage;
    }

    @Override
    public void execute() throws ImageNotFoundException {
        repository.updateImage(imageName, newImage);
    }
}

class DisplayImageCommand implements Command {
    private final ImageRepository repository;
    private final String imageName;

    public DisplayImageCommand(ImageRepository repository, String imageName) {
        this.repository = repository;
        this.imageName = imageName;
    }

    @Override
    public void execute() throws ImageNotFoundException, IOException {
        repository.displayImage(imageName);
    }
}

class ReportCommand implements Command {
    private final ImageRepository repository;

    public ReportCommand(ImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() throws IOException, TemplateException {
        repository.generateReport();
    }
}

class SaveCommand implements Command {
    private final ImageRepository repository;

    public SaveCommand(ImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() throws IOException {
        repository.saveToFile();
    }
}

class LoadCommand implements Command {
    private final ImageRepository repository;

    public LoadCommand(ImageRepository repository) {
        this.repository = repository;
    }

    @Override
    public void execute() throws IOException {
        repository.loadFromFile();
    }
}

class ImageShell {
    private final ImageRepository repository;

    public ImageShell(ImageRepository repository) {
        this.repository = repository;
    }

    public void run() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Image Manager Shell. Type 'exit' to quit.");

        try {
            while (true) {
                System.out.print("Command: ");
                String input = reader.readLine();
                if (input == null || "exit".equalsIgnoreCase(input.trim())) {
                    break;
                }
                try {
                    processCommand(input);
                } catch (InvalidCommandException | ImageNotFoundException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (TemplateException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading input: " + e.getMessage());
        }
    }

    private void processCommand(String input) throws InvalidCommandException, ImageNotFoundException, TemplateException, IOException {
        String[] parts = input.split(" ", 2);
        String command = parts[0];
        String args = parts.length > 1 ? parts[1] : "";

        switch (command.toLowerCase()) {
            case "add":
                String[] addDetails = args.split(",");
                if (addDetails.length < 3) throw new InvalidCommandException("Invalid add format.");
                new AddImageCommand(repository, new ImageItem(addDetails[0], new Date(), Arrays.asList(addDetails[1].split(";")), addDetails[2])).execute();
                break;
            case "remove":
                new RemoveImageCommand(repository, args).execute();
                break;
            case "update":
                String[] updateParts = args.split(",");
                if (updateParts.length < 3) throw new InvalidCommandException("Invalid update format.");
                String name = updateParts[0];
                List<String> tags = Arrays.asList(updateParts[1].split(";"));
                String location = updateParts[2];
                ImageItem updated = new ImageItem(name, new Date(), tags, location);
                new UpdateImageCommand(repository, name, updated).execute();
                break;
            case "display":
                new DisplayImageCommand(repository, args).execute();
                break;
            case "report":
                new ReportCommand(repository).execute();
                break;
            case "save":
                new SaveCommand(repository).execute();
                break;
            case "load":
                new LoadCommand(repository).execute();
                break;
            default:
                throw new InvalidCommandException("Unknown command: " + command);
        }
    }
}

public class HW {
    public static void main(String[] args) {
        ImageRepository repository = new ImageRepository();
        ImageShell shell = new ImageShell(repository);
        shell.run();
    }
}
