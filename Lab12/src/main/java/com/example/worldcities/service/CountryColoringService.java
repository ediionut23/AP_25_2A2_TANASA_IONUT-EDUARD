package com.example.worldcities.service;

import com.example.worldcities.model.Country;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CountryColoringService {

    public Map<Country, String> assignColors(List<Country> countries, Map<Country, Set<Country>> neighbors) {
        // Available colors
        List<String> colors = Arrays.asList("#FF0000", "#00FF00", "#0000FF", "#FFFF00");
        
        Map<Country, String> colorAssignment = new HashMap<>();
        
        // Sort countries by degree (number of neighbors) in descending order
        List<Country> sortedCountries = new ArrayList<>(countries);
        sortedCountries.sort((c1, c2) -> {
            int degree1 = neighbors.getOrDefault(c1, Collections.emptySet()).size();
            int degree2 = neighbors.getOrDefault(c2, Collections.emptySet()).size();
            return Integer.compare(degree2, degree1);
        });
        
        // Assign colors to countries
        for (Country country : sortedCountries) {
            // Get colors used by neighbors
            Set<String> usedColors = new HashSet<>();
            Set<Country> countryNeighbors = neighbors.getOrDefault(country, Collections.emptySet());
            
            for (Country neighbor : countryNeighbors) {
                String neighborColor = colorAssignment.get(neighbor);
                if (neighborColor != null) {
                    usedColors.add(neighborColor);
                }
            }
            
            // Find first available color
            String selectedColor = colors.stream()
                .filter(color -> !usedColors.contains(color))
                .findFirst()
                .orElse(colors.get(0));
            
            colorAssignment.put(country, selectedColor);
        }
        
        return colorAssignment;
    }
} 