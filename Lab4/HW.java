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

class Location{
    private String name;
    private LocationType type;

    public Location(String name, LocationType type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public LocationType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Location{name='" + name + "', type=" + type + '}';
    }
}

class LocationFactory{
    private static final Faker faker = new Faker();
    public static Location createLocation(LocationType type){
        String name = faker.address().cityName();
        return new Location(name, type);
    }
}

class Graf{
    private Graph<Location, DefaultWeightedEdge> graph;
    private List<Location> locations;
    private Map<LocationType, List<Location>> locationsByType;

    public Graf(){
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
        this.locations = new ArrayList<>();
        generateLocations();
        buildGraph();
        groupLocationsByType();
    }

    private void generateLocations(){
        locations.add(LocationFactory.createLocation(LocationType.FRIENDLY));
        locations.add(LocationFactory.createLocation(LocationType.FRIENDLY));
        locations.add(LocationFactory.createLocation(LocationType.NEUTRAL));
        locations.add(LocationFactory.createLocation(LocationType.NEUTRAL));
        locations.add(LocationFactory.createLocation(LocationType.ENEMY));
        locations.add(LocationFactory.createLocation(LocationType.ENEMY));

    }
    private void buildGraph(){
        locations.forEach(graph::addVertex);
        Random random = new Random();
        for (int i = 0; i < locations.size() - 1; i++) {
            for (int j = i + 1; j < locations.size(); j++) {
                if (random.nextBoolean()) {
                    DefaultWeightedEdge edge = graph.addEdge(locations.get(i), locations.get(j));
                    if (edge != null) {
                        int time = random.nextInt(10) + 1;
                        graph.setEdgeWeight(edge, time);
                    }
                }
            }
        }
    }
    private void groupLocationsByType(){
        locationsByType = locations.stream().collect(Collectors.groupingBy(Location::getType));
    }
    public Graph<Location, DefaultWeightedEdge> getGraph(){
        return graph;
    }
    public List<Location> getLocations(){
        return locations;
    }
    public Map<LocationType, List<Location>> getLocationsByType(){
        return locationsByType;
    }
}

class Drone{
    private Location currentLocation;
    private Graf mapGraph;

    public Drone(Graf mapGraph) {
        this.mapGraph = mapGraph;
        this.currentLocation = mapGraph.getLocations().get(0);
    }

    public void findFastestPaths() {
        System.out.println("\nFastest paths from: " + currentLocation.getName());
        DijkstraShortestPath<Location, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(mapGraph.getGraph());
        Map<LocationType, List<String>> fastestPathsByType = new LinkedHashMap<>();

        for (LocationType type : LocationType.values()) {
            fastestPathsByType.put(type, new ArrayList<>());
        }

        for (Location loc : mapGraph.getLocations()) {
            if (!loc.equals(currentLocation)) {
                double time = dijkstra.getPathWeight(currentLocation, loc);
                fastestPathsByType.get(loc.getType()).add(currentLocation.getName() + " -> " + loc.getName() + " : " + time);
            }
        }

        System.out.println("\nFastest paths sorted by location type:");
        for (LocationType type : LocationType.values()) {
            System.out.println(type + " locations:");
            fastestPathsByType.get(type).forEach(System.out::println);
        }
    }
}
public class HW {
    public static void main(String[] args) {
        Graf mapGraph = new Graf();
        Drone robot = new Drone(mapGraph);
        robot.findFastestPaths();

        System.out.println("\nLocations grouped by type:");
        mapGraph.getLocationsByType().forEach((type, locs) -> {
            System.out.println(type + ": " + locs);
        });
    }
}
