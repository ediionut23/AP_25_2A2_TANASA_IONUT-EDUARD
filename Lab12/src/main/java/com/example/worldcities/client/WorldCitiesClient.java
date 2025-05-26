package com.example.worldcities.client;

import com.example.worldcities.model.City;
import com.example.worldcities.model.Continent;
import com.example.worldcities.model.Country;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class WorldCitiesClient {

    private final String BASE_URL = "http://localhost:8080/api";
    private final RestTemplate restTemplate;

    public WorldCitiesClient(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Bean
    public CommandLineRunner run() {
        return args -> {
            System.out.println("Testing World Cities API...");

            // Create test data
            Continent europe = new Continent();
            europe.setName("Europe");

            Country romania = new Country();
            romania.setName("Romania");
            romania.setCode("RO");
            romania.setContinent(europe);

            City bucharest = new City();
            bucharest.setName("Bucharest");
            bucharest.setCountry(romania);
            bucharest.setCapital(true);
            bucharest.setLatitude(44.4268);
            bucharest.setLongitude(26.1025);

            // Test POST - Create a new city
            System.out.println("\nCreating a new city...");
            City createdCity = restTemplate.postForObject(
                BASE_URL + "/cities",
                bucharest,
                City.class
            );
            System.out.println("Created city: " + createdCity.getName());

            // Test GET - Get all cities
            System.out.println("\nGetting all cities...");
            ResponseEntity<List<City>> response = restTemplate.exchange(
                BASE_URL + "/cities",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<City>>() {}
            );
            List<City> cities = response.getBody();
            cities.forEach(city -> System.out.println("Found city: " + city.getName()));

            // Test PUT - Update city name
            if (createdCity != null && createdCity.getId() != null) {
                System.out.println("\nUpdating city name...");
                City cityUpdate = new City();
                cityUpdate.setName("București");
                restTemplate.put(
                    BASE_URL + "/cities/" + createdCity.getId(),
                    cityUpdate
                );
                System.out.println("City name updated");
            }

            // Test DELETE - Delete the city
            if (createdCity != null && createdCity.getId() != null) {
                System.out.println("\nDeleting city...");
                restTemplate.delete(BASE_URL + "/cities/" + createdCity.getId());
                System.out.println("City deleted");
            }

            System.out.println("\nAll tests completed!");
        };
    }
} 