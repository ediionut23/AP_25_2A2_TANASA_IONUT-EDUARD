import java.util.*;

interface PassengerCapable{
    int getPassengerCapacity();
}
interface CargoCapable{
    double getMaxPayload();
}

abstract class Aircraft implements Comparable<Aircraft>{
    protected String name;
    protected String tailNumber;

    public Aircraft(String name, String tailNumber){
        this.name = name;
        this.tailNumber = tailNumber;
    }
    public String getName(){
        return name;
    }
    public String getTailNumber(){
        return tailNumber;
    }
    public int compareTo(Aircraft a){
        return this.name.compareTo(a.name);
    }
    public String toString() {
        return String.format("%s (%s)", name, tailNumber);
    }
}

class Airliner extends Aircraft implements PassengerCapable{
    private int passengerCapacity;

    public Airliner(String name, String tailNumber, int passengerCapacity){
        super(name, tailNumber);
        this.passengerCapacity = passengerCapacity;
    }
    public int getPassengerCapacity(){
        return passengerCapacity;
    }
    public String toString() {
        return super.toString() + " - Passengers: " + passengerCapacity;
    }
}

class Freighter extends Aircraft implements CargoCapable{
    private double maxPayload;

    public Freighter(String name, String tailNumber, double maxPayload){
        super(name, tailNumber);
        this.maxPayload = maxPayload;
    }
    public double getMaxPayload(){
        return maxPayload;
    }
    public String toString() {
        return super.toString() + " - Cargo Capacity: " + maxPayload + " tons";
    }

}

class Drone extends Aircraft implements CargoCapable{
    private double maxPayload;
    private double batteryLife;

    public Drone(String name, String tailNumber, double maxPayload, double batteryLife){
        super(name, tailNumber);
        this.maxPayload = maxPayload;
    }
    public double getMaxPayload(){
        return maxPayload;
    }
    public double getBatteryLife(){
        return batteryLife;
    }
    public String toString() {
        return super.toString() + " - Battery Life: " + batteryLife + " hrs, Cargo Capacity: " + maxPayload + " kg";
    }
}


public class Compulsory {
    public static void main(String[] args) {
        List<Aircraft> aircraftList = new ArrayList<>();

        aircraftList.add(new Airliner("AirBus", "cartof", 416));
        aircraftList.add(new Freighter("Boeing 7", "morcov", 102));

        Collections.sort(aircraftList);

        System.out.println("All Aircraft:");
        for (Aircraft a : aircraftList) {
            System.out.println(a);
        }

        List<CargoCapable> cargoAircraft = new ArrayList<>();
        List<PassengerCapable> passengerAircraft = new ArrayList<>();
        for (Aircraft a : aircraftList) {
            if (a instanceof CargoCapable) {
                cargoAircraft.add((CargoCapable) a);
            }else if(a instanceof PassengerCapable){
                passengerAircraft.add((PassengerCapable) a);
            }
        }

        System.out.println("\nCargo Transport Capable Aircraft:");
        for (CargoCapable c : cargoAircraft) {
            System.out.println(c);
        }
        System.out.println("\nPassenger Aircraft:");
        for (PassengerCapable p : passengerAircraft) {
            System.out.println(p);
        }
    }
}
