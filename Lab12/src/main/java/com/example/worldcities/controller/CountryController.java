package com.example.worldcities.controller;

import com.example.worldcities.model.Country;
import com.example.worldcities.repository.CountryRepository;
import com.example.worldcities.service.CountryColoringService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/countries")
@Tag(name = "Countries", description = "The Countries API")
public class CountryController {
    
    @Autowired
    private CountryRepository countryRepository;
    
    @Autowired
    private CountryColoringService countryColoringService;
    
    @GetMapping
    @Operation(summary = "Get all countries")
    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    @PostMapping
    @Operation(summary = "Create a new country")
    public ResponseEntity<Country> createCountry(@RequestBody Country country) {
        Country savedCountry = countryRepository.save(country);
        return ResponseEntity.ok(savedCountry);
    }

    @GetMapping("/colors")
    @Operation(
        summary = "Get country colors",
        description = "Assigns colors to countries such that no neighboring countries have the same color",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    public Map<String, String> getCountryColors() {
        List<Country> countries = countryRepository.findAll();
        
        Map<Country, Set<Country>> neighbors = new HashMap<>();
        for (int i = 0; i < countries.size(); i++) {
            Set<Country> countryNeighbors = new HashSet<>();
            if (i > 0) countryNeighbors.add(countries.get(i - 1));
            if (i < countries.size() - 1) countryNeighbors.add(countries.get(i + 1));
            neighbors.put(countries.get(i), countryNeighbors);
        }
        
        Map<Country, String> colorAssignment = countryColoringService.assignColors(countries, neighbors);
        
        Map<String, String> response = new HashMap<>();
        colorAssignment.forEach((country, color) -> response.put(country.getName(), color));
        
        return response;
    }
} 