package com.example.worldcities.controller;

import com.example.worldcities.model.Continent;
import com.example.worldcities.repository.ContinentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/continents")
@Tag(name = "Continent", description = "The Continent API")
public class ContinentController {
    
    @Autowired
    private ContinentRepository continentRepository;
    
    @GetMapping
    @Operation(summary = "Get all continents", description = "Returns a list of all continents in the database")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of continents")
    public List<Continent> getAllContinents() {
        return continentRepository.findAll();
    }
    
    @PostMapping
    @Operation(summary = "Create a new continent", description = "Creates a new continent or returns existing one if name already exists")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully created continent or returned existing one"),
        @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<?> createContinent(@RequestBody Continent continent) {
        Continent existingContinent = continentRepository.findByName(continent.getName());
        if (existingContinent != null) {
            return ResponseEntity.ok(existingContinent);
        }
        
        Continent savedContinent = continentRepository.save(continent);
        return ResponseEntity.ok(savedContinent);
    }
} 