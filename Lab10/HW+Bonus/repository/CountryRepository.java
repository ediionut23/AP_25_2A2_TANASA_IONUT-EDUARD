package org.example.repository;

import org.example.model.Country;

import java.util.List;

public class CountryRepository extends AbstractRepository<Country, Long> {
    public CountryRepository() {
        super(Country.class);
    }

    public List<Country> findByName(String pattern) {
        return super.findByName(pattern, "Country.findByName");
    }
}
