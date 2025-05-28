package com.example.worldcities.repository;

import com.example.worldcities.model.Continent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

@RepositoryRestResource
public interface ContinentRepository extends JpaRepository<Continent, Long> {
    @RestResource(path = "byName")
    Continent findByName(@Param("name") String name);
} 