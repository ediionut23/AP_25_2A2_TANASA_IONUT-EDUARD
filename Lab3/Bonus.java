import java.time.LocalTime;
import java.util.*;

interface PassengerCapable {
    int getPassengerCapacity();
}

interface CargoCapable {
    double getMaxPayload();
}

abstract class Aircraft {
    protected String name;
    protected String tailNumber;

    public Aircraft(String name, String tailNumber) {
        this.name = name;
        this.tailNumber = tailNumber;
    }

    public String getName() { return name; }
}

class Airliner extends Aircraft implements PassengerCapable {
    private int passengerCapacity;

    public Airliner(String name, String tailNumber, int passengerCapacity) {
        super(name, tailNumber);
        this.passengerCapacity = passengerCapacity;
    }

    public int getPassengerCapacity() { return passengerCapacity; }
}

class Freighter extends Aircraft implements CargoCapable {
    private double maxPayload;

    public Freighter(String name, String tailNumber, double maxPayload) {
        super(name, tailNumber);
        this.maxPayload = maxPayload;
    }

    public double getMaxPayload() { return maxPayload; }
}

class Drone extends Aircraft {
    public Drone(String name, String tailNumber) {
        super(name, tailNumber);
    }
}

class Flight {
    private String id;
    private Aircraft aircraft;
    private LocalTime startTime;
    private LocalTime endTime;

    public Flight(String id, Aircraft aircraft, LocalTime startTime, LocalTime endTime) {
        this.id = id;
        this.aircraft = aircraft;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public boolean conflictsWith(Flight other) {
        return !(this.endTime.isBefore(other.startTime) || this.startTime.isAfter(other.endTime));
    }

    public void adjustLandingTime(LocalTime newStart) {
        this.startTime = newStart;
        this.endTime = newStart.plusMinutes(30);
    }

    public String getId() { return id; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }
}

class Airport {
    private int runways;

    public Airport(int runways) {
        this.runways = runways;
    }

    public int getRunways() { return runways; }
}

class FlightScheduler {
    private Airport airport;
    private List<Flight> flights;
    private Map<Flight, Integer> schedule;

    public FlightScheduler(Airport airport, List<Flight> flights) {
        this.airport = airport;
        this.flights = flights;
        this.schedule = new HashMap<>();
    }

    public void assignRunways() {
        flights.sort(Comparator.comparing(Flight::getStartTime));
        Map<Flight, Integer> flightColors = new HashMap<>();

        for (Flight flight : flights) {
            Set<Integer> usedColors = new HashSet<>();
            for (Flight assignedFlight : flightColors.keySet()) {
                if (flight.conflictsWith(assignedFlight)) {
                    usedColors.add(flightColors.get(assignedFlight));
                }
            }

            int color = 0;
            while (usedColors.contains(color)) {
                color++;
            }
            flightColors.put(flight, color);
        }

        int maxRunwaysNeeded = flightColors.values().stream().max(Integer::compare).orElse(0) + 1;
        if (maxRunwaysNeeded > airport.getRunways()) {
            System.out.println("Adjusting landing times due to insufficient runways...");
            adjustLandingTimes();
        }

        for (Flight flight : flights) {
            schedule.put(flight, flightColors.get(flight) % airport.getRunways());
        }
    }

    private void adjustLandingTimes() {
        LocalTime lastTime = flights.get(0).getStartTime();
        for (Flight flight : flights) {
            if (flight.getStartTime().isBefore(lastTime.plusMinutes(10))) {
                flight.adjustLandingTime(lastTime.plusMinutes(10));
            }
            lastTime = flight.getEndTime();
        }
    }

    public void printSchedule() {
        for (Map.Entry<Flight, Integer> entry : schedule.entrySet()) {
            System.out.println("Flight " + entry.getKey().getId() + " assigned to Runway " + (entry.getValue() + 1) + " from " + entry.getKey().getStartTime() + " to " + entry.getKey().getEndTime());
        }
    }
}

public class Bonus {
    public static void main(String[] args) {
        Airport airport = new Airport(1);

        List<Flight> flights = Arrays.asList(
                new Flight("FL123", new Airliner("Boeing 747", "ABC123", 300), LocalTime.of(10, 0), LocalTime.of(10, 30)),
                new Flight("FL456", new Freighter("Cargolux 777", "DEF456", 50000), LocalTime.of(10, 15), LocalTime.of(10, 45)),
                new Flight("FL789", new Drone("DJI Phantom", "GHI789"), LocalTime.of(10, 50), LocalTime.of(11, 20))
        );

        FlightScheduler scheduler = new FlightScheduler(airport, flights);
        scheduler.assignRunways();
        scheduler.printSchedule();
    }
}
