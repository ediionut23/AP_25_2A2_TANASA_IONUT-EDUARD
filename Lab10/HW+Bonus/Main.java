package org.example;

import org.example.factory.AbstractFactory;
import org.example.model.*;
import org.example.repository.*;
import org.example.util.PersistenceUtil;
import org.example.solver.CityConstrainsSolver;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ContinentRepository continentRepo = new ContinentRepository();
        CountryRepository countryRepo = new CountryRepository();
        Repository<City, Long> cityRepo = AbstractFactory.getCityRepository();

        try {
            System.out.println("=== POPULARE BAZĂ DE DATE ===");

            Continent europe = new Continent("Europe");
            Continent asia = new Continent("Asia");
            continentRepo.create(europe);
            continentRepo.create(asia);
            System.out.println("Continente create: Europe, Asia");

            Country romania = new Country("Romania", "RO", europe);
            Country france = new Country("France", "FR", europe);
            Country germany = new Country("Germany", "DE", europe);
            Country spain = new Country("Spain", "ES", europe);
            Country italy = new Country("Italy", "IT", europe);

            countryRepo.create(romania);
            countryRepo.create(france);
            countryRepo.create(germany);
            countryRepo.create(spain);
            countryRepo.create(italy);
            System.out.println("Țări create în Europa: Romania, France, Germany, Spain, Italy");

            Country china = new Country("China", "CN", asia);
            Country india = new Country("India", "IN", asia);
            countryRepo.create(china);
            countryRepo.create(india);
            System.out.println("Țări create în Asia: China, India");

            City bucharest = new City("Bucharest", romania, true, 44.4268, 26.1025, 1800000);
            City brasov = new City("Brasov", romania, false, 45.6427, 25.5887, 290000);
            cityRepo.create(bucharest);
            cityRepo.create(brasov);

            City bordeaux = new City("Bordeaux", france, false, 44.8378, -0.5792, 250000);
            City brest = new City("Brest", france, false, 48.3904, -4.4861, 140000);
            cityRepo.create(bordeaux);
            cityRepo.create(brest);

            City berlin = new City("Berlin", germany, true, 52.5200, 13.4050, 3700000);
            City bremen = new City("Bremen", germany, false, 53.0793, 8.8017, 550000);
            cityRepo.create(berlin);
            cityRepo.create(bremen);

            City barcelona = new City("Barcelona", spain, false, 41.3851, 2.1734, 1620000);
            City bilbao = new City("Bilbao", spain, false, 43.2630, -2.9350, 350000);
            cityRepo.create(barcelona);
            cityRepo.create(bilbao);

            City bologna = new City("Bologna", italy, false, 44.4949, 11.3426, 390000);
            cityRepo.create(bologna);

            City beijing = new City("Beijing", china, true, 39.9042, 116.4074, 21500000);
            cityRepo.create(beijing);

            City bangalore = new City("Bangalore", india, false, 12.9716, 77.5946, 8400000);
            City bhopal = new City("Bhopal", india, false, 23.2599, 77.4126, 1800000);
            cityRepo.create(bangalore);
            cityRepo.create(bhopal);

            City clujNapoca = new City("Cluj-Napoca", romania, false, 46.7712, 23.6236, 330000);
            City paris = new City("Paris", france, true, 48.8566, 2.3522, 2200000);
            City rome = new City("Rome", italy, true, 41.9028, 12.4964, 2800000);

            cityRepo.create(clujNapoca);
            cityRepo.create(paris);
            cityRepo.create(rome);

            System.out.println("Orașe create: au fost create 16 orașe, dintre care 13 încep cu B");

            System.out.println("\n=== TESTARE REPOSITORY ===");

            System.out.println("Țări care încep cu R:");
            List<Country> countries = countryRepo.findByName("R%");
            for (Country c : countries) {
                System.out.println("- " + c.getName() + " (" + c.getCode() + ")");
            }

            System.out.println("\nOrașe din România:");
            List<City> romanianCities = cityRepo.findByName("%");
            for (City city : romanianCities) {
                if (city.getCountry().getName().equals("Romania")) {
                    System.out.println("- " + city.getName() +
                            (city.isCapital() ? " (Capitală)" : "") +
                            ", Populație: " + city.getPopulation());
                }
            }

            System.out.println("\n=== DEMONSTRARE CONSTRAINT SOLVER ===");

            char startingLetter = 'B';
            long minPopulation = 1000000;
            long maxPopulation = 5000000;

            System.out.println("Căutăm orașe care:");
            System.out.println("- Încep cu litera: " + startingLetter);
            System.out.println("- Suma populației între: " + minPopulation + " și " + maxPopulation);
            System.out.println("- Sunt din țări diferite");

            CityConstrainsSolver solver = new CityConstrainsSolver();
            List<City> result = solver.findCitiesWithConstraints(startingLetter, minPopulation, maxPopulation);

            if (result.isEmpty()) {
                System.out.println("\nNu s-a găsit nicio soluție pentru constrângerile date.");
            } else {
                System.out.println("\nSOLUȚIE GĂSITĂ! " + result.size() + " orașe selectate:");
                long totalPopulation = 0;

                System.out.printf("%-15s %-15s %-15s\n", "Oraș", "Țară", "Populație");
                System.out.println("-----------------------------------------------");

                for (City city : result) {
                    System.out.printf("%-15s %-15s %-15d\n",
                            city.getName(),
                            city.getCountry().getName(),
                            city.getPopulation());
                    totalPopulation += city.getPopulation();
                }

                System.out.println("-----------------------------------------------");
                System.out.println("Populație totală: " + totalPopulation);
                System.out.println("Verificare constrângeri: " +
                        (totalPopulation >= minPopulation && totalPopulation <= maxPopulation ?
                                "ÎNDEPLINITE ✓" : "NEÎNDEPLINITE ✗"));
            }

            System.out.println("\nAL DOILEA TEST cu alte constrângeri:");
            minPopulation = 6000000;
            maxPopulation = 10000000;

            System.out.println("- Încep cu litera: " + startingLetter);
            System.out.println("- Suma populației între: " + minPopulation + " și " + maxPopulation);
            System.out.println("- Sunt din țări diferite");

            result = solver.findCitiesWithConstraints(startingLetter, minPopulation, maxPopulation);

            if (result.isEmpty()) {
                System.out.println("\nNu s-a găsit nicio soluție pentru constrângerile date.");
            } else {
                System.out.println("\nSOLUȚIE GĂSITĂ! " + result.size() + " orașe selectate:");
                long totalPopulation = 0;

                System.out.printf("%-15s %-15s %-15s\n", "Oraș", "Țară", "Populație");
                System.out.println("-----------------------------------------------");

                for (City city : result) {
                    System.out.printf("%-15s %-15s %-15d\n",
                            city.getName(),
                            city.getCountry().getName(),
                            city.getPopulation());
                    totalPopulation += city.getPopulation();
                }

                System.out.println("-----------------------------------------------");
                System.out.println("Populație totală: " + totalPopulation);
                System.out.println("Verificare constrângeri: " +
                        (totalPopulation >= minPopulation && totalPopulation <= maxPopulation ?
                                "ÎNDEPLINITE ✓" : "NEÎNDEPLINITE ✗"));
            }

        } finally {
            PersistenceUtil.close();
        }
    }
}