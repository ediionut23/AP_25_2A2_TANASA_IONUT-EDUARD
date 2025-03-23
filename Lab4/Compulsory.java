import java.util.*;
import java.util.stream.Collectors;

enum LocationType {
    FRIENDLY, NEUTRAL, ENEMY;
}

class Location implements Comparable<Location> {
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

    @Override
    public int compareTo(Location other) {
        return this.name.compareTo(other.name);
    }
}

class Map {
    private List<Location> locations;

    public Map(List<Location> locations) {
        this.locations = locations;
    }

    public void printFriendlyLocations() {
        System.out.println("Friendly Locations:");
        locations.stream()
                .filter(location -> location.getType() == LocationType.FRIENDLY)
                .collect(Collectors.toCollection(TreeSet::new))
                .forEach(System.out::println);
    }

    public void printEnemyLocations() {
        System.out.println("Enemy Locations:");
        locations.stream()
                .filter(location -> location.getType() == LocationType.ENEMY)
                .sorted(Comparator.comparing(Location::getType).thenComparing(Location::getName))
                .collect(Collectors.toCollection(LinkedList::new))
                .forEach(System.out::println);
    }
}

public class Compulsory {
    public static void main(String[] args) {
        Location location1 = new Location("L2", LocationType.FRIENDLY);
        Location location2 = new Location("L1", LocationType.ENEMY);
        Location location3 = new Location("L3", LocationType.FRIENDLY);
        Location location4 = new Location("L4", LocationType.ENEMY);
        Location location5 = new Location("L5", LocationType.NEUTRAL);

        List<Location> locations = Arrays.asList(location1, location2, location3, location4, location5);

        Map map = new Map(locations);
        map.printFriendlyLocations();
        map.printEnemyLocations();
    }
}
