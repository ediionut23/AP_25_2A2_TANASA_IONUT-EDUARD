package org.example.service;

import org.example.model.Continent;
import org.example.model.Country;
import org.example.repository.ContinentRepository;
import org.example.repository.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeographyService {
    private final ContinentRepository continentRepository;
    private final CountryRepository countryRepository;

    @Autowired
    public GeographyService(ContinentRepository continentRepository, CountryRepository countryRepository) {
        this.continentRepository = continentRepository;
        this.countryRepository = countryRepository;
    }

    @Transactional
    public Continent createContinent(String name) {
        Continent continent = new Continent();
        continent.setName(name);
        return continentRepository.save(continent);
    }

    @Transactional
    public Country createCountry(String name, String code, Integer continentId) {
        Continent continent = continentRepository.findById(continentId)
                .orElseThrow(() -> new RuntimeException("Continent not found"));

        Country country = new Country();
        country.setName(name);
        country.setCode(code);
        country.setContinent(continent);
        return countryRepository.save(country);
    }

    public Continent findContinentById(Integer id) {
        return continentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Continent not found"));
    }

    public Country findCountryById(Integer id) {
        return countryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Country not found"));
    }
} 