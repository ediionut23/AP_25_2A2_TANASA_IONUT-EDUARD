import java.awt.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.template.*;

interface StrategiePersistenta {
    void salveaza(List<Imagine> imagini, String fisier) throws IOException;
    List<Imagine> incarca(String fisier) throws IOException;
}

class StrategieJson implements StrategiePersistenta {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void salveaza(List<Imagine> imagini, String fisier) throws IOException {
        mapper.writeValue(new File(fisier), imagini);
    }

    @Override
    public List<Imagine> incarca(String fisier) throws IOException {
        File file = new File(fisier);
        if (!file.exists()) return new ArrayList<>();
        return mapper.readValue(file, new TypeReference<List<Imagine>>() {});
    }
}

class StrategieBinara implements StrategiePersistenta {
    @Override
    public void salveaza(List<Imagine> imagini, String fisier) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fisier))) {
            out.writeObject(imagini);
        }
    }

    @Override
    public List<Imagine> incarca(String fisier) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fisier))) {
            return (List<Imagine>) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Eroare la citirea fisierului binar", e);
        }
    }
}

class StrategieText implements StrategiePersistenta {
    @Override
    public void salveaza(List<Imagine> imagini, String fisier) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(fisier))) {
            for (Imagine img : imagini) {
                writer.println(img.nume() + "," + String.join(";", img.taguri()) + "," + img.locatie());
            }
        }
    }

    @Override
    public List<Imagine> incarca(String fisier) throws IOException {
        List<Imagine> rezultat = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fisier))) {
            String linie;
            while ((linie = reader.readLine()) != null) {
                String[] parti = linie.split(",");
                String nume = parti[0];
                List<String> taguri = Arrays.asList(parti[1].split(";"));
                String locatie = parti[2];
                rezultat.add(new Imagine(nume, new Date(), taguri, locatie));
            }
        }
        return rezultat;
    }
}

class ExceptieImagineNegasita extends Exception {
    public ExceptieImagineNegasita(String mesaj) {
        super(mesaj);
    }
}

class ExceptieComandaInvalida extends Exception {
    public ExceptieComandaInvalida(String mesaj) {
        super(mesaj);
    }
}

record Imagine(String nume, Date data, List<String> taguri, String locatie) implements Serializable {}

class RepozitoriuImagini {
    private List<Imagine> imagini = new ArrayList<>();
    private StrategiePersistenta strategie = new StrategieJson();

    public void seteazaStrategie(StrategiePersistenta strategie) {
        this.strategie = strategie;
    }

    public void adaugaImagine(Imagine imagine) {
        imagini.add(imagine);
    }

    public void stergeImagine(String nume) throws ExceptieImagineNegasita {
        boolean eliminata = imagini.removeIf(img -> img.nume().equals(nume));
        if (!eliminata) throw new ExceptieImagineNegasita("Imaginea nu a fost gasita: " + nume);
    }

    public void actualizeazaImagine(String nume, Imagine nouaImagine) throws ExceptieImagineNegasita {
        boolean actualizata = false;
        for (int i = 0; i < imagini.size(); i++) {
            if (imagini.get(i).nume().equals(nume)) {
                imagini.set(i, nouaImagine);
                actualizata = true;
                break;
            }
        }
        if (!actualizata) throw new ExceptieImagineNegasita("Imaginea nu a fost gasita: " + nume);
    }

    public void salveaza(String fisier) throws IOException {
        strategie.salveaza(imagini, fisier);
    }

    public void incarca(String fisier) throws IOException {
        imagini = strategie.incarca(fisier);
    }

    public void afiseazaImagine(String nume) throws ExceptieImagineNegasita, IOException {
        for (Imagine img : imagini) {
            if (img.nume().equals(nume)) {
                Desktop.getDesktop().open(new File(img.locatie()));
                return;
            }
        }
        throw new ExceptieImagineNegasita("Imaginea nu a fost gasita: " + nume);
    }

    public List<Imagine> getImagini() {
        return imagini;
    }

    public void genereazaRaport() throws IOException, TemplateException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setDirectoryForTemplateLoading(new File("/Users/ediionut23/Desktop/templates"));
        cfg.setDefaultEncoding("UTF-8");
        Template template = cfg.getTemplate("report.ftl");

        Map<String, Object> data = new HashMap<>();
        data.put("images", imagini);

        FileWriter writer = new FileWriter("raport.html");
        template.process(data, writer);
        writer.close();

        Desktop.getDesktop().browse(new File("raport.html").toURI());
    }
}

interface Comanda {
    void executa() throws Exception;
}

class ComandaAdaugaImagine implements Comanda {
    private final RepozitoriuImagini repo;
    private final Imagine imagine;

    public ComandaAdaugaImagine(RepozitoriuImagini repo, Imagine imagine) {
        this.repo = repo;
        this.imagine = imagine;
    }

    @Override
    public void executa() {
        repo.adaugaImagine(imagine);
    }
}

class ComandaStergeImagine implements Comanda {
    private final RepozitoriuImagini repo;
    private final String numeImagine;

    public ComandaStergeImagine(RepozitoriuImagini repo, String numeImagine) {
        this.repo = repo;
        this.numeImagine = numeImagine;
    }

    @Override
    public void executa() throws ExceptieImagineNegasita {
        repo.stergeImagine(numeImagine);
    }
}

class ComandaAfiseazaImagine implements Comanda {
    private final RepozitoriuImagini repo;
    private final String numeImagine;

    public ComandaAfiseazaImagine(RepozitoriuImagini repo, String numeImagine) {
        this.repo = repo;
        this.numeImagine = numeImagine;
    }

    @Override
    public void executa() throws Exception {
        repo.afiseazaImagine(numeImagine);
    }
}

class ComandaRaport implements Comanda {
    private final RepozitoriuImagini repo;

    public ComandaRaport(RepozitoriuImagini repo) {
        this.repo = repo;
    }

    @Override
    public void executa() throws IOException, TemplateException {
        repo.genereazaRaport();
    }
}

class ComandaAdaugaTot implements Comanda {
    private final RepozitoriuImagini repo;
    private final Path director;
    private final List<String> taguriPosibile = List.of("cartof1", "cartof2", "cartof3", "cartof4", "cartof5");

    public ComandaAdaugaTot(RepozitoriuImagini repo, Path director) {
        this.repo = repo;
        this.director = director;
    }

    @Override
    public void executa() throws IOException {
        Files.walk(director)
                .filter(Files::isRegularFile)
                .filter(p -> p.toString().endsWith(".jpg") || p.toString().endsWith(".png"))
                .forEach(p -> {
                    String nume = p.getFileName().toString();
                    List<String> taguri = taguriRandom();
                    Imagine imagine = new Imagine(nume, new Date(), taguri, p.toAbsolutePath().toString());
                    repo.adaugaImagine(imagine);
                });
    }

    private List<String> taguriRandom() {
        Collections.shuffle(taguriPosibile);
        return taguriPosibile.subList(0, 2 + new Random().nextInt(2));
    }
}

class GrupatorTaguri {
    public static List<Set<Imagine>> gasesteGrupe(List<Imagine> imagini) {
        Map<Imagine, Set<Imagine>> graf = new HashMap<>();
        for (Imagine img : imagini) graf.put(img, new HashSet<>());

        for (int i = 0; i < imagini.size(); i++) {
            for (int j = i + 1; j < imagini.size(); j++) {
                if (!Collections.disjoint(imagini.get(i).taguri(), imagini.get(j).taguri())) {
                    graf.get(imagini.get(i)).add(imagini.get(j));
                    graf.get(imagini.get(j)).add(imagini.get(i));
                }
            }
        }

        Set<Imagine> vizitate = new HashSet<>();
        List<Set<Imagine>> grupe = new ArrayList<>();

        for (Imagine img : imagini) {
            if (!vizitate.contains(img)) {
                Set<Imagine> grup = new HashSet<>();
                dfs(img, graf, vizitate, grup);
                grupe.add(grup);
            }
        }

        return grupe;
    }

    private static void dfs(Imagine curent, Map<Imagine, Set<Imagine>> graf, Set<Imagine> vizitate, Set<Imagine> grup) {
        vizitate.add(curent);
        grup.add(curent);
        for (Imagine vecin : graf.get(curent)) {
            if (!vizitate.contains(vecin)) {
                dfs(vecin, graf, vizitate, grup);
            }
        }
    }
}

class TerminalImagini {
    private final RepozitoriuImagini repo;

    public TerminalImagini(RepozitoriuImagini repo) {
        this.repo = repo;
    }

    public void porneste() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Terminal Manager Imagini. Scrie 'exit' pentru a iesi.");
        try {
            while (true) {
                System.out.print("Comanda: ");
                String input = reader.readLine();
                if (input == null || "exit".equalsIgnoreCase(input.trim())) break;
                try {
                    proceseazaComanda(input);
                } catch (Exception e) {
                    System.out.println("Eroare: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Eroare la citirea comenzii: " + e.getMessage());
        }
    }

    private void proceseazaComanda(String input) throws Exception {
        String[] parti = input.split(" ", 2);
        String comanda = parti[0];
        String argumente = parti.length > 1 ? parti[1] : "";

        switch (comanda.toLowerCase()) {
            case "adauga" -> {
                String[] detalii = argumente.split(",");
                new ComandaAdaugaImagine(repo, new Imagine(detalii[0], new Date(), Arrays.asList(detalii[1].split(";")), detalii[2])).executa();
            }
            case "sterge" -> new ComandaStergeImagine(repo, argumente).executa();
            case "afiseaza" -> new ComandaAfiseazaImagine(repo, argumente).executa();
            case "raport" -> new ComandaRaport(repo).executa();
            case "adaugaTot" -> new ComandaAdaugaTot(repo, Paths.get(argumente)).executa();
            case "salveaza" -> repo.salveaza(argumente);
            case "incarca" -> repo.incarca(argumente);
            case "grupeaza" -> {
                List<Set<Imagine>> grupe = GrupatorTaguri.gasesteGrupe(repo.getImagini());
                int i = 1;
                for (Set<Imagine> grup : grupe) {
                    System.out.println("Grup " + i++ + ":");
                    grup.forEach(img -> System.out.println(" - " + img.nume() + " [" + String.join(", ", img.taguri()) + "]"));
                }
            }
            default -> throw new ExceptieComandaInvalida("Comanda necunoscuta: " + comanda);
        }
    }
}

public class Bonus {
    public static void main(String[] args) {
        RepozitoriuImagini repo = new RepozitoriuImagini();
        repo.seteazaStrategie(new StrategieJson()); // strategie implicita
        TerminalImagini terminal = new TerminalImagini(repo);
        terminal.porneste();
    }
}
