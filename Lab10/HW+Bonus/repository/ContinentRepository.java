package org.example.repository;

import org.example.model.Continent;

import java.util.List;

public class ContinentRepository extends AbstractRepository<Continent, Long> {

    public ContinentRepository() {
        super(Continent.class);
    }

    public List<Continent> findByName(String pattern) {
        return super.findByName(pattern, "Continent.findByName");
    }
}
