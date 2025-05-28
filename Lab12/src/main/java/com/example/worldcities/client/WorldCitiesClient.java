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
import org.springframework.web.client.HttpClientErrorException;
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

            try {
                ResponseEntity<List<Continent>> continentsResponse = restTemplate.exchange(
                    BASE_URL + "/continents",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Continent>>() {}
                );
                
                Continent europe = null;
                if (continentsResponse.getBody() != null) {
                    europe = continentsResponse.getBody().stream()
                        .filter(c -> "Europe".equals(c.getName()))
                        .findFirst()
                        .orElse(null);
                }

                if (europe == null) {
                    europe = new Continent();
                    europe.setName("Europe");
                    try {
                        europe = restTemplate.postForObject(
                            BASE_URL + "/continents",
                            europe,
                            Continent.class
                        );
                    } catch (HttpClientErrorException e) {
                        europe = restTemplate.getForObject(
                            BASE_URL + "/continents/search/byName?name=Europe",
                            Continent.class
                        );
                    }
                }

                ResponseEntity<List<Country>> countriesResponse = restTemplate.exchange(
                    BASE_URL + "/countries",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Country>>() {}
                );
                
                Country romania = null;
                if (countriesResponse.getBody() != null) {
                    romania = countriesResponse.getBody().stream()
                        .filter(c -> "RO".equals(c.getCode()))
                        .findFirst()
                        .orElse(null);
                }

                if (romania == null) {
                    romania = new Country();
                    romania.setName("Romania");
                    romania.setCode("RO");
                    romania.setContinent(europe);
                    try {
                        romania = restTemplate.postForObject(
                            BASE_URL + "/countries",
                            romania,
                            Country.class
                        );
                    } catch (HttpClientErrorException e) {
                        romania = restTemplate.getForObject(
                            BASE_URL + "/countries/search/byCode?code=RO",
                            Country.class
                        );
                    }
                }

                City bucharest = new City();
                bucharest.setName("Bucharest");
                bucharest.setCountry(romania);
                bucharest.setCapital(true);
                bucharest.setLatitude(44.4268);
                bucharest.setLongitude(26.1025);

                System.out.println("\nCreating a new city...");
                City createdCity = restTemplate.postForObject(
                    BASE_URL + "/cities",
                    bucharest,
                    City.class
                );
                System.out.println("Created city: " + createdCity.getName());

                System.out.println("\nGetting all cities...");
                ResponseEntity<List<City>> response = restTemplate.exchange(
                    BASE_URL + "/cities",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<City>>() {}
                );
                List<City> cities = response.getBody();
                cities.forEach(city -> System.out.println("Found city: " + city.getName()));

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

                if (createdCity != null && createdCity.getId() != null) {
                    System.out.println("\nDeleting city...");
                    restTemplate.delete(BASE_URL + "/cities/" + createdCity.getId());
                    System.out.println("City deleted");
                }

                System.out.println("\nAll tests completed!");
            } catch (Exception e) {
                System.err.println("Error during API testing: " + e.getMessage());
                e.printStackTrace();
            }
        };
    }
} 