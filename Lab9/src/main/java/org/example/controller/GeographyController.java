package org.example.controller;

import org.example.model.Continent;
import org.example.model.Country;
import org.example.service.GeographyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class GeographyController {
    private final GeographyService geographyService;

    @Autowired
    public GeographyController(GeographyService geographyService) {
        this.geographyService = geographyService;
    }

    @PostMapping("/continents")
    public ResponseEntity<Continent> createContinent(@RequestParam String name) {
        return ResponseEntity.ok(geographyService.createContinent(name));
    }

    @PostMapping("/countries")
    public ResponseEntity<Country> createCountry(
            @RequestParam String name,
            @RequestParam String code,
            @RequestParam Integer continentId) {
        return ResponseEntity.ok(geographyService.createCountry(name, code, continentId));
    }

    @GetMapping("/continents/{id}")
    public ResponseEntity<Continent> getContinent(@PathVariable Integer id) {
        return ResponseEntity.ok(geographyService.findContinentById(id));
    }

    @GetMapping("/countries/{id}")
    public ResponseEntity<Country> getCountry(@PathVariable Integer id) {
        return ResponseEntity.ok(geographyService.findCountryById(id));
    }
} 