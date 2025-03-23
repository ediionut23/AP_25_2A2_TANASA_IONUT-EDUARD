import java.time.LocalTime;
import java.util.*;

interface PassengerCapable {
    int getPassengerCapacity();
}
interface CargoCapable{
    double getMaxPayload();
}
abstract class Aircraft implements Comparable<Aircraft> {
    protected String name;
    protected String tailNumber;

    public Aircraft(String name, String tailNumber) {
        this.name = name;
        this.tailNumber = tailNumber;
    }

    public String getName() { return name; }

    @Override
    public int compareTo(Aircraft other) {
        return this.name.compareTo(other.name);
    }
}

class Airliner extends Aircraft implements PassengerCapable{
    private int passengerCapacity;

    public Airliner(String name, String tailNumber, int passengerCapacity) {
        super(name, tailNumber);
        this.passengerCapacity = passengerCapacity;
    }

    @Override
    public int getPassengerCapacity() { return passengerCapacity; }
}

class Freighter extends Aircraft implements CargoCapable{
    private double maxPayload;

    public Freighter(String name, String tailNumber, double maxPayload) {
        super(name, tailNumber);
        this.maxPayload = maxPayload;
    }

    @Override
    public double getMaxPayload() { return maxPayload; }
}

class Drone extends Aircraft {
    private int batteryLife;

    public Drone(String name, String tailNumber, int batteryLife) {
        super(name, tailNumber);
        this.batteryLife = batteryLife;
    }

    public int getBatteryLife() { return batteryLife; }
}

class Flight{
    private String id;
    private Aircraft aircraft;
    private LocalTime arrivalStart;
    private LocalTime arrivalEnd;

    public Flight(String id, Aircraft aircraft, LocalTime arrivalStart, LocalTime arrivalEnd){
        this.id = id;
        this.aircraft = aircraft;
        this.arrivalStart = arrivalStart;
        this.arrivalEnd = arrivalEnd;
    }

    public boolean conflictsWith(Flight other){
        return !(this.arrivalEnd.isBefore(other.arrivalStart) || this.arrivalStart.isAfter(other.arrivalEnd));
    }
    public String getId(){
        return id;
    }
    public Aircraft getAircraft(){
        return aircraft;
    }
}

class Airport{
    private int numRunways;
    private List<String> runwaysNames;
    public Airport(int numRunways){
        this.numRunways = numRunways;
        this.runwaysNames = new ArrayList<>();
        for(int i = 1; i <= numRunways; i++){
            runwaysNames.add("Runway " + i);
        }
    }
    public List<String> getRunways(){
        return runwaysNames;
    }
}

class SchedulingProblem {
    private Airport airport;
    private List<Flight> flights;
    private Map<Flight, String> schedule;

    public SchedulingProblem(Airport airport, List<Flight> flights){
        this.airport = airport;
        this.flights = flights;
        this.schedule = new HashMap<>();
    }

    public void solve() {
        List<String> availableRunways = airport.getRunways();
        Map<String, List<Flight>> runwayAssignments = new HashMap<>();
        for (String runway : availableRunways) {
            runwayAssignments.put(runway, new ArrayList<>());
        }

        for (Flight flight : flights) {
            for (String runway : availableRunways) {
                boolean ok = true;
                for (Flight assignedFlight : runwayAssignments.get(runway)) {
                    if (flight.conflictsWith(assignedFlight)) {
                        ok = false;
                        break;
                    }
                }
                if (ok) {

                    schedule.put(flight, runway);
                    runwayAssignments.get(runway).add(flight);
                    break;
                }
            }
        }
    }

        public void printSchedule(){
            for(Map.Entry<Flight, String> entry : schedule.entrySet()){
                System.out.println("Flight " + entry.getKey().getId() + " assigned to " + entry.getValue());
            }
        }

}

public class hm{
    public static void main(String[] args){
        Airport airport = new Airport(4);

        List<Flight> flights = Arrays.asList(
                new Flight("FL123", new Airliner("Boeing 747", "ABC123", 300), LocalTime.of(10, 0), LocalTime.of(10, 30)),
                new Flight("FL456", new Freighter("Cargolux 777", "DEF456", 50000), LocalTime.of(10, 0), LocalTime.of(10, 30)),
                new Flight("FL126", new Airliner("Boeing 747", "ABC123", 300), LocalTime.of(10, 0), LocalTime.of(10, 30)),
                new Flight("FL128", new Airliner("Boeing 747", "ABC123", 300), LocalTime.of(10, 0), LocalTime.of(10, 30)),
                new Flight("FL789", new Drone("DJI Phantom", "GHI789", 60), LocalTime.of(10, 50), LocalTime.of(11, 20))
        );

        SchedulingProblem problem = new SchedulingProblem(airport, flights);
        problem.solve();
        problem.printSchedule();
    }
}


