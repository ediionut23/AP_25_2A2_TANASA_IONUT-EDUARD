import com.github.javafaker.Faker;
import org.jgrapht.Graph;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import java.util.*;
import java.util.stream.Collectors;

enum LocationType {
    FRIENDLY, NEUTRAL, ENEMY;
}

class Location {
    private String name;
    private LocationType type;

    public Location(String name, LocationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() { return name; }
    public LocationType getType() { return type; }

    @Override
    public String toString() {
        return name + " (" + type + ")";
    }
}

class LocationFactory {
    private static final Faker faker = new Faker();

    public static Location createLocation(LocationType type) {
        return new Location(faker.address().cityName(), type);
    }
}

class LargeGraphGenerator {
    public static Graf generateLargeGraph(int numLocations) {
        Graf graph = new Graf();
        Random random = new Random();

        for (int i = 0; i < numLocations; i++) {
            LocationType type = LocationType.values()[random.nextInt(3)];
            graph.addLocation(LocationFactory.createLocation(type));
        }

        graph.buildGraph();
        return graph;
    }
}

class Graf {
    private Graph<Location, DefaultWeightedEdge> graph;
    private List<Location> locations;
    private Map<Location, Map<Location, Double>> safetyMap;

    public Graf() {
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.locations = new ArrayList<>();
        this.safetyMap = new HashMap<>();
    }

    public void addLocation(Location location) {
        locations.add(location);
        graph.addVertex(location);
    }

    public void buildGraph() {
        Random random = new Random();

        for (int i = 0; i < locations.size(); i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                if (random.nextBoolean()) {
                    DefaultWeightedEdge edge = graph.addEdge(locations.get(i), locations.get(j));
                    if (edge != null) {
                        double safety = random.nextDouble();
                        graph.setEdgeWeight(edge, -Math.log(safety));

                        safetyMap.computeIfAbsent(locations.get(i), k -> new HashMap<>()).put(locations.get(j), safety);
                        safetyMap.computeIfAbsent(locations.get(j), k -> new HashMap<>()).put(locations.get(i), safety);
                    }
                }
            }
        }
    }

    public Graph<Location, DefaultWeightedEdge> getGraph() { return graph; }
    public List<Location> getLocations() { return locations; }
    public Map<Location, Map<Location, Double>> getSafetyMap() { return safetyMap; }
}

class SafestPathFinder {
    private Graph<Location, DefaultWeightedEdge> graph;
    private Map<Location, Map<Location, Double>> safetyMap;

    public SafestPathFinder(Graph<Location, DefaultWeightedEdge> graph, Map<Location, Map<Location, Double>> safetyMap) {
        this.graph = graph;
        this.safetyMap = safetyMap;
    }

    public List<Location> findSafestPath(Location start, Location end) {
        DijkstraShortestPath<Location, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        return dijkstra.getPath(start, end).getVertexList();
    }

    public Map<LocationType, Long> countLocationTypes(List<Location> path) {
        return path.stream().collect(Collectors.groupingBy(Location::getType, Collectors.counting()));
    }
}

class TestResult {
    private int numLocations;
    private int pathLength;
    private long executionTime;
    private Map<LocationType, Long> typeCounts;

    public TestResult(int numLocations, int pathLength, long executionTime, Map<LocationType, Long> typeCounts) {
        this.numLocations = numLocations;
        this.pathLength = pathLength;
        this.executionTime = executionTime;
        this.typeCounts = typeCounts;
    }

    public int getNumLocations() { return numLocations; }
    public int getPathLength() { return pathLength; }
    public long getExecutionTime() { return executionTime; }
    public Map<LocationType, Long> getTypeCounts() { return typeCounts; }

    @Override
    public String toString() {
        return "TestResult{" +
                "numLocations=" + numLocations +
                ", pathLength=" + pathLength +
                ", executionTime=" + executionTime +
                ", typeCounts=" + typeCounts +
                '}';
    }
}

public class Bonus {
    private static List<TestResult> testResults = new ArrayList<>();

    public static void testLargeInstances() {
        int[] sizes = {500,250, 2000};

        for (int size : sizes) {
            System.out.println("\n==== Testam " + size + " locatii ====");
            long startTime = System.currentTimeMillis();

            Graf largeGraph = LargeGraphGenerator.generateLargeGraph(size);
            SafestPathFinder safestPathFinder = new SafestPathFinder(largeGraph.getGraph(), largeGraph.getSafetyMap());

            Location start = largeGraph.getLocations().get(0);
            Location end = largeGraph.getLocations().get(largeGraph.getLocations().size() - 1);

            List<Location> safestPath = safestPathFinder.findSafestPath(start, end);
            Map<LocationType, Long> typeCounts = safestPathFinder.countLocationTypes(safestPath);

            long executionTime = System.currentTimeMillis() - startTime;

            TestResult result = new TestResult(size, safestPath.size(), executionTime, typeCounts);
            testResults.add(result);

            System.out.println("Safest path from " + start.getName() + " to " + end.getName() + ": " + safestPath);
            System.out.println("Number by type: " + typeCounts);
            System.out.println("Exec time: " + executionTime);
        }

        computeStatistics();
    }

    private static void computeStatistics() {
        System.out.println("\n===== Test Statistics =====");

        double avgExecutionTime = testResults.stream()
                .mapToLong(TestResult::getExecutionTime)
                .average()
                .orElse(0.0);
        System.out.println("Average exec time: " + avgExecutionTime );

        double avgPathLength = testResults.stream()
                .mapToInt(TestResult::getPathLength)
                .average()
                .orElse(0.0);
        System.out.println("Average path length: " + avgPathLength);

        Map<LocationType, Double> avgTypeDistribution = Arrays.stream(LocationType.values())
                .collect(Collectors.toMap(
                        type -> type,
                        type -> testResults.stream()
                                .mapToLong(result -> result.getTypeCounts().getOrDefault(type, 0L))
                                .average()
                                .orElse(0.0)
                ));
        System.out.println("Average locations per type: " + avgTypeDistribution);
    }

    public static void main(String[] args) {
        testLargeInstances();
    }
}
