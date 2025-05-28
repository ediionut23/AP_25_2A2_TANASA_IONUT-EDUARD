package com.example.worldcities.controller;

import com.example.worldcities.model.City;
import com.example.worldcities.model.Country;
import com.example.worldcities.repository.CityRepository;
import com.example.worldcities.repository.CountryRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cities")
@Tag(name = "City", description = "The City API")
public class CityController {
    
    @Autowired
    private CityRepository cityRepository;
    
    @Autowired
    private CountryRepository countryRepository;
    
    @GetMapping
    @Operation(summary = "Get all cities", description = "Returns a list of all cities in the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of cities")
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
    
    @PostMapping
    @Operation(summary = "Create a new city", description = "Creates a new city in the database")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created the city"),
        @ApiResponse(responseCode = "400", description = "Invalid input - country ID is missing or invalid")
    })
    public ResponseEntity<Object> createCity(@RequestBody City city) {
        if (city.getCountry() == null || city.getCountry().getId() == null) {
            return ResponseEntity.badRequest().body("Country ID is required");
        }
        
        Optional<Country> countryOpt = countryRepository.findById(city.getCountry().getId());
        if (countryOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Country not found with id: " + city.getCountry().getId());
        }
        
        city.setCountry(countryOpt.get());
        City savedCity = cityRepository.save(city);
        return ResponseEntity.ok().body(savedCity);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update a city", description = "Updates an existing city by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully updated the city"),
        @ApiResponse(responseCode = "404", description = "City not found"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<?> updateCity(
            @Parameter(description = "ID of the city to update") @PathVariable Long id,
            @RequestBody City cityDetails) {
        return cityRepository.findById(id)
            .map(city -> {
                city.setName(cityDetails.getName());
                if (cityDetails.getCountry() != null && cityDetails.getCountry().getId() != null) {
                    countryRepository.findById(cityDetails.getCountry().getId())
                        .ifPresent(city::setCountry);
                }
                City updatedCity = cityRepository.save(city);
                return ResponseEntity.ok(updatedCity);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a city", description = "Deletes a city by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully deleted the city"),
        @ApiResponse(responseCode = "404", description = "City not found")
    })
    public ResponseEntity<?> deleteCity(
            @Parameter(description = "ID of the city to delete") @PathVariable Long id) {
        return cityRepository.findById(id)
            .map(city -> {
                cityRepository.delete(city);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 