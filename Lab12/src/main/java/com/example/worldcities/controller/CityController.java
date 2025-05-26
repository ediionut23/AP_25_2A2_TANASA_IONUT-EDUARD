package com.example.worldcities.controller;

import com.example.worldcities.model.City;
import com.example.worldcities.repository.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityController {
    
    @Autowired
    private CityRepository cityRepository;
    
    @GetMapping
    public List<City> getAllCities() {
        return cityRepository.findAll();
    }
    
    @PostMapping
    public City createCity(@RequestBody City city) {
        return cityRepository.save(city);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<City> updateCity(@PathVariable Long id, @RequestBody City cityDetails) {
        return cityRepository.findById(id)
            .map(city -> {
                city.setName(cityDetails.getName());
                City updatedCity = cityRepository.save(city);
                return ResponseEntity.ok(updatedCity);
            })
            .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCity(@PathVariable Long id) {
        return cityRepository.findById(id)
            .map(city -> {
                cityRepository.delete(city);
                return ResponseEntity.ok().build();
            })
            .orElse(ResponseEntity.notFound().build());
    }
} 