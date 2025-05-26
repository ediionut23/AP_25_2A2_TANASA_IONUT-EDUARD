package com.example.worldcities.config;

import com.example.worldcities.model.City;
import com.example.worldcities.model.Continent;
import com.example.worldcities.model.Country;
import com.example.worldcities.repository.CityRepository;
import com.example.worldcities.repository.ContinentRepository;
import com.example.worldcities.repository.CountryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(
            ContinentRepository continentRepository,
            CountryRepository countryRepository,
            CityRepository cityRepository) {
        return args -> {
            // Create continents
            Continent europe = new Continent();
            europe.setName("Europe");
            europe = continentRepository.save(europe);

            Continent asia = new Continent();
            asia.setName("Asia");
            asia = continentRepository.save(asia);

            // Create countries
            Country romania = new Country();
            romania.setName("Romania");
            romania.setCode("RO");
            romania.setContinent(europe);
            romania = countryRepository.save(romania);

            Country japan = new Country();
            japan.setName("Japan");
            japan.setCode("JP");
            japan.setContinent(asia);
            japan = countryRepository.save(japan);

            // Create cities
            City bucharest = new City();
            bucharest.setName("Bucharest");
            bucharest.setCountry(romania);
            bucharest.setCapital(true);
            bucharest.setLatitude(44.4268);
            bucharest.setLongitude(26.1025);
            cityRepository.save(bucharest);

            City tokyo = new City();
            tokyo.setName("Tokyo");
            tokyo.setCountry(japan);
            tokyo.setCapital(true);
            tokyo.setLatitude(35.6762);
            tokyo.setLongitude(139.6503);
            cityRepository.save(tokyo);
        };
    }
} 